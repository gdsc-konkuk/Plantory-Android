package kr.ac.konkuk.gdsc.plantory.presentation.plant

import android.app.DatePickerDialog
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import coil.load
import dagger.hilt.android.AndroidEntryPoint
import kr.ac.konkuk.gdsc.plantory.R
import kr.ac.konkuk.gdsc.plantory.databinding.FragmentAddPlantBinding
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment
import kr.ac.konkuk.gdsc.plantory.util.view.setOnSingleClickListener
import java.util.Calendar

@AndroidEntryPoint
class AddPlantFragment : BindingFragment<FragmentAddPlantBinding>(R.layout.fragment_add_plant) {

    private var selectedImageUri: Uri? = null
    private val viewModel: AddPlantViewModel by viewModels()
    private var calendar: Calendar = Calendar.getInstance()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        initView()
        addListener()
    }

    private fun initView() {
        initDatePicker()
        initAutoCompleteAdapter()
    }

    private fun addListener() {
        initBackButtonClickListener()
        datePickerListener()
        openGallery()
        autoCompletePlantSpeciesListener()
        initTextChangeListener()
    }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                selectedImageUri = uri
                binding.ivAddplantProfile.load(selectedImageUri) {
                    crossfade(true)
                }
            }
        }

    private fun initTextChangeListener() {
        binding.apply {
            etAddplantBirthday.addTextChangedListener(textWatcher)
            etAddplantNickname.addTextChangedListener(textWatcher)
            tvAddplantSpecies.addTextChangedListener(textWatcher)
            etAddplantShortDescription.addTextChangedListener(textWatcher)
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            updateRegisterBtnState()
            updatePlantSpeciesLength()
        }

        override fun afterTextChanged(s: Editable?) {
        }
    }

    private fun updateRegisterBtnState() {
        val isFieldsNotEmpty =
            binding.etAddplantBirthday.text?.isNotEmpty() == true &&
                    binding.etAddplantNickname.text?.isNotEmpty() == true &&
                    binding.tvAddplantSpecies.text?.isNotEmpty() == true &&
                    binding.etAddplantShortDescription.text?.isNotEmpty() == true

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

    private fun initDatePicker() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        binding.etAddplantBirthday.setText("${year}/${month + 1}/${day}")
    }

    private fun datePickerListener() {
        binding.ivAddplantDatepicker.setOnSingleClickListener {
            val date = DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                binding.etAddplantBirthday.setText("${year}/${month + 1}/${day}")
            }
            DatePickerDialog(
                requireContext(),
                date,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun updatePlantSpeciesLength() {
        val currLength = binding.tvAddplantSpecies.length()
        binding.counterText.text = "$currLength/14"
    }

    private fun initAutoCompleteAdapter() {
        adapter =
            ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                viewModel.plantSpeciesList
            )

        binding.tvAddplantSpecies.setAdapter(adapter)
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
        activity?.supportFragmentManager?.popBackStack()
    }
}