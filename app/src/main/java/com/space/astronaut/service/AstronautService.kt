package com.space.astronaut.service

import com.space.astronaut.model.AstronoutList
import com.space.astronaut.utils.Constants
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface AstronautService {
    @GET(Constants.ASTRONAUT)
    fun getAstronoutsList(@Query(Constants.ASTRONAUT_FORMAT) string: String): Observable<AstronoutList>
}
