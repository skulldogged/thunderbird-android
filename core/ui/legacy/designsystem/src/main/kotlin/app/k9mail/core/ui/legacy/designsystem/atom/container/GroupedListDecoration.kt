package app.k9mail.core.ui.legacy.designsystem.atom.container

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.RippleDrawable
import android.view.View
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import app.k9mail.core.ui.legacy.designsystem.R
import com.google.android.material.color.MaterialColors
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import app.k9mail.core.ui.legacy.theme2.common.R as Theme2R

/**
 * Displays the items between two headers as a group of rounded containers, like the lists in the Android system
 * settings.
 *
 * @param isHeader Returns `true` if the item at the given adapter position is a section header. Headers are drawn
 * without a container and start a new group.
 */
class GroupedListDecoration(
    context: Context,
    private val isHeader: (position: Int) -> Boolean,
) : RecyclerView.ItemDecoration() {
    private val resources = context.resources
    private val horizontalMargin = resources.getDimensionPixelSize(R.dimen.grouped_list_horizontal_margin)
    private val itemGap = resources.getDimensionPixelSize(R.dimen.grouped_list_item_gap)
    private val listEdgeSpacing = resources.getDimensionPixelSize(R.dimen.grouped_list_edge_spacing)
    private val outerCornerRadius = resources.getDimension(R.dimen.grouped_list_outer_corner_radius)
    private val innerCornerRadius = resources.getDimension(R.dimen.grouped_list_inner_corner_radius)
    private val containerColor = MaterialColors.getColor(context, Theme2R.attr.colorContainerBackground, 0)
    private val rippleColor = MaterialColors.getColor(context, android.R.attr.colorControlHighlight, 0)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        if (position == RecyclerView.NO_POSITION) return

        outRect.left = horizontalMargin
        outRect.right = horizontalMargin

        val segment = parent.segmentAt(position) ?: return
        if (!segment.isFirst) {
            outRect.top = itemGap
        } else if (position == 0) {
            outRect.top = listEdgeSpacing
        }

        if (position == state.itemCount - 1) {
            outRect.bottom = listEdgeSpacing
        }
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        for (child in parent.children) {
            val position = parent.getChildAdapterPosition(child)
            if (position != RecyclerView.NO_POSITION) {
                parent.segmentAt(position)?.let { segment -> child.applySegmentBackground(segment) }
            }
        }
    }

    private fun RecyclerView.segmentAt(position: Int): Segment? {
        val itemCount = adapter?.itemCount ?: 0
        if (position >= itemCount || isHeader(position)) return null

        val isFirst = position == 0 || isHeader(position - 1)
        val isLast = position == itemCount - 1 || isHeader(position + 1)

        return Segment(isFirst, isLast)
    }

    // View holders may restore their original background when they are rebound, so the background is (re)applied
    // while drawing whenever it doesn't match the item's current position in its group.
    private fun View.applySegmentBackground(segment: Segment) {
        val current = getTag(R.id.grouped_list_background) as? SegmentBackground
        if (current != null && current.segment == segment && background === current.drawable) return

        val shape = ShapeAppearanceModel.builder()
            .setTopLeftCornerSize(if (segment.isFirst) outerCornerRadius else innerCornerRadius)
            .setTopRightCornerSize(if (segment.isFirst) outerCornerRadius else innerCornerRadius)
            .setBottomLeftCornerSize(if (segment.isLast) outerCornerRadius else innerCornerRadius)
            .setBottomRightCornerSize(if (segment.isLast) outerCornerRadius else innerCornerRadius)
            .build()

        val content = MaterialShapeDrawable(shape).apply { fillColor = ColorStateList.valueOf(containerColor) }
        val mask = MaterialShapeDrawable(shape)
        val drawable = RippleDrawable(ColorStateList.valueOf(rippleColor), content, mask)

        background = drawable
        setTag(R.id.grouped_list_background, SegmentBackground(segment, drawable))
    }

    private data class Segment(val isFirst: Boolean, val isLast: Boolean)

    private class SegmentBackground(val segment: Segment, val drawable: RippleDrawable)
}
