package com.space.astronaut.astronautlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.space.astronaut.model.Astronauts
import com.space.astronaut.service.AstronautService
import com.space.astronaut.utils.Constants.JSON
import com.space.astronaut.utils.Resource
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class AstronautListViewModel @Inject constructor(private var userInfoService: AstronautService) :
    ViewModel() {
    private val astronautInfoData = MutableLiveData<Resource<Astronauts>>()
    lateinit var astronautList: Astronauts

    fun fetchAstronautList() {
        astronautInfoData.postValue(Resource.loading(null))
        userInfoService.getAstronautsList(JSON)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<Astronauts>() {
                override fun onComplete() {
                    Timber.i("Astronaut detail complete block executed")
                }

                override fun onNext(astronauts: Astronauts) {
                    Timber.i("Astronaut detail success response received")
                    astronautList = astronauts;
                    val astronauts = astronautList.results.sortedBy { results -> results.name }
                    astronautList.results = astronauts
                    astronautInfoData.postValue(Resource.success(astronautList))
                }

                override fun onError(e: Throwable?) {
                    Timber.e("Astronaut detail error response received $e")
                    astronautInfoData.postValue(Resource.error("${e?.localizedMessage}", null))
                }
            })
    }

    fun getAstronaut(): LiveData<Resource<Astronauts>> {
        return astronautInfoData
    }

    fun getSortedAstronaut() {
        Collections.reverse(astronautList.results)
        astronautInfoData.postValue(Resource.success(astronautList))
    }
}