package com.pascal.rawgapps.domain.usecase.get_platforms

import com.pascal.rawgapps.data.remote.model.RAWGResponsePlatformsResult
import com.pascal.rawgapps.domain.repository.IApiRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GetPlatformsUseCase @Inject constructor(
    private val repository: IApiRepository
) {
    operator fun invoke(): Single<RAWGResponsePlatformsResult> {
        return repository.getPlatforms()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }


}