package com.pascal.rawgapps.viewmodel

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pascal.rawgapps.R
import com.pascal.rawgapps.data.local.model.toDBModelDetails
import com.pascal.rawgapps.data.remote.model.RAWGResponseGameDetails
import com.pascal.rawgapps.data.remote.model.toUIModelDetails
import com.pascal.rawgapps.domain.model.UIModelDetails
import com.pascal.rawgapps.domain.usecase.alert_dialog.AlertDialogUseCase
import com.pascal.rawgapps.domain.usecase.delete_game_wishlist.DeleteGameFromWishlistUseCase
import com.pascal.rawgapps.domain.usecase.get_game_details.GetGameDetailsUseCase
import com.pascal.rawgapps.domain.usecase.get_games_room.GetGameFromRoomUseCase
import com.pascal.rawgapps.domain.usecase.insert_game_wishlist.InsertGameIntoWishlistUseCase
import com.pascal.rawgapps.domain.usecase.popup_webview.PopupWebViewUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetailsFragmentViewModel @Inject constructor(
    private val getGameDetailsUseCase: GetGameDetailsUseCase,
    private val popupWebViewUseCase: PopupWebViewUseCase,
    private val getGameDetailsFromRoomUseCase: GetGameFromRoomUseCase,
    private val deleteGameFromWishlistUseCase: DeleteGameFromWishlistUseCase,
    private val insertGameIntoWishlistUseCase: InsertGameIntoWishlistUseCase,
    private val alertDialogUseCase: AlertDialogUseCase
) : ViewModel() {
    val gameDetails = MutableLiveData<UIModelDetails>()

    val gameID = MutableLiveData<Int>()

    val isSavedOnDB = MutableLiveData<Boolean>()

    private val disposable = CompositeDisposable()

    fun getGameDetails(id: Int, context: Context) {
        gameID.value = id
        checkWishlist(context)
    }

    private fun getGameDetailsFromAPI() {
        disposable.add(
            getGameDetailsUseCase(gameID.value!!)
                .subscribeWith(object : DisposableSingleObserver<RAWGResponseGameDetails>() {
                    override fun onSuccess(t: RAWGResponseGameDetails) {
                        gameDetails.value = t.toUIModelDetails()
                    }

                    override fun onError(e: Throwable) {
                        Timber.d("APIERROR", "An error occured while getting game details from API.")
                    }
                })

        )
    }

    fun insertIntoWishlist(context: Context) {
        viewModelScope.launch {
            insertGameIntoWishlistUseCase(context, gameDetails.value!!.toDBModelDetails())
            isSavedOnDB.value = true
        }
    }

    fun deleteFromWishlist(context: Context) {
        viewModelScope.launch {
            deleteGameFromWishlistUseCase(context, gameID.value!!)
            isSavedOnDB.value = false
        }
    }

    private fun checkWishlist(context: Context) {
        viewModelScope.launch {
            getGameFromDB(context)?.let {
                isSavedOnDB.value = true
                gameDetails.value = it
            } ?: getGameDetailsFromAPI()
        }
    }

    suspend fun getGameFromDB(context: Context): UIModelDetails? {
        return getGameDetailsFromRoomUseCase(context, gameID.value!!)
    }

    fun showWebsiteWebView(context: Context, url: String): Dialog {
        return popupWebViewUseCase(context, R.layout.popup_webview, url)
    }

    fun showAlertDialog(
        context: Context,
        titleString: String,
        messageString: String,
        positiveString: String,
        negativeString: String,
        positiveOnclick: () -> Unit,
        negativeOnClick: (() -> Unit)?
    ): AlertDialog {

        negativeOnClick?.let {
            return alertDialogUseCase(
                context,
                titleString,
                messageString,
                positiveString,
                negativeString,
                positiveOnclick,
                negativeOnClick
            )
        } ?: return alertDialogUseCase(
            context,
            titleString,
            messageString,
            positiveString,
            negativeString,
            positiveOnclick
        )

    }
}