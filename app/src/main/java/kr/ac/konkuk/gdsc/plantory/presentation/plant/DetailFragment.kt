package kr.ac.konkuk.gdsc.plantory.presentation.plant

import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kr.ac.konkuk.gdsc.plantory.R
import kr.ac.konkuk.gdsc.plantory.databinding.FragmentDetailBinding
import kr.ac.konkuk.gdsc.plantory.presentation.plant.diary.DiaryFragment
import kr.ac.konkuk.gdsc.plantory.presentation.plant.diary.UploadFragment
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment
import kr.ac.konkuk.gdsc.plantory.util.fragment.viewLifeCycleScope
import kr.ac.konkuk.gdsc.plantory.util.view.setOnSingleClickListener
import java.util.Calendar

@AndroidEntryPoint
class DetailFragment : BindingFragment<FragmentDetailBinding>(R.layout.fragment_detail) {

    private val viewModel: DetailViewModel by viewModels()
    private lateinit var detailAdapter: DetailAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.data = viewModel.plantInfo
        initView()
        addListener()
    }

    private fun initView() {
        initCalendar()
    }

    private fun addListener() {
        updatePreviousMonth()
        updateNextMonth()
        initBackButton()
        initUploadButton()
        initWaterButton()
        updateWaterButton()
    }

    private fun initUploadButton() {
        binding.ivDetailPlantUpload.setOnSingleClickListener {
            parentFragmentManager.commit {
                replace<UploadFragment>(
                    R.id.fcv_main,
                    UploadFragment::class.simpleName
                ).addToBackStack("DetailToUpload")
            }
        }
    }

    private fun initWaterButton() {
        binding.ivDetailPlantGiveWater.setOnSingleClickListener {
            viewModel.updateIsWatered()
        }
    }

    private fun updateWaterButton() {
        viewLifeCycleScope.launch {
            viewModel.isWatered.collectLatest { isWatered ->
                if (isWatered) {
                    binding.ivDetailPlantGiveWater.setImageResource(R.drawable.ic_detail_is_watered)
                } else {
                    binding.ivDetailPlantGiveWater.setImageResource(R.drawable.ic_detail_not_watered)
                }
            }
        }
    }

    private fun updatePreviousMonth() {
        binding.ivDetailCalendarLeft.setOnSingleClickListener {
            updatePreviousNextMonth(true)
        }
    }

    private fun updateNextMonth() {
        binding.ivDetailCalendarRight.setOnSingleClickListener {
            updatePreviousNextMonth(false)
        }
    }

    private fun updatePreviousNextMonth(isPrevious: Boolean) {
        viewModel.updateMonthAndYear(isPrevious)
        updateCalendar()
    }

    private fun updateCalendar() {
        viewLifeCycleScope.launch {
            viewModel.currentYear.combine(viewModel.currentMonth) { year, month ->
                Pair(year, month)
            }
                .stateIn(lifecycleScope)
                .collect { (currYear, currMonth) ->

                    val dayList = viewModel.updateCalendarDayList(currYear, currMonth)
                    val dummyInfo = viewModel.plantRecord

                    binding.rvDetailCalendar.layoutManager =
                        GridLayoutManager(context, Calendar.DAY_OF_WEEK)
                    detailAdapter = DetailAdapter(currMonth, dummyInfo, onDateClick = { date ->
                        navigateTo<DiaryFragment>(bundleOf("selectedDate" to date))
                    })
                    binding.rvDetailCalendar.adapter = detailAdapter

                    binding.tvDetailCalendarTitle.text = "${currYear}년 ${currMonth + 1}월"
                    detailAdapter.submitList(dayList)
                }
        }
    }

    private fun initCalendar() {
        updateCalendar()
    }

    private fun initBackButton() {
        binding.ivDetailBack.setOnSingleClickListener {
            navigateToHome()
        }
    }

    private fun navigateToHome() {
        parentFragmentManager.popBackStack()
    }

    private inline fun <reified T : Fragment> navigateTo(args: Bundle) {
        parentFragmentManager.commit {
            replace<T>(
                R.id.fcv_main,
                T::class.simpleName,
                args
            ).addToBackStack("DiaryFragement")
        }
    }
}
