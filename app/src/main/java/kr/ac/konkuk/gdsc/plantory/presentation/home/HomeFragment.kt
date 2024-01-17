package kr.ac.konkuk.gdsc.plantory.presentation.home

import PopupMenu
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kr.ac.konkuk.gdsc.plantory.R
import kr.ac.konkuk.gdsc.plantory.databinding.FragmentHomeBinding
import kr.ac.konkuk.gdsc.plantory.domain.entity.Plant
import kr.ac.konkuk.gdsc.plantory.presentation.plant.AddPlantFragment
import kr.ac.konkuk.gdsc.plantory.presentation.plant.diary.DiaryFragment
import kr.ac.konkuk.gdsc.plantory.presentation.plant.diary.UploadFragment
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment
import kr.ac.konkuk.gdsc.plantory.util.decoration.ViewPagerDecoration
import kr.ac.konkuk.gdsc.plantory.util.fragment.viewLifeCycleScope
import kr.ac.konkuk.gdsc.plantory.util.view.setOnSingleClickListener

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
        addListener()
        //TODO: 지우기
        binding.ivHomeLogo.setOnClickListener {
            navigateTo<DiaryFragment>()
        }
    }

    private fun initMockData() {
        initPlantViewPager(viewModel.plantList)
    }

    private fun initPlantViewPager(plants: List<Plant>) {
        createPlantScrollJob()
        initPlantViewPagerAdapter(plants = plants)
        initPlantViewPagerIndicator(plants = plants)
        initViewPagerDecoration(
            previewWidth = VIEWPAGER_PREVIEW_WIDTH, itemMargin = VIEWPAGER_ITEM_MARGIN
        )
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

    private fun initViewPagerDecoration(previewWidth: Int, itemMargin: Int) {
        val decoMargin = previewWidth + itemMargin
        val pageTransX = decoMargin + previewWidth
        val decoration = ViewPagerDecoration(decoMargin)

        binding.vpHomePlant.also {
            it.offscreenPageLimit = 1
            it.addItemDecoration(decoration)
            it.setPageTransformer { page, position ->
                page.translationX = position * -pageTransX
            }
        }
    }

    private fun addCallback() {
        registerPlantPageChangeCallback()
    }

    private fun addListener() {
        initAddButtonClickListener()
        initNotificationButtonClickListener()
    }

    private fun initAddButtonClickListener() {
        binding.ivHomeAdd.setOnSingleClickListener {
            PopupMenu(it.context, onAddButtonClick = {
                navigateToAdd()
            }, onUploadButtonClick = {
                navigateToUpload()
            }).showAsDropDown(it, -55, 0)
        }
    }


    private fun initNotificationButtonClickListener() {
        binding.ivHomeNotification.setOnClickListener {
            navigateToNotification()
        }
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

    private fun navigateToNotification() {
        navigateTo<NotificationFragment>()
    }

    private fun navigateToUpload() {
        navigateTo<UploadFragment>()
    }

    private fun navigateToAdd() {
        navigateTo<AddPlantFragment>()
    }

    private inline fun <reified T : Fragment> navigateTo() {
        activity?.supportFragmentManager?.commit {
            replace<T>(R.id.fcv_main, T::class.simpleName).addToBackStack(ROOT_FRAGMENT_HOME)
        }
    }

    companion object {
        private const val SCROLL_DELAY_TIME = 5000L
        private const val VIEWPAGER_PREVIEW_WIDTH = 60
        private const val VIEWPAGER_ITEM_MARGIN = 50

        private const val ROOT_FRAGMENT_HOME = "homeFragment"
    }
}