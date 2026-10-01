package app.k9mail.core.ui.legacy.designsystem.atom.container

import android.content.Context
import android.graphics.Outline
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes
import app.k9mail.core.ui.legacy.designsystem.R
import com.google.android.material.color.MaterialColors
import app.k9mail.core.ui.legacy.theme2.common.R as Theme2R

/**
 * A rounded content container drawn on top of the tinted screen background.
 *
 * Children are clipped to the rounded shape, so scrolling content stays inside the container. Only the top corners are
 * rounded unless `containerRoundBottom` is set.
 */
class ContainerLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {

    private var cornerRadius = resources.getDimension(R.dimen.container_corner_radius)
    private var roundBottom = false

    init {
        var containerColor = MaterialColors.getColor(this, Theme2R.attr.colorContainerBackground)

        context.withStyledAttributes(attrs, R.styleable.ContainerLayout, defStyleAttr) {
            cornerRadius = getDimension(R.styleable.ContainerLayout_containerCornerRadius, cornerRadius)
            roundBottom = getBoolean(R.styleable.ContainerLayout_containerRoundBottom, false)
            containerColor = getColor(R.styleable.ContainerLayout_containerColor, containerColor)
        }

        setBackgroundColor(containerColor)
        outlineProvider = ContainerOutlineProvider()
        clipToOutline = true
    }

    private inner class ContainerOutlineProvider : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            val width = view.width
            val height = view.height

            if (roundBottom) {
                outline.setRoundRect(0, 0, width, height, cornerRadius)
            } else {
                // Extend the outline below the view so only the top corners are visibly rounded.
                outline.setRoundRect(0, 0, width, height + cornerRadius.toInt(), cornerRadius)
            }
        }
    }
}
