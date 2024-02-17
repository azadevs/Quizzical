package android.azadev.quizzical.data.remote.response


import android.azadev.quizzical.model.DetailedAnswerResult
import com.google.gson.annotations.SerializedName

data class QuizResponse(
    @SerializedName("response_code")
    val responseCode: Int,
    @SerializedName("results")
    val results: List<DetailedAnswerResult>
)
