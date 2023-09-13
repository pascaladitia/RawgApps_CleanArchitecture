package com.pascal.rawgapps.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pascal.rawgapps.data.remote.model.RAWGResponseGameListResult
import com.pascal.rawgapps.data.remote.model.toUIModelListings
import com.pascal.rawgapps.domain.model.UIModelListing
import com.pascal.rawgapps.domain.model.UIPlatforms
import com.pascal.rawgapps.domain.usecase.get_games_by_nexturl.GetGamesByNextURLUseCase
import com.pascal.rawgapps.domain.usecase.get_games_by_search.GetGamesBySearchUseCase
import com.pascal.rawgapps.domain.usecase.get_games_by_search_platform.GetGamesBySearchPlatformUseCase
import com.pascal.rawgapps.domain.usecase.get_platforms.GetPlatformsUseCase
import com.pascal.rawgapps.domain.usecase.json_object_converter.JsonGsonConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ListingFragmentViewModel @Inject constructor(
    private val getPlatformsUseCase: GetPlatformsUseCase,
    private val getGamesByNextURLUseCase: GetGamesByNextURLUseCase,
    private val getGamesBySearchUseCase: GetGamesBySearchUseCase,
    private val getGamesBySearchPlatformUseCase: GetGamesBySearchPlatformUseCase,
    private val jsonGsonConverter: JsonGsonConverter,
) : ViewModel() {

    val listing = MutableLiveData<ArrayList<UIModelListing>>()
    val platforms = MutableLiveData<ArrayList<UIPlatforms>>()

    val searchString = MutableLiveData<String>("")
    val selectedPlatformID = MutableLiveData<Int?>()

    val nextPageURL = MutableLiveData<String?>("")

    val loading = MutableLiveData<Boolean>(false)

    val isFiltered = MutableLiveData<Boolean>(false)

    val selectedPlatformPosition = MutableLiveData<Int>()
    val previousSelectedPlatformID = MutableLiveData<Int?>()

    val showPBar = MutableLiveData<Boolean>()

    private val disposable = CompositeDisposable()


    init {
        listing.value = arrayListOf()
        platforms.value = arrayListOf()
    }


    fun getNextData(context: Context) {
        showPBar.value = true
        nextPageURL.value?.let {
            getGamesByNextPage(context)
        } ?: kotlin.run {
            loading.value = true
            loading.value = false
            Toast.makeText(context, "Sorry. There are no games left to show.", Toast.LENGTH_SHORT)
                .show()
            showPBar.value = false
        }
    }

    fun getNewListing() {
        showPBar.value = true
        selectedPlatformID.value?.let {
            getGamesBySearchPlatforms()
        } ?: getGamesBySearch()
    }

    fun setPlatformValues(selectedPlatformId: Int, selectedPlatformPos: Int) {
        previousSelectedPlatformID.value = selectedPlatformID.value
        selectedPlatformID.value = selectedPlatformId
        selectedPlatformPosition.value = selectedPlatformPos
        isFiltered.value = true
    }

    fun setSearchValues(searchStr: String?) {
        searchString.value = searchStr ?: ""
        isFiltered.value = true
    }

    fun clearSearch() {
        searchString.value = ""
        if (selectedPlatformPosition.value == -1) {
            isFiltered.value = false
        }
    }

    fun clearPlatformFilters() {
        previousSelectedPlatformID.value = selectedPlatformID.value
        selectedPlatformID.value = null
        selectedPlatformPosition.value = -1
        if (searchString.value == "") {
            isFiltered.value = false
        }

    }

    fun clearFilters() {
        clearSearch()
        clearPlatformFilters()
    }

    private fun getGamesByNextPage(context: Context) {
        disposable.add(getGamesByNextURLUseCase(nextPageURL.value!!)//TODO Subscribe
            .subscribeWith(object : DisposableSingleObserver<RAWGResponseGameListResult>() {
                override fun onSuccess(t: RAWGResponseGameListResult) {
                    t.toUIModelListings().let {
                        loading.value = true
                        nextPageURL.value = it.first
                        listing.value!!.addAll(it.second)
                        loading.value = false
                        showPBar.value = false
                    }
                }

                override fun onError(e: Throwable) {
                    Timber.d(
                        "APIERROR",
                        e.localizedMessage ?: e.message
                        ?: "An error occured while getting game listings."
                    )
                    getGamesByNextPage(context)
                }
            })
        )
    }

    private fun getGamesBySearch() {
        disposable.add(getGamesBySearchUseCase(searchString.value ?: "")
            .subscribeWith(object : DisposableSingleObserver<RAWGResponseGameListResult>() {
                override fun onSuccess(t: RAWGResponseGameListResult) {
                    t.toUIModelListings().let {
                        loading.value = true
                        nextPageURL.value = it.first
                        listing.value = it.second!!
                        loading.value = false
                        showPBar.value = false
                    }
                }

                override fun onError(e: Throwable) {
                    Timber.d(
                        "APIERROR",
                        e.localizedMessage ?: e.message
                        ?: "An error occured while getting game listings."
                    )
                }
            })
        )
    }

    private fun getGamesBySearchPlatforms() {
        disposable.add(getGamesBySearchPlatformUseCase(
            searchString.value ?: "",
            selectedPlatformID.value!!
        )
            .subscribeWith(object : DisposableSingleObserver<RAWGResponseGameListResult>() {
                override fun onSuccess(t: RAWGResponseGameListResult) {
                    t.toUIModelListings().let {
                        loading.value = true
                        nextPageURL.value = it.first
                        listing.value = it.second!!
                        loading.value = false
                        showPBar.value = false
                    }
                }

                override fun onError(e: Throwable) {
                    Timber.d(
                        "APIERROR",
                        e.localizedMessage ?: e.message
                        ?: "An error occured while getting game listings."
                    )
                }
            })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}