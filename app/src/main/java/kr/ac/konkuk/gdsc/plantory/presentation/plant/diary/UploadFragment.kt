package kr.ac.konkuk.gdsc.plantory.presentation.plant.diary

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kr.ac.konkuk.gdsc.plantory.R
import kr.ac.konkuk.gdsc.plantory.databinding.FragmentUploadBinding
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment
import kr.ac.konkuk.gdsc.plantory.util.binding.setImageUrl
import kr.ac.konkuk.gdsc.plantory.util.binding.setRegisterBackgroundResource
import kr.ac.konkuk.gdsc.plantory.util.fragment.snackBar
import kr.ac.konkuk.gdsc.plantory.util.fragment.viewLifeCycle
import kr.ac.konkuk.gdsc.plantory.util.fragment.viewLifeCycleScope
import kr.ac.konkuk.gdsc.plantory.util.view.UiState
import kr.ac.konkuk.gdsc.plantory.util.view.setOnSingleClickListener

@AndroidEntryPoint
class UploadFragment : BindingFragment<FragmentUploadBinding>(R.layout.fragment_upload) {

    private val viewModel: UploadViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        initPlantInfo()
        addListener()
        setPostPlantRecordStateObserver()
    }

    private fun addListener() {
        initImageUriChangeListener()
        openGallery()
        initTextChangeListener()
        initBackListener()
        initRegisterButtonClickListener()
    }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                viewModel.updateProfileImage(uri)
            }
        }

    private fun initPlantInfo() {
        val clickedPlantId = arguments?.getInt("plantId", -1) ?: -1
        val clickedPlantNickname = arguments?.getString("plantNickname", "") ?: ""
        viewModel.updateClickedPlantId(clickedPlantId)
        viewModel.updateClickedPlantNickname(clickedPlantNickname)
    }

    private fun initImageUriChangeListener() {
        viewLifeCycleScope.launch {
            viewModel.imageUri.collectLatest { uri ->
                binding.ivEmptyView.setImageUrl(uri.toString())
            }
        }
    }

    private fun updateRegisterButtonState() {
        viewLifeCycleScope.launch {
            viewModel.plantRecord.collectLatest { input ->
                val isFieldNotEmpty = input.comment.isNotEmpty()
                binding.btnDiaryUpload.setRegisterBackgroundResource(isFieldNotEmpty)
            }
        }
    }

    private fun initRegisterButtonClickListener() {
        binding.btnDiaryUpload.setOnSingleClickListener {
            viewModel.postPlantRecord()
        }
    }

    private fun openGallery() {
        binding.ivUploadProfileImg.setOnSingleClickListener {
            getContent.launch("image/*")
        }
    }

    private fun initTextChangeListener() {
        binding.etUploadDiary.doAfterTextChanged { text ->
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

    private fun setPostPlantRecordStateObserver() {
        viewModel.postPlantRecordState.flowWithLifecycle(viewLifeCycle).onEach { state ->
            when(state) {
                is UiState.Success -> {
                    navigateToHome()
                }
                is UiState.Failure -> {
                    snackBar(requireView()) { "이미 기록이 등록되어 있습니다." }
                    navigateToHome()
                }
                is UiState.Empty -> Unit
                is UiState.Loading -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }
}
