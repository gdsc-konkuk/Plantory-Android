package kr.ac.konkuk.gdsc.plantory.presentation.plant.diary

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import coil.load
import coil.transform.RoundedCornersTransformation
import dagger.hilt.android.AndroidEntryPoint
import kr.ac.konkuk.gdsc.plantory.R
import kr.ac.konkuk.gdsc.plantory.databinding.FragmentAddPlantBinding
import kr.ac.konkuk.gdsc.plantory.databinding.FragmentUploadBinding
import kr.ac.konkuk.gdsc.plantory.presentation.plant.AddPlantViewModel
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment
import kr.ac.konkuk.gdsc.plantory.util.view.setOnSingleClickListener

@AndroidEntryPoint

class UploadFragment : BindingFragment<FragmentUploadBinding>(R.layout.fragment_upload){

    private var selectedImageUri: Uri? = null
    private val viewModel: UploadViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addListener()
    }

    private fun addListener() {
        initRepotedBtn()
        initWateredBtn()
        openGallery()
        initTextChangeListener()
        initBackBtn()
    }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                selectedImageUri = uri
                binding.ivEmptyView.load(selectedImageUri) {
                    transformations(RoundedCornersTransformation(radius = 14f))
                    crossfade(true)
                }
            }
        }

    private fun initTextChangeListener() {
        binding.apply {
            etUploadDiary.addTextChangedListener(textWatcher)

        }
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            updateRegisterBtnState()
        }

        override fun afterTextChanged(s: Editable?) {
        }
    }

    private fun initRepotedBtn() {
        binding.ivUploadRepoted.setOnSingleClickListener {
            updateCheckedBtn(binding.ivUploadRepoted)
        }
    }

    private fun initWateredBtn() {
        binding.ivUploadWatered.setOnSingleClickListener {
            updateCheckedBtn(binding.ivUploadWatered)
        }
    }

    private fun openGallery() {
        binding.ivUploadProfileImg.setOnSingleClickListener {
            getContent.launch("image/*")
        }
    }

    private fun updateRegisterBtnState() {
        val isFieldNotEmpty = binding.etUploadDiary.text?.isNotEmpty() == true
        binding.btnDiaryUpload.isEnabled = isFieldNotEmpty
        binding.btnDiaryUpload.setBackgroundResource(
            if (isFieldNotEmpty) R.drawable.shape_mint_fill_10
            else R.drawable.shape_gray_200_fill_10
        )
    }

    private fun updateCheckedBtn(view: ImageView) {
        val currentState = view.tag as? String
        if (currentState == "unchecked") {
            view.setImageResource(R.drawable.ic_diary_checked)
            view.tag = "checked"
        } else {
            view.setImageResource(R.drawable.ic_diary_unchecked)
            view.tag = "unchecked"
        }
    }

    private fun initBackBtn() {
        binding.ivUploadBack.setOnSingleClickListener {
            navigateToHome()
        }
    }

    private fun navigateToHome() {
        parentFragmentManager.popBackStack()
    }
}