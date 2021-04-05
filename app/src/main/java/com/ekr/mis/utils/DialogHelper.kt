package com.ekr.mis.utils

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.provider.Settings
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import com.ekr.mis.R

class DialogHelper {
    companion object {

        fun globalLoading(activity: Activity): Dialog {
            val dialog = Dialog(activity)
            dialog.setContentView(R.layout.dialog_loading_global)
            dialog.setCanceledOnTouchOutside(false)
            dialog.window!!.setLayout(
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager
                    .LayoutParams.WRAP_CONTENT
            )
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.attributes.windowAnimations = android.R.style.Animation_Dialog
            dialog.setCancelable(true)
            return dialog
        }

        fun splashPhoneNumber(activity: Activity): Dialog {
            val dialog = Dialog(activity)
            dialog.setContentView(R.layout.dialog_phone_number)
            dialog.setCanceledOnTouchOutside(false)
            dialog.window!!.setLayout(
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager
                    .LayoutParams.WRAP_CONTENT
            )
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.attributes.windowAnimations = android.R.style.Animation_Dialog
            dialog.setCancelable(true)
            return dialog
        }

        fun showSettingsDialog(context: Activity) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Perizinan Diperlukan")
            builder.setMessage("Aktifkan Perizinan untuk Melanjutkan Aktivitas")
            builder.setPositiveButton("BUKA PENGATURAN") { dialog: DialogInterface, which: Int ->
                dialog.cancel()
                openSettings(context)
            }
            builder.setNegativeButton(
                "Cancel"
            ) { dialog: DialogInterface, which: Int -> dialog.cancel() }
            builder.show()
        }

        private fun openSettings(context: Activity) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", context.packageName, null)
            intent.data = uri
            context.startActivityForResult(intent, 101)
        }
    }

}