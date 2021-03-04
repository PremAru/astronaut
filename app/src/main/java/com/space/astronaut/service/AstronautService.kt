package com.space.astronaut.service

import com.space.astronaut.model.AstronautDetails
import com.space.astronaut.model.Astronauts
import com.space.astronaut.utils.Constants
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AstronautService {
    @GET(Constants.ASTRONAUT)
    fun getAstronautsList(@Query(Constants.ASTRONAUT_FORMAT) string: String): Observable<Astronauts>

    @GET(Constants.ASTRONAUT_DETAILS)
    fun getAstronautDetails(
        @Path(Constants.ASTRONAUT_ID) id: String,
        @Query(Constants.ASTRONAUT_FORMAT) format: String
    ): Observable<AstronautDetails>
}
