package com.space.astronaut.astronautlist

import android.view.View
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
    private val progressBar = MutableLiveData<Int>()

    fun fetchAstronautList() {
        progressBar.value = View.VISIBLE
        userInfoService.getAstronautsList(JSON)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<Astronauts>() {
                override fun onComplete() {
                    Timber.i("Astronaut detail complete block executed")
                }

                override fun onNext(astronauts: Astronauts) {
                    Timber.i("Astronaut detail success response received")
                    progressBar.value = View.GONE
                    astronautList = astronauts
                    val astronauts = astronautList.results.sortedBy { results -> results.name }
                    astronautList.results = astronauts
                    astronautInfoData.postValue(Resource.success(astronautList))

                }

                override fun onError(e: Throwable?) {
                    Timber.e("Astronaut detail error response received $e")
                    progressBar.value = View.GONE
                    astronautInfoData.postValue(Resource.error("${e?.localizedMessage}", null))
                }
            })
    }

    fun getAstronaut(): LiveData<Resource<Astronauts>> {
        return astronautInfoData
    }

    fun getProgressBarStatus(): LiveData<Int> {
        return progressBar
    }

    fun getSortedAstronaut() {
        Collections.reverse(astronautList.results)
        astronautInfoData.postValue(Resource.success(astronautList))
    }
}