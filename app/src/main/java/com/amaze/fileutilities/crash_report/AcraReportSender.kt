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

package com.amaze.fileutilities.crash_report

import android.content.Context
import com.amaze.fileutilities.R
import org.acra.data.CrashReportData
import org.acra.sender.ReportSender

class AcraReportSender : ReportSender {

    override fun send(context: Context, errorContent: CrashReportData) {
        ErrorActivity.reportError(
            context, errorContent,
            ErrorActivity.ErrorInfo.make(
                ErrorActivity.ERROR_UI_ERROR,
                "Application crash", R.string.app_ui_crash
            )
        )
    }

    override fun requiresForeground(): Boolean {
        return true
    }
}
