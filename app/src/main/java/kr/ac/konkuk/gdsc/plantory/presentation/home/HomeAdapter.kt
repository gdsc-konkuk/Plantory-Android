package kr.ac.konkuk.gdsc.plantory.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.ac.konkuk.gdsc.plantory.databinding.ItemHomePlantBinding
import kr.ac.konkuk.gdsc.plantory.domain.entity.Plant
import kr.ac.konkuk.gdsc.plantory.util.view.ItemDiffCallback
import kr.ac.konkuk.gdsc.plantory.util.view.setOnSingleClickListener

class HomeAdapter(private val onItemClick: () -> Unit) :
    ListAdapter<Plant, HomeAdapter.HomeViewHolder>(
        ItemDiffCallback<Plant>(
            onItemsTheSame = { old, new -> old.id == new.id },
            onContentsTheSame = { old, new -> old == new }
        )
    ) {

    class HomeViewHolder(
        private val binding: ItemHomePlantBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: Plant) {
            binding.data = data
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding =
            ItemHomePlantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.itemView.setOnSingleClickListener {
            onItemClick.invoke()
        }
        holder.onBind(getItem(position))
    }

}