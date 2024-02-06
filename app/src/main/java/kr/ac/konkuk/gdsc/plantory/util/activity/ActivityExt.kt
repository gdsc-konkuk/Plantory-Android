package kr.ac.konkuk.gdsc.plantory.util.activity

import android.app.Activity
import android.view.View
import kr.ac.konkuk.gdsc.plantory.R
import kr.ac.konkuk.gdsc.plantory.util.context.hideKeyboard
fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Activity.applyScreenExitAnimation() {
    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
}

fun Activity.applyScreenEnterAnimation() {
    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
}
