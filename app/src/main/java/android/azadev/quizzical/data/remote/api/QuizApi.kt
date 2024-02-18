package android.azadev.quizzical.data.remote.api

import android.azadev.quizzical.data.remote.response.QuizResponse
import android.azadev.quizzical.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by : Azamat Kalmurzayev
 * Date : 2/16/2024
 */

interface QuizApi {
    @GET("api.php")
    suspend fun getQuizzesByCategoryId(
        @Query("amount") amount: Int = Constants.QUESTIONS_AMOUNT,
        @Query("category") categoryId: Int
    ): QuizResponse

}