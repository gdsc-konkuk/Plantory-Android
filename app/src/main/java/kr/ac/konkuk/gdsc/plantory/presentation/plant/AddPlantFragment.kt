package kr.ac.konkuk.gdsc.plantory.presentation.plant

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.flowWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kr.ac.konkuk.gdsc.plantory.R
import kr.ac.konkuk.gdsc.plantory.databinding.FragmentAddPlantBinding
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment
import kr.ac.konkuk.gdsc.plantory.util.binding.setImageUrl
import kr.ac.konkuk.gdsc.plantory.util.binding.setRegisterBackgroundResource
import kr.ac.konkuk.gdsc.plantory.util.fragment.viewLifeCycle
import kr.ac.konkuk.gdsc.plantory.util.fragment.viewLifeCycleScope
import kr.ac.konkuk.gdsc.plantory.util.view.UiState
import kr.ac.konkuk.gdsc.plantory.util.view.setOnSingleClickListener
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class AddPlantFragment : BindingFragment<FragmentAddPlantBinding>(R.layout.fragment_add_plant) {

    private val viewModel: AddPlantViewModel by viewModels()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getPlantInformations()
        initView()
        addListener()
        setPostRegisterPlantStateObserver()
        setGetPlantInformationsStateObserver()
    }

    private fun initView() {
        initDatePickerToCurrent()
    }

    private fun addListener() {
        initBackButtonClickListener()
        initRegisterButtonClickListener()
        initTextChangeListener()
        openGallery()
        autoCompletePlantSpeciesListener()
        updateBirthdayDatePicker()
        updateLastWateredDatePicker()
    }

    private fun initTextChangeListener() {
        initImageUriChangeListener()
        initNicknameTextChangeListener()
        initSpeciesTextChangeListener()
        initDescriptionTextChangeListener()
    }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                viewModel.updatePlantImage(uri, requireContext())
            }
        }

    private fun initRegisterButtonClickListener() {
        binding.btnAddplantUpload.setOnSingleClickListener {
            viewModel.postRegisterPlant()
        }
    }

    private fun setPostRegisterPlantStateObserver() {
        viewModel.postRegisterPlantState.flowWithLifecycle(viewLifeCycle).onEach { state ->
            when (state) {
                is UiState.Success -> parentFragmentManager.popBackStack()
                is UiState.Failure -> Timber.e("Failure : ${state.msg}")
                is UiState.Empty -> Unit
                is UiState.Loading -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun setGetPlantInformationsStateObserver() {
        viewModel.getPlantInformationState.flowWithLifecycle(viewLifeCycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    viewModel.updatePlantInformation(state.data)
                    initAutoCompleteAdapter()
                }
                is UiState.Failure -> Timber.e("Failure : ${state.msg}")
                is UiState.Empty -> Unit
                is UiState.Loading -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun initImageUriChangeListener() {
        viewLifeCycleScope.launch {
            viewModel.plantImage.collectLatest { plantImage ->
                if (plantImage == null) {
                    binding.ivAddplantProfile.setImageResource(R.drawable.ic_addplant_profile)
                } else {
                    binding.ivAddplantProfile.setImageUrl(plantImage.toString())
                }
            }
        }
    }

    private fun initNicknameTextChangeListener() {
        binding.etAddplantNickname.doAfterTextChanged {
            viewModel.updatePlantInfo(viewModel.plantRegisterItem.value.copy(nickname = it.toString()))
            updateRegisterBtnState()
        }
    }

    private fun initSpeciesTextChangeListener() {
        binding.tvAddplantSpecies.doAfterTextChanged {
            viewModel.updatePlantInfo(viewModel.plantRegisterItem.value.copy(species = it.toString()))
            updatePlantSpeciesLength()
            updateRegisterBtnState()
        }
    }

    private fun initDescriptionTextChangeListener() {
        binding.etAddplantShortDescription.doAfterTextChanged {
            viewModel.updatePlantInfo(viewModel.plantRegisterItem.value.copy(shortDescription = it.toString()))
            updateRegisterBtnState()
        }
    }

    private fun updateRegisterBtnState() {
        val isFieldsNotEmpty = viewModel.checkIsNotEmpty()
        binding.btnAddplantUpload.isEnabled = isFieldsNotEmpty
        binding.btnAddplantUpload.setRegisterBackgroundResource(isFieldsNotEmpty)
    }

    private fun openGallery() {
        binding.ivAddplantProfile.setOnSingleClickListener {
            getContent.launch("image/*")
        }
    }

    private fun initDatePickerToCurrent() {
        val currentDate = returnDateFormat(
            viewModel.currentYear.value,
            viewModel.currentMonth.value,
            viewModel.currentDay.value
        )
        binding.tvAddplantBirthday.text = currentDate
        binding.tvAddplantLastWatered.text = currentDate
    }

    private fun updateBirthdayDatePicker() {
        binding.ivAddplantBirthdayDatepicker.setOnSingleClickListener {
            datePickerListener(binding.tvAddplantBirthday)
        }
    }

    private fun updateLastWateredDatePicker() {
        binding.ivAddplantLastWateredDatepicker.setOnSingleClickListener {
            datePickerListener(binding.tvAddplantLastWatered)
        }
    }

    private fun datePickerListener(view: TextView) {
        viewLifeCycleScope.launch {
            val date = DatePickerDialog.OnDateSetListener { _, year, month, day ->
                view.text = returnDateFormat(year, month, day)
                if (view == binding.tvAddplantBirthday) {
                    viewModel.updatePlantInfo(
                        viewModel.plantRegisterItem.value.copy(
                            birthDate = returnDateFormat(year, month, day)
                        )
                    )
                } else if (view == binding.tvAddplantLastWatered) {
                    viewModel.updatePlantInfo(
                        viewModel.plantRegisterItem.value.copy(
                            lastWaterDate = returnDateFormat(year, month, day)
                        )
                    )
                }
            }
            DatePickerDialog(
                requireContext(),
                date,
                viewModel.currentYear.value,
                viewModel.currentMonth.value,
                viewModel.currentDay.value
            ).show()
        }
    }

    private fun updatePlantSpeciesLength() {
        val currLength = viewModel.updatePlantSpeciesLength()
        binding.counterText.text = "$currLength/14"
    }

    private fun initAutoCompleteAdapter() {
        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            adapter = ArrayAdapter<String>(
                requireContext(),
                R.layout.simple_dropdown_item,
                viewModel.plantInformationList.value.map { plantInformation ->
                    plantInformation.species
                }
            )
            binding.tvAddplantSpecies.setAdapter(adapter)
        }
    }

    private fun autoCompletePlantSpeciesListener() {
        binding.tvAddplantSpecies.setOnItemClickListener { parent, view, position, id ->
            val item = parent.getItemAtPosition(position).toString()
            binding.tvAddplantSpecies.setText(item)
        }
    }

    private fun initBackButtonClickListener() {
        binding.ivAddplantBack.setOnSingleClickListener {
            navigateToHome()
        }
    }

    private fun navigateToHome() {
        parentFragmentManager.popBackStack()
    }

    private fun returnDateFormat(year: Int, month: Int, day: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }
}
