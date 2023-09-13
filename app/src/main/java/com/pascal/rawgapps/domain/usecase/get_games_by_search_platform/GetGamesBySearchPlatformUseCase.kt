package com.pascal.rawgapps.domain.usecase.get_games_by_search_platform

import com.pascal.rawgapps.data.remote.model.RAWGResponseGameListResult
import com.pascal.rawgapps.domain.repository.IApiRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GetGamesBySearchPlatformUseCase @Inject constructor(
    private val repository: IApiRepository
) {
    operator fun invoke(searchString: String, platformID: Int): Single<RAWGResponseGameListResult> {
        return repository.getGamesBySearchPlatform(searchString, platformID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}