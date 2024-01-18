package kr.ac.konkuk.gdsc.plantory.util.binding

import android.widget.Button
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.transform.RoundedCornersTransformation
import kr.ac.konkuk.gdsc.plantory.R

@BindingAdapter("setImageUrl")
fun ImageView.setImageUrl(imageUrl: String?) {
    if (imageUrl == null) return
    load(imageUrl) {
        transformations(RoundedCornersTransformation(8F))
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