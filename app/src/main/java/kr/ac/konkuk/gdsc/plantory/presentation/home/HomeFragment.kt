package kr.ac.konkuk.gdsc.plantory.presentation.home

import android.os.Bundle
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import kr.ac.konkuk.gdsc.plantory.R
import kr.ac.konkuk.gdsc.plantory.databinding.FragmentHomeBinding
import kr.ac.konkuk.gdsc.plantory.domain.entity.Plant
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment

@AndroidEntryPoint
class HomeFragment  : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home){
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var viewModel: HomeViewModel
    private var plantItemCount = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel = HomeViewModel()
        initMockData()
    }

    private fun initMockData(){
        initPlantViewPager(viewModel.plantList)
    }
    private fun initPlantViewPager(plants: List<Plant>) {
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

}