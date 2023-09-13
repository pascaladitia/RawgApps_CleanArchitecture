package com.pascal.rawgapps.domain.usecase.get_game_details

import com.pascal.rawgapps.data.remote.model.RAWGResponseGameDetails
import com.pascal.rawgapps.domain.repository.IApiRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GetGameDetailsUseCase @Inject constructor(
    private val repository: IApiRepository
) {
    operator fun invoke(id: Int): Single<RAWGResponseGameDetails> {
        return repository.getGameDetails(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}