package kr.ac.konkuk.gdsc.plantory.util.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import kr.ac.konkuk.gdsc.plantory.R
import kr.ac.konkuk.gdsc.plantory.presentation.plant.AddPlantFragment

object PopupMenu {

    fun showCustomPopup(view: View, layoutId: Int) {
        var popupWindow: PopupWindow
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
    }
}
