package com.pascal.rawgapps.domain.usecase.alert_dialog

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import javax.inject.Inject


class AlertDialogUseCase @Inject constructor() {
    private lateinit var dialog: AlertDialog
    operator fun invoke(
        context: Context,
        titleString: String,
        messageString: String,
        positiveString: String,
        negativeString: String,
        positiveOnClick: () -> Unit,
        negativeOnClick: () -> Unit = this::negativeOnClick
    ): AlertDialog {
        val alertDialogBuilder = AlertDialog.Builder(context)
            .setTitle(titleString)
            .setMessage(messageString)
            .setPositiveButton(
                positiveString
            ) { p0, p1 -> positiveOnClick() }
            .setNegativeButton(
                negativeString
            ) { p0, p1 -> negativeOnClick() }
        dialog = alertDialogBuilder.create()

        return dialog
    }

    private fun negativeOnClick() {
        dialog.dismiss()
    }

}