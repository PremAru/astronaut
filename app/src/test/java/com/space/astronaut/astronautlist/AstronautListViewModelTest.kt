package com.space.astronaut.astronautlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.then
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.space.astronaut.model.Astronauts
import com.space.astronaut.model.Results
import com.space.astronaut.service.AstronautService
import com.space.astronaut.utils.Constants
import com.space.astronaut.utils.Resource
import com.space.astronaut.utils.RxImmediateSchedulerRule
import io.reactivex.rxjava3.core.Observable
import okio.IOException
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

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
    lateinit var observer: Observer<Resource<Astronauts>>




    @Before
    fun setUp() {
        astronautListViewModel = AstronautListViewModel(astronautService)
    }

    @Test
    fun shouldAstronautAPIReturnSuccess() {
        val astronautData = createAstronautData()
        whenever(astronautService.getAstronautsList(Constants.JSON)).thenReturn(
            Observable.just(
                astronautData
            )
        )
        astronautListViewModel.getAstronaut().observeForever(observer)

        astronautListViewModel.fetchAstronautList()

        verify(astronautService).getAstronautsList(Constants.JSON)
        then(verify(observer).onChanged(Resource.loading(null)))
        then(verify(observer).onChanged(Resource.success(astronautData)))
        astronautListViewModel.getAstronaut().removeObserver(observer)
    }

    @Test
    fun shouldAstronautAPIReturnError() {
        val errorMessage = "Error message"
        whenever(astronautService.getAstronautsList(Constants.JSON)).thenReturn(
            Observable.error(
                IOException(
                    errorMessage
                )
            )
        )
        astronautListViewModel.getAstronaut().observeForever(observer)
        astronautListViewModel.fetchAstronautList()
        verify(astronautService).getAstronautsList(Constants.JSON)
        then(verify(observer).onChanged(Resource.loading(null)))
        then(verify(observer).onChanged(Resource.error(errorMessage, null)))
        astronautListViewModel.getAstronaut().removeObserver(observer)
    }

    @Test
    fun shouldSortByName() {
        val astronautData = createAstronautData()
        val expectedAstronautData = createAstronautData()
        Collections.reverse(expectedAstronautData.results);

        astronautListViewModel.getAstronaut().observeForever(observer)
        astronautListViewModel.astronautList = astronautData;

        astronautListViewModel.getSortedAstronaut()
        then(verify(observer).onChanged(Resource.success(expectedAstronautData)))
    }

    private fun createAstronautData(): Astronauts {
        /*
        The results are explicity set to have the name starting with 'A' to be the end so we
        could test sorting
         */
        val resultsList = arrayListOf<Results>(
            Results(
                id="1",
                name = "Zranz Viehböck",
                nationality = "Austrian",
                profile_image_thumbnail = "https://spacelaunchnow-prod-east.nyc3.cdn.digitaloceanspaces.com/media/default/cache/54/57/5457ce75acb7b188196eb442e3f17b64.jpg"
            ),
            Results(
                id="2",
                name = "Aarcos Pontes",
                nationality = "U.S.A",
                profile_image_thumbnail = "https://spacelaunchnow-prod-east.nyc3.cdn.digitaloceanspaces.com/media/default/cache/b5/9b/b59bb16a31087708ffb212d3e6938946.jpg"
            )
        )
        val astronoutData = Astronauts(1, resultsList)
        return astronoutData
    }

}