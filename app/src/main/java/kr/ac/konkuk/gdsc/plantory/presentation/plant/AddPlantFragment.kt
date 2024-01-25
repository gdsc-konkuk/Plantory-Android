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
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.RoundedCornersTransformation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kr.ac.konkuk.gdsc.plantory.R
import kr.ac.konkuk.gdsc.plantory.databinding.FragmentAddPlantBinding
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment
import kr.ac.konkuk.gdsc.plantory.util.binding.setImageUrl
import kr.ac.konkuk.gdsc.plantory.util.fragment.viewLifeCycle
import kr.ac.konkuk.gdsc.plantory.util.fragment.viewLifeCycleScope
import kr.ac.konkuk.gdsc.plantory.util.view.setOnSingleClickListener
import java.util.Calendar

@AndroidEntryPoint
class AddPlantFragment : BindingFragment<FragmentAddPlantBinding>(R.layout.fragment_add_plant) {

    private val viewModel: AddPlantViewModel by viewModels()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        addListener()
    }

    private fun initView() {
        initDatePickerToCurrent()
        initAutoCompleteAdapter()
    }

    private fun addListener() {
        initBackButtonClickListener()
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
                viewModel.updateProfileImage(uri)
            }
        }

    private fun initImageUriChangeListener() {
        viewLifeCycleScope.launch {
            viewModel.addImageUri.collectLatest { uri ->
                if (uri == null) binding.ivAddplantProfile.setImageResource(R.drawable.ic_addplant_profile)
                else binding.ivAddplantProfile.setImageUrl(uri.toString())
            }
        }
    }

    private fun initNicknameTextChangeListener() {
        binding.etAddplantNickname.doAfterTextChanged {
            viewModel.updateNickname(it.toString())
            updateRegisterBtnState()
        }
    }

    private fun initSpeciesTextChangeListener() {
        binding.tvAddplantSpecies.doAfterTextChanged {
            viewModel.updateSpecies(it.toString())
            updatePlantSpeciesLength()
            updateRegisterBtnState()
        }
    }

    private fun initDescriptionTextChangeListener() {
        binding.etAddplantShortDescription.doAfterTextChanged {
            viewModel.updateDescription(it.toString())
            updateRegisterBtnState()
        }
    }

    private fun updateRegisterBtnState() {
        val isFieldsNotEmpty = viewModel.checkIsNotEmpty()

        binding.btnAddplantUpload.isEnabled = isFieldsNotEmpty
        binding.btnAddplantUpload.setBackgroundResource(
            if (isFieldsNotEmpty) R.drawable.shape_mint_fill_10
            else R.drawable.shape_gray_200_fill_10
        )
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
                if (view == binding.tvAddplantBirthday) viewModel.updateBirthday(
                    returnDateFormat(year, month, day)
                )
                else if (view == binding.tvAddplantLastWatered) viewModel.updateLastWatered(
                    returnDateFormat(year, month, day)
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
        return "${year}-${month + 1}-${day}"
    }
}