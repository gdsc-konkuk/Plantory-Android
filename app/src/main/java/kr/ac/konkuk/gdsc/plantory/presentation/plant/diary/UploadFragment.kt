package kr.ac.konkuk.gdsc.plantory.presentation.plant.diary

import android.os.Bundle
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import kr.ac.konkuk.gdsc.plantory.R
import kr.ac.konkuk.gdsc.plantory.databinding.FragmentAddPlantBinding
import kr.ac.konkuk.gdsc.plantory.databinding.FragmentUploadBinding
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment

@AndroidEntryPoint

class UploadFragment : BindingFragment<FragmentUploadBinding>(R.layout.fragment_upload){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}