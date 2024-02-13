package kr.ac.konkuk.gdsc.plantory.util.binding

import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import coil.transform.RoundedCornersTransformation
import kr.ac.konkuk.gdsc.plantory.R
import kr.ac.konkuk.gdsc.plantory.util.date.DateUtil

@BindingAdapter("setImageUrl")
fun ImageView.setImageUrl(imageUrl: String?) {
    imageUrl?.let { url ->
        if (url.startsWith("R.drawable.")) {
            val drawableResId = context.resources.getIdentifier(
                url.removePrefix("R.drawable."),
                "drawable",
                context.packageName
            )
            setImageResource(drawableResId)
        } else {
            load(url) {
                transformations(RoundedCornersTransformation(20F))
            }
        }
    }
}

@BindingAdapter("setCheckBoxImage")
fun ImageView.setCheckBoxImage(isChecked: Boolean) {
    load(if (isChecked) R.drawable.ic_diary_checked else R.drawable.ic_diary_unchecked)
}

@BindingAdapter("setRegisterBackgroundResource")
fun Button.setRegisterBackgroundResource(isFieldNotEmpty: Boolean) {
    setBackgroundResource(if (isFieldNotEmpty) R.drawable.shape_mint_fill_10 else R.drawable.shape_gray_200_fill_10)
}

@BindingAdapter("app:daysFromBirthDate", "app:nickname")
fun TextView.setDaysFromBirthDateAndNickname(birthDate: String, nickname: String) {
    val date = DateUtil.parseDate(birthDate)
    text = if (date != null) {
        val days = DateUtil.calculateDaysFromDate(date) + 1
        context.getString(R.string.home_plant_dday, nickname, days)
    } else {
        context.getString(R.string.home_plant_add_plant_item)
    }
}
