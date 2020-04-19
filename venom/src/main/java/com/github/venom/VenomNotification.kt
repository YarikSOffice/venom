package com.github.venom

import android.content.Context
import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.android.parcel.Parcelize

@Parcelize
class VenomNotification(
    var contentTitle: String,
    var contentText: String,
    var killButton: String,
    var cancelButton: String,
    @DrawableRes var smallIconDrawableRes: Int
) : Parcelable {
    internal companion object {
        fun default(context: Context) = with(context) {
            VenomNotification(
                contentTitle = getString(R.string.venom_foreground_service_title),
                contentText = getString(R.string.venom_foreground_service_text),
                killButton = getString(R.string.venom_notification_button_kill),
                cancelButton  = getString(R.string.venom_notification_button_cancel),
                smallIconDrawableRes = R.drawable.android_adb
            )
        }
    }
}

