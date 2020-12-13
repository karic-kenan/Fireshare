package io.aethibo.fireshare.features.utils

import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import io.aethibo.fireshare.R

fun View.slideUp(context: Context, animationTime: Long, startOffset: Long) {
    val slideUp = AnimationUtils.loadAnimation(context, R.anim.slide_up).apply {
        duration = animationTime
        interpolator = FastOutSlowInInterpolator()
        this.startOffset = startOffset
    }
    startAnimation(slideUp)
}

fun slideUpViews(
        context: Context,
        vararg views: View,
        animationTime: Long = 300L,
        delay: Long = 150L
) {
    for (i in views.indices) {
        views[i].slideUp(context, animationTime, delay * i)
    }
}