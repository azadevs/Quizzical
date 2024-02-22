package android.azadev.quizzical.utils

import android.view.View

/**
 * Created by : Azamat Kalmurzayev
 * Date : 2/22/2024
 */

object UIExtensions {

    fun View.visible() {
        visibility = View.VISIBLE
    }

    fun View.inVisible() {
        visibility = View.INVISIBLE
    }
}