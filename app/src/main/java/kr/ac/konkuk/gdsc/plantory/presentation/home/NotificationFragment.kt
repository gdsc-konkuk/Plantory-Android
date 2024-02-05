package kr.ac.konkuk.gdsc.plantory.presentation.home

import android.os.Bundle
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import kr.ac.konkuk.gdsc.plantory.R
import kr.ac.konkuk.gdsc.plantory.databinding.FragmentNotificationBinding
import kr.ac.konkuk.gdsc.plantory.domain.entity.Notification
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment

@AndroidEntryPoint
class NotificationFragment :
    BindingFragment<FragmentNotificationBinding>(R.layout.fragment_notification) {
    private lateinit var notificationAdapter: NotificationAdapter
    private lateinit var viewModel: NotificationViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel = NotificationViewModel()
        initMockData()
        addListener()
    }

    private fun addListener() {
        initBackButtonClickListener()
    }

    private fun initMockData() {
        initAdapter(viewModel.notificationList)
    }

    private fun initAdapter(list: List<Notification>) {
        notificationAdapter = NotificationAdapter().apply {
            binding.rvNotification.adapter = this
            submitList(list)
        }
    }

    private fun initBackButtonClickListener() {
        binding.ivNotificationBack.setOnClickListener {
            navigateToNotification()
        }
    }

    private fun navigateToNotification() {
        activity?.supportFragmentManager?.popBackStack()
    }
}
