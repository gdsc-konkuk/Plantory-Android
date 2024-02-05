package kr.ac.konkuk.gdsc.plantory.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.ac.konkuk.gdsc.plantory.databinding.ItemHomeNotificationBinding
import kr.ac.konkuk.gdsc.plantory.domain.entity.Notification
import kr.ac.konkuk.gdsc.plantory.util.view.ItemDiffCallback

class NotificationAdapter :
    ListAdapter<Notification, NotificationAdapter.NotificationHolder>(
        ItemDiffCallback<Notification>(
            onItemsTheSame = { old, new -> old.id == new.id },
            onContentsTheSame = { old, new -> old == new }
        )
    ) {

    class NotificationHolder(
        private val binding: ItemHomeNotificationBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: Notification) {
            binding.data = data
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
        val binding =
            ItemHomeNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {
        holder.onBind(getItem(position))
    }
}
