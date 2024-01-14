package kr.ac.konkuk.gdsc.plantory.presentation.home

import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kr.ac.konkuk.gdsc.plantory.R
import kr.ac.konkuk.gdsc.plantory.databinding.FragmentHomeBinding
import kr.ac.konkuk.gdsc.plantory.domain.entity.Plant
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment
import kr.ac.konkuk.gdsc.plantory.util.fragment.viewLifeCycleScope

@AndroidEntryPoint
class HomeFragment : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var viewModel: HomeViewModel
    private lateinit var plantScrollJob: Job
    private var currentPlantPosition = 0
    private var plantItemCount = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel = HomeViewModel()
        initMockData()
        addCallback()
    }

    private fun initMockData() {
        initPlantViewPager(viewModel.plantList)
    }

    private fun initPlantViewPager(plants: List<Plant>) {
        createPlantScrollJob()
        initPlantViewPagerAdapter(plants = plants)
        initPlantViewPagerIndicator(plants = plants)
    }

    private fun initPlantViewPagerAdapter(plants: List<Plant>) {
        homeAdapter = HomeAdapter().apply {
            binding.vpHomePlant.adapter = this
            submitList(plants)
        }
    }

    private fun initPlantViewPagerIndicator(plants: List<Plant>) {
        binding.diHomePlant.apply {
            attachTo(binding.vpHomePlant)
            plantItemCount = plants.size
            addDot(plantItemCount)
        }
    }

    private fun addCallback() {
        registerPlantPageChangeCallback()
    }

    private fun registerPlantPageChangeCallback() {
        binding.vpHomePlant.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updatePlantPosition(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                setPlantScrollJobState(state)
            }
        })
    }

    private fun createPlantScrollJob() {
        plantScrollJob = viewLifeCycleScope.launch {
            delay(SCROLL_DELAY_TIME)
            binding.vpHomePlant.setCurrentItem(++currentPlantPosition, true)
        }
    }

    private fun updatePlantPosition(position: Int) {
        currentPlantPosition = position
    }

    private fun setPlantScrollJobState(state: Int) {
        when (state) {
            ViewPager2.SCROLL_STATE_IDLE -> {
                if (!plantScrollJob.isActive) createPlantScrollJob()
            }

            ViewPager2.SCROLL_STATE_DRAGGING -> {
                if (plantScrollJob.isActive) plantScrollJob.cancel()
            }

            ViewPager2.SCROLL_STATE_SETTLING -> {}
        }
    }

    companion object {
        private const val SCROLL_DELAY_TIME = 5000L
    }
}