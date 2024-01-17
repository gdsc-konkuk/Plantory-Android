package kr.ac.konkuk.gdsc.plantory.presentation.plant

import android.text.TextUtils.substring
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kr.ac.konkuk.gdsc.plantory.R
import kr.ac.konkuk.gdsc.plantory.databinding.ItemDetailCalendarBinding
import kr.ac.konkuk.gdsc.plantory.domain.entity.PlantDailyRecord
import kr.ac.konkuk.gdsc.plantory.util.view.ItemDiffCallback
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DetailAdapter(
    private val currMonth: Int,
    private val dayList: MutableList<Date>,
    private val dummyinfo: MutableList<PlantDailyRecord>,
) : androidx.recyclerview.widget.ListAdapter<Date, DetailAdapter.ViewHolder>(
    ItemDiffCallback<Date>(
        onItemsTheSame = { old, new -> old == new },
        onContentsTheSame = { old, new -> old == new }
    )
) {

    class ViewHolder(
        private val binding: ItemDetailCalendarBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        var currMonth: Int = 0
        private val dayText: TextView = binding.tvCalenderDate

        fun onBind(date: Date, plantDailyRecords: List<PlantDailyRecord>) {
            val dateFormat = SimpleDateFormat("dd", Locale.getDefault())
            dayText.text = dateFormat.format(date).toString()

            if (currMonth != date.month) {
                dayText.alpha = 0.4f
            }

            // 토요일이면 파란색 || 일요일이면 빨간색으로 색상표시
            if ((adapterPosition + 1) % 7 == 0) {
                dayText.setTextColor(ContextCompat.getColor(binding.root.context, R.color.blue))
            } else if (adapterPosition == 0 || adapterPosition % 7 == 0) {
                dayText.setTextColor(ContextCompat.getColor(binding.root.context, R.color.red))
            }

            //물주기/분갈이 색상 다르게
            for (record in plantDailyRecords) {
                if (record.date.takeLast(2) != dateFormat.format(date).toString()) {
                    continue
                }
                if (record.checkList.isWatered) {
                    binding.ivCalendarWateredStamp.visibility = View.VISIBLE
                }
                if (record.checkList.isRepoted) {
                    binding.ivCalendarRepotedStamp.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemDetailCalendarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.currMonth = currMonth
        val currentDate = dayList[position]
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        val formattedCurrentDate = dateFormat.format(currentDate)

        val plantDailyRecordsForDate = dummyinfo.filter {
            it.date.substring(0, 4) == formattedCurrentDate.substring(0, 4) &&
                    it.date.substring(5, 7) == formattedCurrentDate.substring(5, 7) &&
                    it.date.takeLast(2) == formattedCurrentDate.takeLast(2)
        }
        holder.onBind(currentDate, plantDailyRecordsForDate)
    }
}