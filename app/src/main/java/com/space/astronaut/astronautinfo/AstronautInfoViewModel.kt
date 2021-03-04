package com.space.astronaut.astronautinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.space.astronaut.model.AstronautDetails
import com.space.astronaut.service.AstronautService
import com.space.astronaut.utils.Constants
import com.space.astronaut.utils.Resource
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class AstronautInfoViewModel @Inject constructor(private var astronautService: AstronautService) :
    ViewModel() {
    private val astronautDetails = MutableLiveData<Resource<AstronautDetails>>()

    fun getAstronautDetails(): LiveData<Resource<AstronautDetails>> {
        return astronautDetails
    }

    fun fetchAstronautDetails(astronautId: String) {
        astronautDetails.postValue(Resource.loading(null))
        astronautService.getAstronautDetails( astronautId, Constants.JSON)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<AstronautDetails>() {
                override fun onComplete() {
                    Timber.i("Astronaut detail complete block executed")
                }

                override fun onNext(astronautDetails: AstronautDetails) {
                    Timber.i("Astronaut detail success response received")

                    this@AstronautInfoViewModel.astronautDetails.postValue(Resource.success(astronautDetails))
                }

                override fun onError(e: Throwable?) {
                    Timber.e("Astronaut detail error response received $e")
                    astronautDetails.postValue(Resource.error("${e?.localizedMessage}", null))
                }
            })
    }

}