package kr.ac.konkuk.gdsc.plantory.presentation.plant

import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Bundle
import android.view.View
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kr.ac.konkuk.gdsc.plantory.R
import kr.ac.konkuk.gdsc.plantory.databinding.FragmentDetailBinding
import kr.ac.konkuk.gdsc.plantory.presentation.plant.diary.UploadFragment
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment
import kr.ac.konkuk.gdsc.plantory.util.view.setOnSingleClickListener
import java.util.Calendar
import java.util.Date

@AndroidEntryPoint
class DetailFragment : BindingFragment<FragmentDetailBinding>(R.layout.fragment_detail) {

    private val viewModel: DetailViewModel by viewModels()
    private lateinit var detailAdapter: DetailAdapter
    private var calendar = Calendar.getInstance()

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
        uploadPreviousMonth()
        uploadNextMonth()
        initBackBtn()
        initUploadBtn()
    }

    private fun initUploadBtn() {
        binding.ivDetailPlantUpload.setOnSingleClickListener {
            parentFragmentManager.commit {
                replace<UploadFragment>(
                    R.id.fcv_main,
                    T::class.simpleName
                ).addToBackStack("DetailToUpload")
            }
        }
    }

    private fun uploadPreviousMonth() {
        binding.ivDetailCalendarLeft.setOnSingleClickListener {
            uploadPreviousNextMonth(true)
        }
    }

    private fun uploadNextMonth() {
        binding.ivDetailCalendarRight.setOnSingleClickListener {
            uploadPreviousNextMonth(false)
        }
    }

    private fun uploadPreviousNextMonth(isPrevious: Boolean) {
        viewModel.updatePreviousNextMonthYear(isPrevious)
        updateCalendar()
    }

    private fun updateCalendar() {
        lifecycleScope.launch {
            viewModel.currentYear.collectLatest { currYear ->
                viewModel.currentMonth.collectLatest { currMonth ->
                    calendar.set(Calendar.YEAR, currYear)
                    calendar.set(Calendar.MONTH, currMonth)
                    calendar.set(Calendar.DAY_OF_MONTH, FIRST_DAY)

                    val dayList: MutableList<Date> = mutableListOf()

                    for (i in 0..Calendar.WEEK_OF_MONTH) {
                        for (k in 0..Calendar.DAY_OF_YEAR) {
                            calendar.add(Calendar.DAY_OF_MONTH, (FIRST_DAY - calendar.get(Calendar.DAY_OF_WEEK)) + k)
                            dayList.add(calendar.time.clone() as Date)
                        }
                        calendar.add(Calendar.WEEK_OF_MONTH, FIRST_DAY)
                    }

                    binding.rvDetailCalendar.layoutManager = GridLayoutManager(context, Calendar.DAY_OF_WEEK)
                    val dummyInfo = viewModel.plantRecord
                    detailAdapter = DetailAdapter(currMonth, dayList, dummyInfo)
                    binding.rvDetailCalendar.adapter = detailAdapter

                    binding.tvDetailCalendarTitle.text = "${currYear}년 ${currMonth + 1}월"
                    detailAdapter.submitList(dayList)
                }
            }
        }
    }

    private fun initCalendar() {
        updateCalendar()
    }

    private fun initBackBtn() {
        binding.ivDetailBack.setOnSingleClickListener {
            navigateToHome()
        }
    }

    private fun navigateToHome() {
        parentFragmentManager.popBackStack()
    }

    companion object {
        private const val FIRST_DAY = 1
    }
}