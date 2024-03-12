package android.azadev.quizzical.ui.leaderboard.adapter

import android.annotation.SuppressLint
import android.azadev.quizzical.R
import android.azadev.quizzical.data.local.entity.UserAndScore
import android.azadev.quizzical.databinding.ItemUserBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

/**
 * Created by : Azamat Kalmurzayev
 * Date : 3/11/2024
 */

class LeaderboardUserAdapter(
    private val users: List<UserAndScore>
) : RecyclerView.Adapter<LeaderboardUserAdapter.UserViewHolder>() {

    inner class UserViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun onBind(userAndScore: UserAndScore, position: Int) {
            val user = userAndScore.userEntity
            val score = userAndScore.scoreEntity
            if (user.userImageUri.isEmpty()) {
                binding.ivUserImage.setImageResource(R.drawable.placeholder)
            } else {
                Glide.with(binding.root).load(user.userImageUri)
                    .into(binding.ivUserImage)
            }
            binding.tvUserFullName.text =
                "${user.fName} ${user.lName}"
            binding.tvUserScore.text = "${score.score}"
            val positionInRank = (position + 1) % (users.size + 1)
            binding.tvUserPosition.text = positionInRank.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.onBind(users[position], position)
    }

    override fun getItemCount(): Int = users.size
}