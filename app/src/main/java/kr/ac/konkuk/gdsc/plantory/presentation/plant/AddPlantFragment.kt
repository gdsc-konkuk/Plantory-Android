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
import androidx.lifecycle.viewModelScope
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
import kr.ac.konkuk.gdsc.plantory.util.multipart.ContentUriRequestBody
import kr.ac.konkuk.gdsc.plantory.util.view.UiState
import kr.ac.konkuk.gdsc.plantory.util.view.setOnSingleClickListener
import timber.log.Timber

@AndroidEntryPoint
class AddPlantFragment : BindingFragment<FragmentAddPlantBinding>(R.layout.fragment_add_plant) {

    private val viewModel: AddPlantViewModel by viewModels()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        addListener()
        setPostRegisterPlantStateObserver()
    }

    private fun initView() {
        initDatePickerToCurrent()
        initAutoCompleteAdapter()
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
                viewModel.updatePlantImage(uri)
                updateRequestBody()
            }
        }

    private fun initRegisterButtonClickListener() {
        binding.btnAddplantUpload.setOnSingleClickListener {
            viewModel.viewModelScope.launch {
                viewModel.postRegisterPlant()
            }
        }
    }

    private fun setPostRegisterPlantStateObserver() {
        viewModel.postRegisterPlantState.flowWithLifecycle(viewLifeCycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    Timber.d("Success : Register ")
                    parentFragmentManager.popBackStack()
                }

                is UiState.Failure -> {
                    Timber.d("Failure : ${state.msg}")
                }

                is UiState.Empty -> {
                }

                is UiState.Loading -> {
                }
            }

        }.launchIn(viewLifeCycleScope)
    }

    private fun initImageUriChangeListener() {
        viewLifeCycleScope.launch {
            viewModel.plantImage.collectLatest { plantImage ->
                if (plantImage == null) binding.ivAddplantProfile.setImageResource(R.drawable.ic_addplant_profile)
                else binding.ivAddplantProfile.setImageUrl(plantImage.toString())
            }
        }
    }

    private fun initNicknameTextChangeListener() {
        binding.etAddplantNickname.doAfterTextChanged {
            val newPlantInfo = viewModel.plantRegisterItem.value.copy(nickname = it.toString())
            viewModel.updatePlantInfo(newPlantInfo)
            updateRegisterBtnState()
        }
    }

    private fun initSpeciesTextChangeListener() {
        binding.tvAddplantSpecies.doAfterTextChanged {
            val newPlantInfo = viewModel.plantRegisterItem.value.copy(species = it.toString())
            viewModel.updatePlantInfo(newPlantInfo)
            updatePlantSpeciesLength()
            updateRegisterBtnState()
        }
    }

    private fun initDescriptionTextChangeListener() {
        binding.etAddplantShortDescription.doAfterTextChanged {
            val newPlantInfo = viewModel.plantRegisterItem.value.copy(shortDescription = it.toString())
            viewModel.updatePlantInfo(newPlantInfo)
            updateRegisterBtnState()
        }
    }

    private fun updateRequestBody() {
        val imageUri = viewModel.plantImage.value ?: return
        val requestBody = ContentUriRequestBody(requireContext(), imageUri)
        viewModel.updateRequestBody(requestBody)
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
        binding.tvAddplantBirthday.text = returnDateFormat(
            viewModel.currentYear.value,
            viewModel.currentMonth.value,
            viewModel.currentDay.value
        )
        binding.tvAddplantLastWatered.text = returnDateFormat(
            viewModel.currentYear.value,
            viewModel.currentMonth.value,
            viewModel.currentDay.value
        )
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
                if (view == binding.tvAddplantBirthday) viewModel.updatePlantInfo(
                    viewModel.plantRegisterItem.value.copy(
                        birthDate = returnDateFormat(year, month, day)
                    )
                )
                else if (view == binding.tvAddplantLastWatered) viewModel.updatePlantInfo(
                    viewModel.plantRegisterItem.value.copy(
                        lastWaterDate = returnDateFormat(year, month, day)
                    )
                )
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
                viewModel.plantSpeciesList
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
        return "${year}-${formatDateToAddZero(month+1)}-${formatDateToAddZero(day)}"
    }

    private fun formatDateToAddZero(date: Int): String {
        if (date < 10){
            return String.format("%02d", date)
        }
        return date.toString()
    }
}