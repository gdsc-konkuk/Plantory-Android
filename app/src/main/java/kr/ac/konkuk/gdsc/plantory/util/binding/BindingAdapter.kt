package kr.ac.konkuk.gdsc.plantory.util.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.transform.RoundedCornersTransformation
import kr.ac.konkuk.gdsc.plantory.R

@BindingAdapter("setImageUrl")
fun ImageView.setImageUrl(imageUrl: String?) {
    if (imageUrl == null) return
    load(imageUrl) {
        transformations(RoundedCornersTransformation(10F))
    }
}

@BindingAdapter("setCheckBoxImage")
fun ImageView.setCheckBoxImage(boolean: Boolean) {
    if (boolean) {
        load(R.drawable.ic_diary_checked)
    }else{
        load(R.drawable.ic_diary_unchecked)
    }
}