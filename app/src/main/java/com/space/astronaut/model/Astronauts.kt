package com.space.astronaut.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Astronauts(
    @SerializedName("count")
    val count: Int,

    @SerializedName("results")
    var results: List<Results>

) : Serializable