package com.example.demovoteinfo.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VotingCenter(
    val name: String,
    val maleVoters: Int,
    val femaleVoters: Int,
    val thirdGenderVoters: Int,
    val contactName: String? = null,
    val contactPhone: String? = null
) : Parcelable {
    val totalVoters: Int
        get() = maleVoters + femaleVoters + thirdGenderVoters
}
