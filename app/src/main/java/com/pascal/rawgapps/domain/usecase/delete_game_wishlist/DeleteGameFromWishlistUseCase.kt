package com.pascal.rawgapps.domain.usecase.delete_game_wishlist

import android.content.Context
import com.pascal.rawgapps.data.local.service.ModelDatabase
import com.pascal.rawgapps.di.DefaultDispatcher
import kotlinx.coroutines.*
import javax.inject.Inject

class DeleteGameFromWishlistUseCase @Inject constructor(
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(context: Context, uuid: Int) = withContext(defaultDispatcher) {
        val dao = ModelDatabase(context).modelDao()
        dao.deleteFromWishlist(uuid)
    }
}