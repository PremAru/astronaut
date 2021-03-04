package com.space.astronaut.astronautlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.then
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.space.astronaut.model.AstronoutList
import com.space.astronaut.model.Results
import com.space.astronaut.service.AstronautService
import com.space.astronaut.utils.Constants
import com.space.astronaut.utils.Resource
import com.space.astronaut.utils.RxImmediateSchedulerRule
import io.reactivex.rxjava3.core.Observable
import okio.IOException
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AstronautListViewModelTest {

    lateinit var astronautListViewModel: AstronautListViewModel

    @Mock
    lateinit var astronautService: AstronautService

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    lateinit var observer: Observer<Resource<AstronoutList>>




    @Before
    fun setUp() {
        astronautListViewModel = AstronautListViewModel(astronautService)
    }

    @Test
    fun shouldAstronautAPIReturnSuccess() {
        val astronoutData = createAstronoutData()
        whenever(astronautService.getAstronoutsList(Constants.JSON)).thenReturn(
            Observable.just(
                astronoutData
            )
        )
        astronautListViewModel.getAstronout().observeForever(observer)
        astronautListViewModel.fetchAstronoutList()
        verify(astronautService).getAstronoutsList(Constants.JSON)
        then(verify(observer).onChanged(Resource.loading(null)))
        then(verify(observer).onChanged(Resource.success(astronoutData)))
        astronautListViewModel.getAstronout().removeObserver(observer)
    }

    @Test
    fun shouldAstronautAPIReturnError() {
        val errorMessage = "Error message"
        whenever(astronautService.getAstronoutsList(Constants.JSON)).thenReturn(
            Observable.error(
                IOException(
                    errorMessage
                )
            )
        )
        astronautListViewModel.getAstronout().observeForever(observer)
        astronautListViewModel.fetchAstronoutList()
        verify(astronautService).getAstronoutsList(Constants.JSON)
        then(verify(observer).onChanged(Resource.loading(null)))
        then(verify(observer).onChanged(Resource.error(errorMessage, null)))
        astronautListViewModel.getAstronout().removeObserver(observer)
    }

    @Test
    fun shouldSortByName() {
        val astronoutData = createAstronoutData()
        astronautListViewModel.getAstronout().observeForever(observer)
        astronautListViewModel.getSortedAstronout()
        then(verify(observer).onChanged(Resource.success(astronoutData)))
    }

    private fun createAstronoutData(): AstronoutList {
        val resultsList = arrayListOf<Results>(
            Results(
                id="1",
                name = "Franz Viehböck",
                nationality = "Austrian",
                profile_image_thumbnail = "https://spacelaunchnow-prod-east.nyc3.cdn.digitaloceanspaces.com/media/default/cache/54/57/5457ce75acb7b188196eb442e3f17b64.jpg"
            ),
            Results(
                id="2",
                name = "Marcos Pontes",
                nationality = "Brazilian",
                profile_image_thumbnail = "https://spacelaunchnow-prod-east.nyc3.cdn.digitaloceanspaces.com/media/default/cache/b5/9b/b59bb16a31087708ffb212d3e6938946.jpg"
            )
        )
        val astronoutData = AstronoutList(1, resultsList)
        return astronoutData
    }

}