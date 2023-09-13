package com.pascal.rawgapps.domain.usecase.get_wishlist_games

import android.content.Context
import com.pascal.rawgapps.domain.model.UIModelListing
import com.pascal.rawgapps.data.local.model.toUIModelListing
import com.pascal.rawgapps.data.local.service.ModelDatabase
import com.pascal.rawgapps.di.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetWishlistGamesUseCase @Inject constructor(
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(context: Context): ArrayList<UIModelListing> =
        withContext(defaultDispatcher) {
            val dao = ModelDatabase(context).modelDao()
            ArrayList(dao.getAllFromWishlist().map { it.toUIModelListing() })
        }
}