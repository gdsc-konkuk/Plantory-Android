package kr.ac.konkuk.gdsc.plantory.presentation.plant

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kr.ac.konkuk.gdsc.plantory.R
import kr.ac.konkuk.gdsc.plantory.databinding.FragmentAddPlantBinding
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment
import java.util.Calendar

@AndroidEntryPoint
class AddPlantFragment : BindingFragment<FragmentAddPlantBinding>(R.layout.fragment_add_plant) {

    private var selectedImageUri: Uri? = null
    private lateinit var viewModel: AddPlantViewModel
    private lateinit var adapter: ArrayAdapter<String>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel = AddPlantViewModel()
        openGallery()
        initDatePicker()
        initPlantSpeciesWordNum()
        autoCompletePlantSpecies()
    }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                selectedImageUri = uri
                Glide.with(this)
                    .load(uri)
                    .centerCrop()
                    .into(binding.ivAddplantProfile)
            }
        }

    private fun openGallery() {
        binding.ivAddplantProfile.setOnClickListener {
            getContent.launch("image/*")
        }
    }

    private fun initDatePicker() {
        var calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        binding.etAddplantBirthday.setText("${year}/${month + 1}/${day}")
        binding.ivAddplantDatepicker.setOnClickListener {
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

    private fun initPlantSpeciesWordNum() {
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

    private fun autoCompletePlantSpecies() {
        adapter =
            ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                viewModel.plantSpeciesList
            )

        binding.tvAddplantSpecies.setAdapter(adapter)

        binding.tvAddplantSpecies.setOnItemClickListener { parent, view, position, id ->
            val item = parent.getItemAtPosition(position).toString()
            binding.tvAddplantSpecies.setText(item)
        }
    }
}