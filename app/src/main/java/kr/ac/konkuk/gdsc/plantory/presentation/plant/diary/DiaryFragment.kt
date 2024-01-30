package kr.ac.konkuk.gdsc.plantory.presentation.plant.diary

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kr.ac.konkuk.gdsc.plantory.R
import kr.ac.konkuk.gdsc.plantory.databinding.FragmentDiaryBinding
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment
import kr.ac.konkuk.gdsc.plantory.util.fragment.snackBar
import kr.ac.konkuk.gdsc.plantory.util.fragment.viewLifeCycle
import kr.ac.konkuk.gdsc.plantory.util.fragment.viewLifeCycleScope
import kr.ac.konkuk.gdsc.plantory.util.view.UiState
import kr.ac.konkuk.gdsc.plantory.util.view.setOnSingleClickListener
import java.util.Date

@AndroidEntryPoint
class DiaryFragment : BindingFragment<FragmentDiaryBinding>(R.layout.fragment_diary) {
    private val viewModel by viewModels<DiaryViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val selectedDate = arguments?.getSerializable("selectedDate") as Date
        initBackButton()
        setGetPantDailyRecordStateObserver()
    }

    private fun setGetPantDailyRecordStateObserver() {
        viewModel.getPlantDailyRecordState.flowWithLifecycle(viewLifeCycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    binding.data = state.data
                }

                is UiState.Failure -> {
                    snackBar(binding.root) { state.msg }
                }

                is UiState.Empty -> {
                }

                is UiState.Loading -> {
                }
            }

        }.launchIn(viewLifeCycleScope)
    }

    private fun initBackButton() {
        binding.ivDiaryBack.setOnSingleClickListener {
            navigateToHome()
        }
    }

    private fun navigateToHome() {
        parentFragmentManager.popBackStack()
    }
}