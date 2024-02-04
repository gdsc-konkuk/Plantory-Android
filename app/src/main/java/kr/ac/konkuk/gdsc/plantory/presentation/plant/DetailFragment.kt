package kr.ac.konkuk.gdsc.plantory.presentation.plant

import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Bundle
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
import kr.ac.konkuk.gdsc.plantory.presentation.plant.diary.DiaryFragment
import kr.ac.konkuk.gdsc.plantory.presentation.plant.diary.UploadFragment
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment
import kr.ac.konkuk.gdsc.plantory.util.fragment.viewLifeCycle
import kr.ac.konkuk.gdsc.plantory.util.fragment.viewLifeCycleScope
import kr.ac.konkuk.gdsc.plantory.util.view.UiState
import kr.ac.konkuk.gdsc.plantory.util.view.setOnSingleClickListener
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar

@AndroidEntryPoint
class DetailFragment : BindingFragment<FragmentDetailBinding>(R.layout.fragment_detail) {

    private val viewModel: DetailViewModel by viewModels()
    private lateinit var detailAdapter: DetailAdapter
    private var firstCallCalendar = true
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firstCallCalendar = true

        initPlantInfo()
        getPlantHistoryStateObserver()
        getPlantDetailStateObserver()
        postPlantWateredStateObserver()
        addListener()

        viewModel.getPlantHistories()
    }

    private fun addListener() {
        updatePreviousMonth()
        updateNextMonth()
        initBackButton()
        initUploadButton()
        updateWaterButton()
    }

    private fun initPlantInfo() {
        val plantId = arguments?.getInt("plantId", -1) ?: -1
        if (plantId != -1) {
            viewModel.updateClickedPlantId(plantId)
        }
    }

    private fun initUploadButton() {
        binding.ivDetailPlantUpload.setOnSingleClickListener {
            navigateToWithBundle<UploadFragment>(bundleOf("plantId" to viewModel.clickedPlantId.value,
                "plantNickname" to viewModel.clickedPlantNickname.value))
        }
    }

    private fun initWaterButton(plantHistories: List<PlantHistory>) {
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
        binding.ivDetailPlantGiveWater.setOnSingleClickListener {
            val isWatered = viewModel.isWatered.value
            if (!isWatered) {
                viewModel.postPlantWatered()
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
                    binding.rvDetailCalendar.layoutManager =
                        GridLayoutManager(context, Calendar.DAY_OF_WEEK)
                    detailAdapter = DetailAdapter(currMonth, plantHistories, onDateClick = { date ->
                        navigateToWithBundle<DiaryFragment>(bundleOf("selectedDate" to date))
                    })
                    binding.rvDetailCalendar.adapter = detailAdapter

                    binding.tvDetailCalendarTitle.text = "${currYear}년 ${currMonth + 1}월"
                    detailAdapter.submitList(dayList)

                    firstCallCalendar = false
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

    /*getPlantById*/
    private fun getPlantDetailStateObserver() {
        viewModel.getPlantDetailState.flowWithLifecycle(viewLifeCycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    binding.data = initDday(state.data)
                    viewModel.updateClickedPlantNickname(state.data.nickname)
                }
                is UiState.Failure -> Timber.d("Failure : ${state.msg}")
                is UiState.Empty -> Unit
                is UiState.Loading -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun initDday(plant: Plant) : Plant{
        val targetDate = LocalDate.parse(plant.birthDate, DateTimeFormatter.ISO_DATE)
        val daysPassed = ChronoUnit.DAYS.between(targetDate, LocalDate.now())+1
        return plant.copy(dDay = daysPassed.toInt())
    }

    /*getPlantHistory*/
    private fun getPlantHistoryStateObserver() {
        viewModel.getPlantHistoryState.flowWithLifecycle(viewLifeCycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    if (state.data.isEmpty() && !firstCallCalendar) {
                    }else {
                        initWaterButton(state.data)
                        updateCalendar(state.data)
                    }
                }
                is UiState.Failure -> Timber.d("Failure : ${state.msg}")
                is UiState.Empty -> Unit
                is UiState.Loading -> Unit
            }

        }.launchIn(viewLifeCycleScope)
    }

    /*postPlantWatered*/
    private fun postPlantWateredStateObserver() {
        viewModel.postPlantWateredState.flowWithLifecycle(viewLifeCycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    binding.ivDetailPlantGiveWater.setImageResource(R.drawable.ic_detail_is_watered)
                    viewModel.updateIsWatered(true)
                    viewModel.getPlantHistories()
                }
                is UiState.Failure -> Timber.d("Failure : ${state.msg}")
                is UiState.Empty -> Unit
                is UiState.Loading -> Unit
            }

        }.launchIn(viewLifeCycleScope)
    }

    private inline fun <reified T : Fragment> navigateToWithBundle(args: Bundle) {
        parentFragmentManager.commit {
            replace<T>(
                R.id.fcv_main,
                T::class.simpleName,
                args
            ).addToBackStack("DiaryFragment")
        }
    }
}