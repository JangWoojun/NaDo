package com.woojun.nado

import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonSizeSpec

object ToolTip {
    fun createBalloon(
        context: Context,
        view: View,
        viewLifecycleOwner: LifecycleOwner,
        text: String
    ): Balloon {
        return Balloon.Builder(context)
            .setHeight(BalloonSizeSpec.WRAP)
            .setWidth(view.width - 24)
            .setText(text)
            .setPaddingVertical(28)
            .setArrowPositionRules(ArrowPositionRules.ALIGN_BALLOON)
            .setTextColorResource(R.color.white)
            .setTextSize(18f)
            .setCornerRadius(10f)
            .setIsVisibleArrow(false)
            .setTextTypeface(R.font.spoqahansansneo_bold)
            .setBackgroundColorResource(R.color.balloon_background)
            .setLifecycleOwner(viewLifecycleOwner)
            .setAutoDismissDuration(3000L)
            .setMarginHorizontal(12)
            .build()
    }

    private fun Context.dpToPx(dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            resources.displayMetrics
        ).toInt()
    }
}