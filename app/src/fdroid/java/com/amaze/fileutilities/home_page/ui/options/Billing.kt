/*
 * Copyright (C) 2021-2023 Arpit Khurana <arpitkh96@gmail.com>, Vishal Nehra <vishalmeham2@gmail.com>,
 * Emmanuel Messulam<emmanuelbendavid@gmail.com>, Raymond Lai <airwave209gt at gmail.com> and Contributors.
 *
 * This file is part of Amaze File Utilities.
 *
 * Amaze File Utilities is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.amaze.fileutilities.home_page.ui.options

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.amaze.fileutilities.utilis.PreferencesConstants
import com.amaze.fileutilities.utilis.Utils
import com.amaze.fileutilities.utilis.getAppCommonSharedPreferences
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Billing(val context: Context, private var uniqueId: String) {

    var log: Logger = LoggerFactory.getLogger(Billing::class.java)

    companion object {
        private const val URL_AUTHOR_2_PAYPAL = "https://www.paypal.me/vishalnehra"
        private const val URL_LIBERAPAY = "https://liberapay.com/Team-Amaze/donate"

        fun getInstance(activity: AppCompatActivity): Billing? {
            var billing: Billing? = null
            val deviceId = activity.getAppCommonSharedPreferences()
                .getString(PreferencesConstants.KEY_DEVICE_UNIQUE_ID, null)
            deviceId?.let {
                billing = Billing(activity, deviceId)
            }
            return billing
        }

        fun getInstance(context: Context): Billing? {
            var billing: Billing? = null
            val deviceId = context.getAppCommonSharedPreferences()
                .getString(PreferencesConstants.KEY_DEVICE_UNIQUE_ID, null)
            deviceId?.let {
                billing = Billing(context, deviceId)
            }
            return billing
        }
    }

    fun getSubscriptions(resultCallback: () -> Unit) {
        resultCallback.invoke()
    }

    /** Start a purchase flow  */
    fun initiatePurchaseFlow() {
        Utils.buildPurchaseFdroidDialog(context, {
            Utils.openURL(URL_AUTHOR_2_PAYPAL, context)
        }, {
            Utils.openURL(URL_LIBERAPAY, context)
        }).show()
    }
}
