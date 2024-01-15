package kr.ac.konkuk.gdsc.plantory.util.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ViewPagerDecoration(private val margin: Int): RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.left = margin
        outRect.right = margin
    }
}
