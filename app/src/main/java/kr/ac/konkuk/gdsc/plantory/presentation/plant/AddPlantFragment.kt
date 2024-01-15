package kr.ac.konkuk.gdsc.plantory.presentation.plant

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kr.ac.konkuk.gdsc.plantory.R
import kr.ac.konkuk.gdsc.plantory.databinding.FragmentAddPlantBinding
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment
import java.util.Calendar

@AndroidEntryPoint
class AddPlantFragment: BindingFragment<FragmentAddPlantBinding>(R.layout.fragment_add_plant) {

    private var selectedImageUri: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        openGallery()
        initDatePicker()
    }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                selectedImageUri = uri
                Glide.with(this)
                    .load(uri)
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

}