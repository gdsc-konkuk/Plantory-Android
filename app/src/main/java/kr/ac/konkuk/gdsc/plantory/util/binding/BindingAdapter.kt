package kr.ac.konkuk.gdsc.plantory.util.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.transform.RoundedCornersTransformation

@BindingAdapter("setImageUrl")
fun ImageView.setImageUrl(imageUrl: String?) {
    if (imageUrl == null) return
    load(imageUrl) {
        transformations(RoundedCornersTransformation(10F))
    }
}

