package com.space.astronaut.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Results(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("nationality")
    val nationality: String,
    @SerializedName("profile_image_thumbnail")
    val profile_image_thumbnail: String
): Serializable