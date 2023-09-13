package com.pascal.rawgapps.domain.usecase.insert_game_wishlist

import android.content.Context
import com.pascal.rawgapps.data.local.model.DBModelDetails
import com.pascal.rawgapps.data.local.service.ModelDatabase
import com.pascal.rawgapps.di.DefaultDispatcher
import kotlinx.coroutines.*
import javax.inject.Inject

class InsertGameIntoWishlistUseCase @Inject constructor(
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(context: Context, dbModelDetails: DBModelDetails) =
        withContext(defaultDispatcher) {
            val dao = ModelDatabase(context).modelDao()
            dao.insertIntoWishlist(dbModelDetails)
        }
}