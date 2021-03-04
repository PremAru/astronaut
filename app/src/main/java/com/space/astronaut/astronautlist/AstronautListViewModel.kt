package com.space.astronaut.astronautlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.space.astronaut.model.AstronoutList
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
    private val astronoutInfoData = MutableLiveData<Resource<AstronoutList>>()
    lateinit var astronoutInfoMain: AstronoutList

    fun fetchAstronoutList() {
        astronoutInfoData.postValue(Resource.loading(null))
        userInfoService.getAstronoutsList(JSON)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<AstronoutList>() {
                override fun onComplete() {
                    Timber.i("Astronaut detail complete block executed")
                }

                override fun onNext(astronoutList: AstronoutList) {
                    Timber.i("Astronaut detail success response received")

                    astronoutInfoMain = astronoutList;
                    val astronauts = astronoutInfoMain.results.sortedBy { results -> results.name }
                    astronoutInfoMain.results = astronauts
                    astronoutInfoData.postValue(Resource.success(astronoutInfoMain))
                }

                override fun onError(e: Throwable?) {
                    Timber.e("Astronaut detail error response received $e")
                    astronoutInfoData.postValue(Resource.error("${e?.localizedMessage}", null))
                }
            })
    }

    fun getAstronout(): LiveData<Resource<AstronoutList>> {
        return astronoutInfoData
    }

    fun getSortedAstronout() {
        Collections.reverse(astronoutInfoMain.results)
        astronoutInfoData.postValue(Resource.success(astronoutInfoMain))
    }



}