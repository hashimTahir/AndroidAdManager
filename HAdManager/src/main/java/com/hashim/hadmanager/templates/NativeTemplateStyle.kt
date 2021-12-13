package com.hashim.hadmanager.templates

import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable

class NativeTemplateStyle {
    // Call to action typeface.
    var callToActionTextTypeface: Typeface? = null
        private set

    // Size of call to action text.
    var callToActionTextSize = 0f
        private set

    // Call to action typeface color in the form 0xAARRGGBB.
    var callToActionTypefaceColor = 0
        private set

    // Call to action background color.
    var callToActionBackgroundColor: ColorDrawable? = null
        private set

    // All templates have a primary text area which is populated by the native ad's headline.
    // Primary text typeface.
    var primaryTextTypeface: Typeface? = null
        private set

    // Size of primary text.
    var primaryTextSize = 0f
        private set

    // Primary text typeface color in the form 0xAARRGGBB.
    var primaryTextTypefaceColor = 0
        private set

    // Primary text background color.
    var primaryTextBackgroundColor: ColorDrawable? = null
        private set

    // The typeface, typeface color, and background color for the second row of text in the template.
    // All templates have a secondary text area which is populated either by the body of the ad or
    // by the rating of the app.
    // Secondary text typeface.
    var secondaryTextTypeface: Typeface? = null
        private set

    // Size of secondary text.
    var secondaryTextSize = 0f
        private set

    // Secondary text typeface color in the form 0xAARRGGBB.
    var secondaryTextTypefaceColor = 0
        private set

    // Secondary text background color.
    var secondaryTextBackgroundColor: ColorDrawable? = null
        private set

    // The typeface, typeface color, and background color for the third row of text in the template.
    // The third row is used to display store name or the default tertiary text.
    // Tertiary text typeface.
    var tertiaryTextTypeface: Typeface? = null
        private set

    // Size of tertiary text.
    var tertiaryTextSize = 0f
        private set

    // Tertiary text typeface color in the form 0xAARRGGBB.
    var tertiaryTextTypefaceColor = 0
        private set

    // Tertiary text background color.
    var tertiaryTextBackgroundColor: ColorDrawable? = null
        private set

    // The background color for the bulk of the ad.
    var mainBackgroundColor: ColorDrawable? = null
        private set

    /** A class that provides helper methods to build a style object. *  */
    class Builder {
        private val styles: NativeTemplateStyle = NativeTemplateStyle()
        fun withCallToActionTextTypeface(callToActionTextTypeface: Typeface?): Builder {
            styles.callToActionTextTypeface = callToActionTextTypeface
            return this
        }

        fun withCallToActionTextSize(callToActionTextSize: Float): Builder {
            styles.callToActionTextSize = callToActionTextSize
            return this
        }

        fun withCallToActionTypefaceColor(callToActionTypefaceColor: Int): Builder {
            styles.callToActionTypefaceColor = callToActionTypefaceColor
            return this
        }

        fun withCallToActionBackgroundColor(callToActionBackgroundColor: ColorDrawable?): Builder {
            styles.callToActionBackgroundColor = callToActionBackgroundColor
            return this
        }

        fun withPrimaryTextTypeface(primaryTextTypeface: Typeface?): Builder {
            styles.primaryTextTypeface = primaryTextTypeface
            return this
        }

        fun withPrimaryTextSize(primaryTextSize: Float): Builder {
            styles.primaryTextSize = primaryTextSize
            return this
        }

        fun withPrimaryTextTypefaceColor(primaryTextTypefaceColor: Int): Builder {
            styles.primaryTextTypefaceColor = primaryTextTypefaceColor
            return this
        }

        fun withPrimaryTextBackgroundColor(primaryTextBackgroundColor: ColorDrawable?): Builder {
            styles.primaryTextBackgroundColor = primaryTextBackgroundColor
            return this
        }

        fun withSecondaryTextTypeface(secondaryTextTypeface: Typeface?): Builder {
            styles.secondaryTextTypeface = secondaryTextTypeface
            return this
        }

        fun withSecondaryTextSize(secondaryTextSize: Float): Builder {
            styles.secondaryTextSize = secondaryTextSize
            return this
        }

        fun withSecondaryTextTypefaceColor(secondaryTextTypefaceColor: Int): Builder {
            styles.secondaryTextTypefaceColor = secondaryTextTypefaceColor
            return this
        }

        fun withSecondaryTextBackgroundColor(secondaryTextBackgroundColor: ColorDrawable?): Builder {
            styles.secondaryTextBackgroundColor = secondaryTextBackgroundColor
            return this
        }

        fun withTertiaryTextTypeface(tertiaryTextTypeface: Typeface?): Builder {
            styles.tertiaryTextTypeface = tertiaryTextTypeface
            return this
        }

        fun withTertiaryTextSize(tertiaryTextSize: Float): Builder {
            styles.tertiaryTextSize = tertiaryTextSize
            return this
        }

        fun withTertiaryTextTypefaceColor(tertiaryTextTypefaceColor: Int): Builder {
            styles.tertiaryTextTypefaceColor = tertiaryTextTypefaceColor
            return this
        }

        fun withTertiaryTextBackgroundColor(tertiaryTextBackgroundColor: ColorDrawable?): Builder {
            styles.tertiaryTextBackgroundColor = tertiaryTextBackgroundColor
            return this
        }

        fun withMainBackgroundColor(mainBackgroundColor: ColorDrawable?): Builder {
            styles.mainBackgroundColor = mainBackgroundColor
            return this
        }

        fun build(): NativeTemplateStyle {
            return styles
        }

    }
}