package kr.ac.konkuk.gdsc.plantory.presentation.plant

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
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
    var calendar = Calendar.getInstance()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        initview()
        addListener()
    }

    private fun initview() {
        initDatePicker()
        initautoCompleteAdapter()
    }

    private fun addListener() {
        initBackButtonClickListener()
        initPlantSpeciesListener()
        datePickerListener()
        openGallery()
        autoCompletePlantSpeciesListener()
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

    private fun initPlantSpeciesListener() {
        binding.tvAddplantSpecies.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val currLength = binding.tvAddplantSpecies.length()
                binding.counterText.text = "$currLength/14"
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun initautoCompleteAdapter() {
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