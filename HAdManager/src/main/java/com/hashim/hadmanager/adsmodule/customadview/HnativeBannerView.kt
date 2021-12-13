package com.hashim.hadmanager.adsmodule.customadview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.hashim.hadmanager.R
import com.hashim.hadmanager.adsmodule.hDp
import com.hashim.hadmanager.databinding.HnativeBannerLayoutBinding
import timber.log.Timber


class HnativeBannerView(
    context: Context,
    attrs: AttributeSet?
) : ConstraintLayout(context, attrs) {

    var hLayoutHAdcontainerBinding = HnativeBannerLayoutBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    init {

        val hTypedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.hHAdContainerClStyleable, 0, 0
        )
        hLayoutHAdcontainerBinding.hShimmerLoader
        hSetBackGroundDrawable(hTypedArray)
    }

    private fun hSetBackGroundDrawable(hTypedArray: TypedArray) {
        hTypedArray.apply {
            val hColor = getColor(
                R.styleable.hHAdContainerClStyleable_hBackgroundColor,
                ContextCompat.getColor(context, R.color.white)

            )

            val hCornerRadius = getInt(
                R.styleable.hHAdContainerClStyleable_hCornerRadius,
                6
            )
            val hStrokeColor = getColor(
                R.styleable.hHAdContainerClStyleable_hStrokeColor,
                ContextCompat.getColor(context, R.color.colorPrimaryDark)
            )

            val hStrokeWidth = getInt(
                R.styleable.hHAdContainerClStyleable_hStrokeWidth,
                1
            )

            hSetBackGroundDrawable(
                hBackgroundColor = hColor,
                hCornerRadius = hCornerRadius,
                Stroke(
                    hContext = context,
                    hColor = hStrokeColor,
                    hWidth = hStrokeWidth,
                )
            )
        }

    }

    fun hShowHideAdLoader(hShowLoader: Boolean) {
        when (hShowLoader) {
            true -> {
                hLayoutHAdcontainerBinding.hShimmerLoader.visibility = View.VISIBLE
                hLayoutHAdcontainerBinding.hAdContainer.visibility = View.GONE
            }
            false -> {
                hLayoutHAdcontainerBinding.hShimmerLoader.visibility = View.GONE
                hLayoutHAdcontainerBinding.hAdContainer.visibility = View.VISIBLE
            }
        }

    }

    fun hShowHideAdView(hShowAdView: Boolean) {
        when (hShowAdView) {
            true -> {
                hLayoutHAdcontainerBinding.hAdContainer.visibility = View.VISIBLE
                hLayoutHAdcontainerBinding.hShimmerLoader.visibility = View.GONE
            }
            false -> {
                hLayoutHAdcontainerBinding.hAdContainer.visibility = View.GONE
                hLayoutHAdcontainerBinding.hShimmerLoader.visibility = View.VISIBLE
            }
        }
    }

    fun hSetBackGroundDrawable(
        hBackgroundColor: Int,
        hCornerRadius: Int = 6,
        hStroke: Stroke = Stroke(context)
    ) {
        val hGradientDrawable = GradientDrawable()
        hGradientDrawable.apply {
            setColor(hBackgroundColor)
            cornerRadius = hDp(context, hCornerRadius)
            setStroke(
                hStroke.hWidth,
                hStroke.hColor
            )
            hLayoutHAdcontainerBinding.hAdRootCL.background = hGradientDrawable
        }
    }

    fun hShowAdView(
        viewGroup: ViewGroup? = null,
        view: View? = null,
    ) {
        try {
            viewGroup?.let {
                hLayoutHAdcontainerBinding.hAdContainer.apply {
                    removeAllViews()
                    addView(it)
                }
                hShowHideAdLoader(hShowLoader = false)
            }
            view?.let {
                hLayoutHAdcontainerBinding.hAdContainer.apply {
                    removeAllViews()
                    addView(it)
                }
                hShowHideAdLoader(hShowLoader = false)
            }

        } catch (e: Exception) {
            Timber.d("Exception is ${e.message}")
        }

    }
}


