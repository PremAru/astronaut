package com.space.astronaut.astronautinfo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.then
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.space.astronaut.model.AstronautDetails
import com.space.astronaut.service.AstronautService
import com.space.astronaut.utils.Constants
import com.space.astronaut.utils.Resource
import com.space.astronaut.utils.RxImmediateSchedulerRule
import io.reactivex.rxjava3.core.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import java.io.IOException

class AstronautInfoViewModelTest {
    lateinit var astronautInfoViewModel: AstronautInfoViewModel

    @Mock
    lateinit var astronautService: AstronautService

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    lateinit var observer: Observer<Resource<AstronautDetails>>

    private val bio =
        "Franz Artur Viehböck (born August 24, 1960 in Vienna) is an Austrian electrical engineer, and was Austria's first cosmonaut. He was titulated „Austronaut“ by his country's media. He visited the Mir space station in 1991 aboard Soyuz TM-13, returning aboard Soyuz TM-12 after spending just over a week in space."
    private val astronautId = "2"
    private val astronautDOB = "1960-08-24"

    @Before
    fun setUp() {
        astronautInfoViewModel = AstronautInfoViewModel(astronautService)
    }

    @Test
    fun shouldAstronautDetailsApiReturnSuccess() {
        val astronautDetails = createAstronoutDetailsData()
        whenever(astronautService.getAstronautDetails(Constants.JSON, astronautId)).thenReturn(
            Observable.just(
                astronautDetails
            )
        )
        astronautInfoViewModel.getAstronautDetails().observeForever(observer)
        astronautInfoViewModel.fetchAstronautDetails(astronautId)
        verify(astronautService).getAstronautDetails(Constants.JSON, astronautId)
        then(verify(observer).onChanged(Resource.loading(null)))
        then(verify(observer).onChanged(Resource.success(astronautDetails)))
        astronautInfoViewModel.getAstronautDetails().removeObserver(observer)
    }

    @Test
    fun shouldAstronautDetailsApiReturnError() {
        val errorMessage = "Error message"
        whenever(astronautService.getAstronautDetails(Constants.JSON, astronautId)).thenReturn(
            Observable.error(
                IOException(
                    errorMessage
                )
            )
        )
        astronautInfoViewModel.getAstronautDetails().observeForever(observer)
        astronautInfoViewModel.fetchAstronautDetails(astronautId)
        verify(astronautService).getAstronautDetails(Constants.JSON, astronautId)
        then(verify(observer).onChanged(Resource.loading(null)))
        then(verify(observer).onChanged(Resource.error(errorMessage, null)))
        astronautInfoViewModel.getAstronautDetails().removeObserver(observer)
    }

    private fun createAstronoutDetailsData(): AstronautDetails {
        val astronautDetails = AstronautDetails(astronautDOB, bio)
        return astronautDetails
    }
}