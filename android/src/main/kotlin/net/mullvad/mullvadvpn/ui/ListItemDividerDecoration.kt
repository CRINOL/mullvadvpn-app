package net.mullvad.mullvadvpn.ui

import android.content.Context
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ItemDecoration
import android.support.v7.widget.RecyclerView.State
import android.view.View
import net.mullvad.mullvadvpn.R

class ListItemDividerDecoration(private val context: Context) : ItemDecoration() {
    private val dividerHeight = context.resources.getDimensionPixelSize(R.dimen.list_item_divider)

    override fun getItemOffsets(offsets: Rect, view: View, parent: RecyclerView, state: State) {
        val position = parent.getChildAdapterPosition(view)
        val lastItem = parent.adapter!!.itemCount - 1

        if (position != lastItem) {
            offsets.bottom = dividerHeight
        }
    }
}
