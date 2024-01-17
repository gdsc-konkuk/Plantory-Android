package kr.ac.konkuk.gdsc.plantory.util.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow

object PopupMenu {

    private var popupWindow: PopupWindow? = null
    fun showCustomPopup(view: View, layoutId: Int): PopupWindow {
        val inflater = LayoutInflater.from(view.context)
        val popupView = inflater.inflate(layoutId, null)
        popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        popupWindow?.isOutsideTouchable = true
        popupWindow?.isFocusable = true

        popupWindow?.showAsDropDown(
            view,
            -55, 0
        )
        return popupWindow as PopupWindow
    }

    fun dismissPopup() {
        popupWindow?.dismiss()
    }
}
