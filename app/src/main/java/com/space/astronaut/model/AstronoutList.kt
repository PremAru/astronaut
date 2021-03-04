package com.space.astronaut.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AstronoutList(
    @SerializedName("count")
    val count: Int,
    @SerializedName("results")
    var results: List<Results>
) : Serializable