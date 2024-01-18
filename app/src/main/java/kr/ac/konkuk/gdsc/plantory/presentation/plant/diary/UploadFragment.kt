package kr.ac.konkuk.gdsc.plantory.presentation.plant.diary

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kr.ac.konkuk.gdsc.plantory.R
import kr.ac.konkuk.gdsc.plantory.databinding.FragmentUploadBinding
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment
import kr.ac.konkuk.gdsc.plantory.util.binding.setCheckBoxImage
import kr.ac.konkuk.gdsc.plantory.util.binding.setImageUrl
import kr.ac.konkuk.gdsc.plantory.util.binding.setRegisterBackgroundResource
import kr.ac.konkuk.gdsc.plantory.util.view.setOnSingleClickListener

@AndroidEntryPoint

class UploadFragment : BindingFragment<FragmentUploadBinding>(R.layout.fragment_upload){

    private val viewModel: UploadViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addListener()
    }

    private fun addListener() {
        initRepotedClickListener()
        initWateredClickListener()
        initImageUriListener()
        openGallery()
        initTextChangeListener()
        initBackListener()
        updateWateredCheckedListener()
        updateRepotedCheckedListener()
    }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                viewModel.updateProfileImage(uri)
            }
        }

    private fun initImageUriListener() {
        lifecycleScope.launch {
            viewModel.imageUri.collectLatest { uri ->
                binding.ivEmptyView.setImageUrl(uri.toString())
            }
        }
    }

    private fun initRepotedClickListener() {
        binding.ivUploadRepoted.setOnSingleClickListener {
            viewModel.updateRepotedButtonState()
        }
    }

    private fun initWateredClickListener() {
        binding.ivUploadWatered.setOnSingleClickListener {
            viewModel.updateWateredButtonState()
        }
    }

    private fun updateWateredCheckedListener() {
        lifecycleScope.launch {
            viewModel.wateredState.collectLatest { isChecked ->
                binding.ivUploadWatered.setCheckBoxImage(isChecked)
            }
        }
    }

    private fun updateRepotedCheckedListener() {
        lifecycleScope.launch {
            viewModel.repotedState.collectLatest { isChecked ->
                binding.ivUploadRepoted.setCheckBoxImage(isChecked)
            }
        }
    }

    private fun updateRegisterButtonState() {
        lifecycleScope.launch {
            viewModel.diaryInput.collectLatest { input ->
                val isFieldNotEmpty = input.isNotEmpty()
                binding.btnDiaryUpload.setRegisterBackgroundResource(isFieldNotEmpty)
            }
        }
    }

    private fun openGallery() {
        binding.ivUploadProfileImg.setOnSingleClickListener {
            getContent.launch("image/*")
        }
    }

    private fun initTextChangeListener() {
        binding.etUploadDiary.doAfterTextChanged {text ->
            viewModel.updateDiaryInput(text.toString())
            updateRegisterButtonState()
        }
    }

    private fun initBackListener() {
        binding.ivUploadBack.setOnSingleClickListener {
            navigateToHome()
        }
    }

    private fun navigateToHome() {
        parentFragmentManager.popBackStack()
    }
}