package io.aethibo.fireshare.ui.utils.interpolators

import android.view.animation.Interpolator
import kotlin.math.cos
import kotlin.math.pow

internal class BounceInterpolator(amplitude: Double, frequency: Double) : Interpolator {

    private var mAmplitude = 1.0
    private var mFrequency = 10.0

    override fun getInterpolation(time: Float): Float = (-1 * Math.E.pow(-time / mAmplitude) *
            cos(mFrequency * time) + 1).toFloat()

    init {
        mAmplitude = amplitude
        mFrequency = frequency
    }
}