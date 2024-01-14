package kr.ac.konkuk.gdsc.plantory.util.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow

object PopupMenu {
    fun showCustomPopup(view: View, layoutId: Int) {
        val inflater = LayoutInflater.from(view.context)
        val popupView = inflater.inflate(layoutId, null)
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true

        popupWindow.showAsDropDown(
            view,
            -55, 0
        )
    }
}
