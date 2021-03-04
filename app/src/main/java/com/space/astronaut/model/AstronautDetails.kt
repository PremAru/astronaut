package com.space.astronaut.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AstronautDetails(
    @SerializedName("date_of_birth")
    val date_of_birth: String,
    @SerializedName("bio")
    val bio: String
) : Serializable