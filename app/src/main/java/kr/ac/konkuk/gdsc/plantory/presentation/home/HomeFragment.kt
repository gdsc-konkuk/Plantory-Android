package kr.ac.konkuk.gdsc.plantory.presentation.home

import PopupMenu
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kr.ac.konkuk.gdsc.plantory.R
import kr.ac.konkuk.gdsc.plantory.databinding.FragmentHomeBinding
import kr.ac.konkuk.gdsc.plantory.domain.entity.Plant
import kr.ac.konkuk.gdsc.plantory.presentation.plant.AddPlantFragment
import kr.ac.konkuk.gdsc.plantory.presentation.plant.DetailFragment
import kr.ac.konkuk.gdsc.plantory.presentation.plant.diary.UploadFragment
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment
import kr.ac.konkuk.gdsc.plantory.util.decoration.ViewPagerDecoration
import kr.ac.konkuk.gdsc.plantory.util.fragment.snackBar
import kr.ac.konkuk.gdsc.plantory.util.fragment.viewLifeCycleScope
import kr.ac.konkuk.gdsc.plantory.util.view.UiState
import kr.ac.konkuk.gdsc.plantory.util.view.setOnSingleClickListener
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var plantScrollJob: Job
    private var isDecorationAdded: Boolean = false
    private var currentPlantPosition = 0
    private var plantItemCount = 0
    private val viewModel by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        setGetAllPlantsStateObserver()
        addCallback()
        addListener()
        viewModel.getAllPlants()

        getArgumentsAndShowAction(KEY_FROM_ADD, view)
        getArgumentsAndShowAction(KEY_FROM_DETAIL_DELETE, view)
    }

    private fun getArgumentsAndShowAction(key: String, view: View) {
        arguments?.getString(key)?.let { message ->
            snackBar(view) { message }
            arguments?.putString(key, null)
        }
    }

    private fun initPlantViewPager(plants: List<Plant>) {
        createPlantScrollJob()
        initViewPagerDecoration(
            previewWidth = VIEWPAGER_PREVIEW_WIDTH,
            itemMargin = VIEWPAGER_ITEM_MARGIN
        )
        initPlantViewPagerAdapter(plants = plants)
        initPlantViewPagerIndicator(plants = plants)
    }

    private fun initSwipeRefreshListener() {
        binding.lHomeRefresh.setOnRefreshListener {
            refreshAllPlants()
            binding.lHomeRefresh.isRefreshing = false
        }
    }

    private fun refreshAllPlants() {
        viewModel.getAllPlants()
    }

    private fun initPlantViewPagerAdapter(plants: List<Plant>) {
        homeAdapter = HomeAdapter(
            onItemClick = { plantId ->
                val bundle = bundleOf("plantId" to plantId)
                navigateToDetailWithBundle(bundle)
            },
            onAddPlantButtonClick = { navigateToAdd() },
            onUploadDiaryButtonClick = { plant ->
                val bundle = bundleOf(
                    "plantId" to plant.id,
                    "plantNickname" to plant.nickname
                )
                navigateToUploadWithBundle(bundle)
            }
        ).apply {
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
        if (!isDecorationAdded) {
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
            isDecorationAdded = true
        }
        return
    }

    private fun addCallback() {
        registerPlantPageChangeCallback()
    }

    private fun addListener() {
        initAddButtonClickListener()
        initSwipeRefreshListener()
    }

    private fun initAddButtonClickListener() {
        binding.ivHomeAdd.setOnSingleClickListener {
            PopupMenu(it.context, onAddButtonClick = {
                navigateToAdd()
            }).showAsDropDown(it, -55, 0)
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
                    binding.lHomeRefresh.isEnabled = state == ViewPager2.SCROLL_STATE_IDLE
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

    private fun setGetAllPlantsStateObserver() {
        viewModel.getAllPlantsState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {
                is UiState.Loading -> {
                }

                is UiState.Success -> {
                    Timber.d("Success : Get Plants ")
                    initPlantViewPager(state.data)
                }

                is UiState.Failure -> {
                    Timber.d("Failure : ${state.msg}")
                }

                is UiState.Empty -> {
                    initPlantViewPager(listOf(viewModel.emptyItemForAddPlant))
                }
            }
        }.launchIn(lifecycleScope)
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

    private fun navigateToAdd() {
        navigateTo<AddPlantFragment>()
    }

    private fun navigateToDetailWithBundle(bundle: Bundle) {
        navigateTo<DetailFragment>(bundle)
    }

    private fun navigateToUploadWithBundle(bundle: Bundle) {
        navigateTo<UploadFragment>(bundle)
    }

    private inline fun <reified T : Fragment> navigateTo() {
        activity?.supportFragmentManager?.commit {
            replace<T>(R.id.fcv_main, T::class.simpleName).addToBackStack(ROOT_FRAGMENT_HOME)
        }
    }

    private inline fun <reified T : Fragment> navigateTo(args: Bundle) {
        parentFragmentManager.commit {
            replace<T>(
                R.id.fcv_main,
                T::class.simpleName,
                args
            ).addToBackStack("HomeToDetail")
        }
    }

    companion object {
        private const val SCROLL_DELAY_TIME = 5000L
        private const val VIEWPAGER_PREVIEW_WIDTH = 60
        private const val VIEWPAGER_ITEM_MARGIN = 50

        private const val ROOT_FRAGMENT_HOME = "homeFragment"

        private const val KEY_FROM_DETAIL_DELETE = "KEY_FROM_DETAIL_DELETE"
        private const val KEY_FROM_ADD = "KEY_FROM_ADD"
    }
}
