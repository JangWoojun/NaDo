package com.woojun.nado.util

import android.content.Context

object Utils {
    fun Context.dpToPx(dp: Float): Float {
        return dp * this.resources.displayMetrics.density
    }
}
