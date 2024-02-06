package kr.ac.konkuk.gdsc.plantory.presentation.plant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.ac.konkuk.gdsc.plantory.R
import kr.ac.konkuk.gdsc.plantory.databinding.ItemDetailCalendarBinding
import kr.ac.konkuk.gdsc.plantory.domain.entity.PlantHistory
import kr.ac.konkuk.gdsc.plantory.domain.entity.PlantHistoryType
import kr.ac.konkuk.gdsc.plantory.util.view.ItemDiffCallback
import kr.ac.konkuk.gdsc.plantory.util.view.setOnSingleClickListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailAdapter(
    private val currMonth: Int,
    private val plantHistories: List<PlantHistory>,
    private val onDateClick: (Date) -> Unit
) : ListAdapter<Date, DetailAdapter.ViewHolder>(
    ItemDiffCallback<Date>(
        onItemsTheSame = { old, new -> old == new },
        onContentsTheSame = { old, new -> old == new }
    )
) {

    class ViewHolder(
        private val binding: ItemDetailCalendarBinding,
        private val onDateClick: (Date) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        var currMonth: Int = 0
        private val dayText: TextView = binding.tvCalenderDate

        fun onBind(date: Date, plantDailyRecords: List<PlantHistory>) {
            val dateFormat = SimpleDateFormat("dd", Locale.getDefault())
            dayText.text = dateFormat.format(date).toString()

            if (currMonth != date.month) {
                dayText.alpha = 0.4f
            }

            if ((bindingAdapterPosition + 1) % 7 == 0) {
                dayText.setTextColor(ContextCompat.getColor(binding.root.context, R.color.blue))
            } else if (bindingAdapterPosition == 0 || bindingAdapterPosition % 7 == 0) {
                dayText.setTextColor(ContextCompat.getColor(binding.root.context, R.color.red))
            }

            val isToday = dateFormat.format(date) == dateFormat.format(Date())

            binding.llCalendarDay.apply {
                if (isToday) {
                    setBackgroundResource(R.drawable.oval_gray_100_fill)
                } else {
                    setBackgroundResource(0)
                }
            }

            val recordsByDate = plantDailyRecords.groupBy { it.date.takeLast(2) }

            recordsByDate.forEach { (date, records) ->
                binding.apply {
                    val waterChangeVisible =
                        records.any { it.type == PlantHistoryType.WATER_CHANGE }
                    val recordingVisible = records.any { it.type == PlantHistoryType.RECORDING }
                    ivCalendarWateredStamp.visibility =
                        if (waterChangeVisible) View.VISIBLE else View.GONE
                    ivCalendarRecordedStamp.visibility =
                        if (recordingVisible) View.VISIBLE else View.GONE
                }
            }
            binding.llCalendarDay.setOnSingleClickListener {
                onDateClick(date)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemDetailCalendarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onDateClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.currMonth = currMonth
        val currentDate = getItem(position)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedCurrentDate = dateFormat.format(currentDate)

        val plantDailyRecordsForDate = plantHistories.filter {
            it.date.substring(0, 4) == formattedCurrentDate.substring(0, 4) &&
                it.date.substring(5, 7) == formattedCurrentDate.substring(5, 7) &&
                it.date.takeLast(2) == formattedCurrentDate.takeLast(2)
        }
        holder.onBind(currentDate, plantDailyRecordsForDate)
    }
}
