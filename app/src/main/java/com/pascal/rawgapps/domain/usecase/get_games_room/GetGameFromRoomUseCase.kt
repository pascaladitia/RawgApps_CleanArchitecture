package com.pascal.rawgapps.domain.usecase.get_games_room

import android.content.Context
import com.pascal.rawgapps.domain.model.UIModelDetails
import com.pascal.rawgapps.data.local.model.toUIModelDetails
import com.pascal.rawgapps.data.local.service.ModelDatabase
import com.pascal.rawgapps.di.DefaultDispatcher
import kotlinx.coroutines.*
import javax.inject.Inject

class GetGameFromRoomUseCase @Inject constructor(
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(context: Context, uuid: Int): UIModelDetails? =
        withContext(defaultDispatcher) {
            val dao = ModelDatabase(context).modelDao()
            dao.checkIfWishlist(uuid)?.toUIModelDetails()
        }
}