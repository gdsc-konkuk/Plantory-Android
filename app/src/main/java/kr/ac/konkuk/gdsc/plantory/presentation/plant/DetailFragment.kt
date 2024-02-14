package kr.ac.konkuk.gdsc.plantory.presentation.plant

import PopupMenu
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import coil.transform.RoundedCornersTransformation
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
import kr.ac.konkuk.gdsc.plantory.presentation.home.HomeFragment
import kr.ac.konkuk.gdsc.plantory.presentation.plant.diary.DiaryFragment
import kr.ac.konkuk.gdsc.plantory.presentation.plant.diary.UploadFragment
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment
import kr.ac.konkuk.gdsc.plantory.util.fragment.snackBar
import kr.ac.konkuk.gdsc.plantory.util.fragment.viewLifeCycle
import kr.ac.konkuk.gdsc.plantory.util.fragment.viewLifeCycleScope
import kr.ac.konkuk.gdsc.plantory.util.view.PopupDeleteMenu
import kr.ac.konkuk.gdsc.plantory.util.view.UiState
import kr.ac.konkuk.gdsc.plantory.util.view.setOnSingleClickListener
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Locale

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
        deletePlantObserver()
        addListener()

        viewModel.getPlantHistories()
    }

    private fun addListener() {
        updatePreviousMonth()
        updateNextMonth()
        updateWaterButton()
        initBackButton()
        initUploadButton()
        initAddButtonClickListener()
        initMenuButtonClickListener()
    }

    private fun initPlantInfo() {
        val plantId = arguments?.getInt("plantId", -1) ?: -1
        if (plantId != -1) {
            viewModel.updateClickedPlantId(plantId)
        }
    }

    private fun initUploadButton() {
        binding.ivDetailPlantUpload.setOnSingleClickListener {
            if (viewModel.isRecoreded.value) {
                snackBar(requireView()) { "이미 기록이 있습니다." }
            } else {
                navigateToWithBundle<UploadFragment>(
                    bundleOf(
                        "plantId" to viewModel.clickedPlantId.value,
                        "plantNickname" to viewModel.clickedPlant.value.nickname
                    )
                )
            }
        }
    }

    private fun initImage() {
        viewModel.clickedPlant.value.let { plant ->
            binding.ivDetailImg.load(plant.imageUrl) {
                transformations(RoundedCornersTransformation(20f))
            }
        }
    }

    private fun initWaterButton(plantHistories: List<PlantHistory>) {
        plantHistories.forEach { plantHistory ->
            if (plantHistory.date.takeLast(2).toInt() == viewModel.currentDay.value) {
                if (plantHistory.type == PlantHistoryType.WATER_CHANGE) {
                    binding.ivDetailPlantGiveWater.setImageResource(R.drawable.ic_detail_is_watered)
                    viewModel.updateIsWatered(true)
                } else if (plantHistory.type == PlantHistoryType.RECORDING) {
                    binding.ivDetailPlantUpload.setImageResource(R.drawable.ic_detail_record_complete)
                    viewModel.updateIsRecorded(true)
                }
            }
        }
    }

    private fun initAddButtonClickListener() {
        binding.ivDetailAdd.setOnSingleClickListener {
            PopupMenu(it.context, onAddButtonClick = {
                navigateToAdd()
            }).showAsDropDown(it, -55, 0)
        }
    }

    private fun initMenuButtonClickListener() {
        binding.ivDetailMenu.setOnSingleClickListener {
            PopupDeleteMenu(it.context, onMenuButtonClick = {
                viewModel.deletePlant()
            }).showAsDropDown(it, -55, 0)
        }
    }

    private fun updateWaterButton() {
        binding.ivDetailPlantGiveWater.setOnSingleClickListener {
            val isWatered = viewModel.isWatered.value
            if (!isWatered) {
                viewModel.postPlantWatered()
            } else {
                snackBar(requireView()) { "이미 물주기를 완료했습니다." }
            }
        }
    }

    private fun updatePreviousMonth() {
        binding.ivDetailCalendarLeft.setOnSingleClickListener {
            updatePreviousNextMonth(true)
        }ㄴㅇ
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
                    detailAdapter = DetailAdapter(
                        currMonth = currMonth,
                        plantHistories = plantHistories,
                        onDateClick = { date ->
                            navigateToWithBundle<DiaryFragment>(
                                bundleOf(
                                    "selectedDate" to SimpleDateFormat(
                                        "yyyy-MM-dd",
                                        Locale.getDefault()
                                    ).format(date),
                                    "plantId" to viewModel.clickedPlantId.value
                                )
                            )
                        },
                        onEmptyDateClick = {
                            snackBar(binding.root) { getString(R.string.detail_empty_record_message) }
                        }
                    )
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

    private fun navigateToAdd() {
        navigateTo<AddPlantFragment>()
    }

    /*getPlantById*/
    private fun getPlantDetailStateObserver() {
        viewModel.getPlantDetailState.flowWithLifecycle(viewLifeCycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    binding.data = initDday(state.data)
                    viewModel.updateClickedPlant(state.data)
                    initImage()
                }
                is UiState.Failure -> Timber.e("Failure : ${state.msg}")
                is UiState.Empty -> Unit
                is UiState.Loading -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun initDday(plant: Plant): Plant {
        val targetDate = LocalDate.parse(plant.birthDate, DateTimeFormatter.ISO_DATE)
        val daysPassed = ChronoUnit.DAYS.between(targetDate, LocalDate.now()) + 1
        return plant.copy(dDay = daysPassed.toInt())
    }

    /*getPlantHistory*/
    private fun getPlantHistoryStateObserver() {
        viewModel.getPlantHistoryState.flowWithLifecycle(viewLifeCycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    if (state.data.isEmpty() && !firstCallCalendar) {
                    } else {
                        deactivateLoadingProgressBar()
                        initWaterButton(state.data)
                        updateCalendar(state.data)
                    }
                }

                is UiState.Failure -> Timber.e("Failure : ${state.msg}")
                is UiState.Empty -> Unit
                is UiState.Loading -> {
                    activateLoadingProgressBar()
                }
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

                is UiState.Failure -> Timber.e("Failure : ${state.msg}")
                is UiState.Empty -> Unit
                is UiState.Loading -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    /*deletePlant*/
    private fun deletePlantObserver() {
        viewModel.deletePlantState.flowWithLifecycle(viewLifeCycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    navigateToHomeWithMessage(KEY_FROM_DETAIL_DELETE, MSG_FROM_DETAIL_DELETE)
                }
                is UiState.Failure -> Timber.e("Failure : ${state.msg}")
                is UiState.Empty -> Unit
                is UiState.Loading -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }

    private fun activateLoadingProgressBar() {
        binding.clDetail.isVisible = false
        binding.pbDetailLoading.isVisible = true
    }

    private fun deactivateLoadingProgressBar() {
        binding.clDetail.isVisible = true
        binding.pbDetailLoading.isVisible = false
    }

    private fun navigateToHomeWithMessage(key: String, message: String) {
        val fragment = HomeFragment().apply {
            arguments = Bundle().apply {
                putString(key, message)
            }
        }

        activity?.supportFragmentManager?.commit {
            replace(R.id.fcv_main, fragment)
        }
    }

    private inline fun <reified T : Fragment> navigateTo() {
        activity?.supportFragmentManager?.commit {
            replace<T>(R.id.fcv_main, T::class.simpleName).addToBackStack("DetailFragment")
        }
    }

    private inline fun <reified T : Fragment> navigateToWithBundle(args: Bundle) {
        parentFragmentManager.commit {
            replace<T>(
                R.id.fcv_main,
                T::class.simpleName,
                args
            ).addToBackStack("DetailFragment")
        }
    }

    companion object {
        private const val KEY_FROM_DETAIL_DELETE = "KEY_FROM_DETAIL_DELETE"
        private const val MSG_FROM_DETAIL_DELETE = "식물을 성공적으로 삭제했습니다"
    }
}
