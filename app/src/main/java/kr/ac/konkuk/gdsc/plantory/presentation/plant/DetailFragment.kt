package kr.ac.konkuk.gdsc.plantory.presentation.plant

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kr.ac.konkuk.gdsc.plantory.R
import kr.ac.konkuk.gdsc.plantory.databinding.FragmentDetailBinding
import kr.ac.konkuk.gdsc.plantory.presentation.home.HomeFragment
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment
import kr.ac.konkuk.gdsc.plantory.util.view.setOnSingleClickListener
import java.util.Calendar
import java.util.Date

@AndroidEntryPoint
class DetailFragment : BindingFragment<FragmentDetailBinding>(R.layout.fragment_detail) {

    private val viewModel: DetailViewModel by viewModels()
    private lateinit var detailAdapter: DetailAdapter
    private var calendar = Calendar.getInstance()
    private var currYear: Int = Calendar.YEAR
    private var currMonth: Int = Calendar.MONTH

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
    }

    private fun initPlantInfo() {
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
        if (isPrevious) {
            currMonth--
            if (currMonth < 0) {
                currMonth = Calendar.DECEMBER
                currYear--
            }
        } else {
            currMonth++
            if (currMonth > 11) {
                currMonth = Calendar.JANUARY
                currYear++
            }
        }
        updateCalendar()
    }

    private fun updateCalendar() {
        //달 구하기
        calendar.set(Calendar.YEAR, currYear)
        calendar.set(Calendar.MONTH, currMonth)
        calendar.set(Calendar.DAY_OF_MONTH, 1) //스크롤시 현재 월의 1일로 이동

        binding.tvDetailCalendarTitle.text = "${currYear}년 ${currMonth + 1}월"
        var dayList: MutableList<Date> = mutableListOf()

        for (i in 0..4) { //주
            for (k in 0..6) { //요일
                //요일 표시
                calendar.add(Calendar.DAY_OF_MONTH, (1 - calendar.get(Calendar.DAY_OF_WEEK)) + k)
                dayList.add(calendar.time.clone() as Date) //배열 인덱스 만큼 요일 데이터 저장
            }
            //주 표시
            calendar.add(Calendar.WEEK_OF_MONTH, 1)
        }

        binding.recyclerCalenderView.layoutManager = GridLayoutManager(context, 7)
        val dummyInfo = viewModel.plantRecord
        detailAdapter = DetailAdapter(currMonth, dayList, dummyInfo)
        binding.recyclerCalenderView.adapter = detailAdapter

        detailAdapter.submitList(dayList)
    }

    private fun initCalendar() {
        calendar.time = Date()
        currMonth = calendar.get(Calendar.MONTH)
        currYear = calendar.get(Calendar.YEAR)
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
}