package com.example.demovoteinfo

import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.demovoteinfo.model.VotingCenter

class VoterDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_VOTING_CENTER = "extra_voting_center"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voter_detail)
        title = getString(R.string.title_activity_voter_detail)

        val tvCenterNameTitle: TextView = findViewById(R.id.tvCenterNameTitle)
        val tvMaleVoters: TextView = findViewById(R.id.tvMaleVoters)
        val tvFemaleVoters: TextView = findViewById(R.id.tvFemaleVoters)
        val tvThirdGenderVoters: TextView = findViewById(R.id.tvThirdGenderVoters)
        val tvTotalVoters: TextView = findViewById(R.id.tvTotalVoters)

        @Suppress("DEPRECATION")
        val votingCenter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_VOTING_CENTER, VotingCenter::class.java)
        } else {
            intent.getParcelableExtra<VotingCenter>(EXTRA_VOTING_CENTER)
        }

        votingCenter?.let {
            tvCenterNameTitle.text = it.name
            tvMaleVoters.text = "${getString(R.string.male_voters)}: ${it.maleVoters}"
            tvFemaleVoters.text = "${getString(R.string.female_voters)}: ${it.femaleVoters}"
            tvThirdGenderVoters.text = "${getString(R.string.third_gender_voters)}: ${it.thirdGenderVoters}"
            tvTotalVoters.text = "${getString(R.string.total_voters)}: ${it.totalVoters}"
        }
    }
}
