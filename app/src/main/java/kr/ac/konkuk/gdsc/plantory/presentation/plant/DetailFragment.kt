package kr.ac.konkuk.gdsc.plantory.presentation.plant

import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kr.ac.konkuk.gdsc.plantory.R
import kr.ac.konkuk.gdsc.plantory.databinding.FragmentDetailBinding
import kr.ac.konkuk.gdsc.plantory.domain.entity.Plant
import kr.ac.konkuk.gdsc.plantory.domain.entity.PlantHistory
import kr.ac.konkuk.gdsc.plantory.domain.entity.PlantHistoryType
import kr.ac.konkuk.gdsc.plantory.presentation.home.HomeFragment
import kr.ac.konkuk.gdsc.plantory.presentation.plant.diary.DiaryFragment
import kr.ac.konkuk.gdsc.plantory.presentation.plant.diary.UploadFragment
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment
import kr.ac.konkuk.gdsc.plantory.util.fragment.viewLifeCycle
import kr.ac.konkuk.gdsc.plantory.util.fragment.viewLifeCycleScope
import kr.ac.konkuk.gdsc.plantory.util.view.UiState
import kr.ac.konkuk.gdsc.plantory.util.view.setOnSingleClickListener
import timber.log.Timber
import java.util.Calendar
import java.util.Date

@AndroidEntryPoint
class DetailFragment : BindingFragment<FragmentDetailBinding>(R.layout.fragment_detail) {

    private val viewModel: DetailViewModel by viewModels()
    private lateinit var detailAdapter: DetailAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.data = viewModel.generatePlantMockData()
        addListener()
        getPlantHistoryStateObserver()
        postPlantWateredStateObserver()
    }


    private fun addListener() {
        updatePreviousMonth()
        updateNextMonth()
        initBackButton()
        initUploadButton()
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

    private fun initWaterButton(plantHistories: List<PlantHistory>) {
        //오늘 물 줬는지 시작때 파악
        plantHistories.forEach { plantHistory ->
            if (plantHistory.date.takeLast(2).toInt() == viewModel.currentDay.value) {
                if (plantHistory.type == PlantHistoryType.WATER_CHANGE) {
                    binding.ivDetailPlantGiveWater.setImageResource(R.drawable.ic_detail_is_watered)
                    viewModel.updateIsWatered(true)
                }
            }
        }
    }

    private fun updateWaterButton() {
        //한번 물 주면 취소 불가
        binding.ivDetailPlantGiveWater.setOnSingleClickListener {
            viewLifeCycleScope.launch {
                viewModel.isWatered.collectLatest { isWatered ->
                    if (!isWatered) {
                        viewModel.postPlantWatered()
                        viewModel.getPlantHistories()
                        binding.ivDetailPlantGiveWater.setImageResource(R.drawable.ic_detail_is_watered)
                        viewModel.updateIsWatered(true)
                    }
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
        viewModel.getPlantHistories()
    }

    private fun updateCalendar(plantHistories: List<PlantHistory>) {
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
                    detailAdapter = DetailAdapter(currMonth, plantHistories, onDateClick = { date ->
                        navigateTo<DiaryFragment>(bundleOf("selectedDate" to date))
                    })
                    binding.rvDetailCalendar.adapter = detailAdapter

                    binding.tvDetailCalendarTitle.text = "${currYear}년 ${currMonth + 1}월"
                    detailAdapter.submitList(dayList)
                }
        }
    }

    private fun initBackButton() {
        binding.ivDetailBack.setOnSingleClickListener {
            navigateToHome()
        }
    }

    private fun navigateToHome() {
        parentFragmentManager.popBackStack()
    }

    /*getPlantHistory*/
    private fun getPlantHistoryStateObserver() {
        viewModel.getPlantHistoryState.flowWithLifecycle(viewLifeCycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    Timber.d("Success : Register ")
                    initWaterButton(state.data)
                    updateCalendar(state.data)
                }

                is UiState.Failure -> {
                    Timber.d("Failure : ${state.msg}")
                }

                is UiState.Empty -> {
                }

                is UiState.Loading -> {
                }
            }

        }.launchIn(viewLifeCycleScope)
    }

    /*postPlantWatered*/
    private fun postPlantWateredStateObserver() {
        viewModel.postPlantWateredState.flowWithLifecycle(viewLifeCycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    Timber.d("Success : watered Register ")
                }

                is UiState.Failure -> {
                    Timber.d("Failure : ${state.msg}")
                }

                is UiState.Empty -> {
                }

                is UiState.Loading -> {
                }
            }

        }.launchIn(viewLifeCycleScope)
    }

    private inline fun <reified T : Fragment> navigateTo(args: Bundle) {
        parentFragmentManager.commit {
            replace<T>(
                R.id.fcv_main,
                T::class.simpleName,
                args
            ).addToBackStack("DiaryFragment")
        }
    }
}