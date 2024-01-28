package kr.ac.konkuk.gdsc.plantory.presentation.plant.diary

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kr.ac.konkuk.gdsc.plantory.R
import kr.ac.konkuk.gdsc.plantory.databinding.FragmentDiaryBinding
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment
import kr.ac.konkuk.gdsc.plantory.util.view.setOnSingleClickListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class DiaryFragment : BindingFragment<FragmentDiaryBinding>(R.layout.fragment_diary) {
    private val viewModel by viewModels<DiaryViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //달력 클릭시 날짜 전달
        val selectedDate = arguments?.getSerializable("selectedDate") as Date
        initMockData()
        initBackButton()
    }

    private fun initMockData() {
        binding.data = viewModel.plantRecord
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