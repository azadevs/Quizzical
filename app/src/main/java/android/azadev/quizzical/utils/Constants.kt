package android.azadev.quizzical.utils

import android.azadev.quizzical.R
import android.content.Context
import androidx.core.content.ContextCompat.getString

/**
 * Created by : Azamat Kalmurzayev
 * Date : 2/16/2024
 */

object Constants {

    const val BASE_URL = "https://opentdb.com/"

    const val QUESTIONS_AMOUNT = 20

    const val HISTORY_ID = 23

    const val GEOGRAPHY_ID = 22

    const val MATH_ID = 19

    const val SPORT_ID = 21

    const val GENERAL_KNOWLEDGE = 9

    const val TOTAL_SECONDS = 20

    const val PREFS_SCORE_ID = "scoreId"
    const val PREFS_USER_ID = "userId"
    const val PREFS_IS_HAVE = "isHave"

    private fun getMonth(month: Int, context: Context): String {
        return when (month) {
            1 -> getString(context, R.string.month_january)
            2 -> getString(context, R.string.month_february)
            3 -> getString(context, R.string.month_march)
            4 -> getString(context, R.string.month_april)
            5 -> getString(context, R.string.month_may)
            6 -> getString(context, R.string.month_june)
            7 -> getString(context, R.string.month_july)
            8 -> getString(context, R.string.month_august)
            9 -> getString(context, R.string.month_september)
            10 -> getString(context, R.string.month_october)
            11 -> getString(context, R.string.month_november)
            12 -> getString(context, R.string.month_december)
            else -> {
                getString(context, R.string.month_january)
            }
        }
    }

    fun getAllTabsNames() = listOf("All time", "This week", "Month")

    fun makeDateToString(day: Int, month: Int, year: Int, context: Context): String {
        return "${getMonth(month, context)} $day $year"
    }


}