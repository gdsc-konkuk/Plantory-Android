package kr.ac.konkuk.gdsc.plantory.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.ac.konkuk.gdsc.plantory.databinding.ItemHomePlantBinding
import kr.ac.konkuk.gdsc.plantory.domain.entity.Plant
import kr.ac.konkuk.gdsc.plantory.util.view.ItemDiffCallback

class HomeAdapter(
    private val onItemClick: () -> Unit,
    private val onAddPlantButtonClick: () -> Unit,
    private val onUploadDiaryButtonClick: () -> Unit
) :
    ListAdapter<Plant, HomeAdapter.HomeViewHolder>(
        ItemDiffCallback<Plant>(
            onItemsTheSame = { old, new -> old.id == new.id },
            onContentsTheSame = { old, new -> old == new }
        )
    ) {

    class HomeViewHolder(
        private val binding: ItemHomePlantBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(
            data: Plant,
            position: Int,
            itemCount: Int,
            onItemClick: () -> Unit,
            onAddPlantButtonClick: () -> Unit,
            onUploadDiaryButtonClick: () -> Unit
        ) {
            binding.data = data
            if (position == itemCount - 1) {
                binding.root.setOnClickListener { }
                binding.ivPlantUpload.setOnClickListener { onAddPlantButtonClick.invoke() }
            } else {
                binding.root.setOnClickListener { onItemClick.invoke() }
                binding.ivPlantUpload.setOnClickListener { onUploadDiaryButtonClick.invoke() }
            }
            binding.executePendingBindings()
        }
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.onBind(getItem(position), position, itemCount, onItemClick, onAddPlantButtonClick, onUploadDiaryButtonClick)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding =
            ItemHomePlantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding)
    }

}