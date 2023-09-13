package com.pascal.rawgapps.domain.repository

import com.pascal.rawgapps.data.remote.model.RAWGResponseGameDetails
import com.pascal.rawgapps.data.remote.model.RAWGResponseGameListResult
import com.pascal.rawgapps.data.remote.model.RAWGResponsePlatformsResult
import io.reactivex.Single


interface IApiRepository {

    fun getGameDetails(id:Int): Single<RAWGResponseGameDetails>

    fun getPlatforms():Single<RAWGResponsePlatformsResult>

    fun getGamesBySearch(searchString: String):Single<RAWGResponseGameListResult>

    fun getGamesBySearchPlatform(searchString: String,platformId:Int):Single<RAWGResponseGameListResult>

    fun getGamesByNextURL(urlString: String):Single<RAWGResponseGameListResult>
}