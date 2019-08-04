package co.djurayev.connectionview.utils

import android.content.Context
import android.util.TypedValue

object ScreenUtils {

    fun dpToPx(context: Context, valueInDp: Float): Float {
        val metrics = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics)
    }

    fun toDp(context: Context, value: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (value * scale + 0.5f).toInt()
    }

    fun getStatusBarHeight(context: Context): Int {
        var statusBarHeight = toDp(context, 24f)

        val resource = context.resources.getIdentifier("status_bar_height", "dimen", "android")

        if (resource > 0) {
            statusBarHeight = context.resources.getDimensionPixelSize(resource)
        }

        return statusBarHeight
    }
}
