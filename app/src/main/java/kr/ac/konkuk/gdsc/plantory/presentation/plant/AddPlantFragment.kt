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
import coil.load
import coil.transform.RoundedCornersTransformation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kr.ac.konkuk.gdsc.plantory.R
import kr.ac.konkuk.gdsc.plantory.databinding.FragmentAddPlantBinding
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment
import kr.ac.konkuk.gdsc.plantory.util.fragment.viewLifeCycle
import kr.ac.konkuk.gdsc.plantory.util.view.setOnSingleClickListener
import java.util.Calendar

@AndroidEntryPoint
class AddPlantFragment : BindingFragment<FragmentAddPlantBinding>(R.layout.fragment_add_plant) {

    private val viewModel: AddPlantViewModel by viewModels()
    private var calendar: Calendar = Calendar.getInstance()
    private lateinit var adapter: ArrayAdapter<String>
    private val currYear: Int
        get() = calendar.get(Calendar.YEAR)

    private val currMonth: Int
        get() = calendar.get(Calendar.MONTH)

    private val currDay: Int
        get() = calendar.get(Calendar.DAY_OF_MONTH)

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
        openGallery()
        autoCompletePlantSpeciesListener()
        initTextChangeListener()
        updateBirthdayDatePicker()
        updateLastWateredDatePicker()
    }

    private fun initTextChangeListener() {
        initNicknameTextChangeListener()
        initSpeciesTextChangeListener()
        initDescriptionTextChangeListener()
    }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                viewModel.updateProfileImage(uri)
                binding.ivAddplantProfile.load(uri) {
                    transformations(RoundedCornersTransformation(radius = 14f))
                    crossfade(true)
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
        binding.tvAddplantBirthday.text = returnDateFormat(currYear, currMonth, currDay)
        binding.tvAddplantLastWatered.text = returnDateFormat(currYear, currMonth, currDay)
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
        viewLifeCycle.coroutineScope.launch {
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
                currYear,
                currMonth,
                currDay
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