package kr.ac.konkuk.gdsc.plantory.util.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import kr.ac.konkuk.gdsc.plantory.databinding.MenuDetailBinding

class PopupDeleteMenu(
    context: Context,
    private val onAddButtonClick: () -> Unit
) : PopupWindow(context) {

    private val binding: MenuDetailBinding by lazy {
        val inflater = LayoutInflater.from(context)
        MenuDetailBinding.inflate(inflater, null, false)
    }

    init {
        isOutsideTouchable = true
        isTouchable = true
        contentView = binding.root
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        width = ViewGroup.LayoutParams.WRAP_CONTENT
        height = ViewGroup.LayoutParams.WRAP_CONTENT

        binding.ivMenuAdd.setOnClickListener {
            onAddButtonClick.invoke()
            dismiss()
        }
    }
}
