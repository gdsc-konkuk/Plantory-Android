package kr.ac.konkuk.gdsc.plantory.util.activity

import android.app.Activity
import android.view.View
import kr.ac.konkuk.gdsc.plantory.util.context.hideKeyboard
fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

