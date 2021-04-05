package com.ekr.mis.ui.guest.register

import java.io.File

interface RegisterContract {
    interface Presenter {
        fun doRegister(
            image: File?,
            email: String,
            token: String,
            nama: String,
            kodepos: String,
            telpon: String,
            alamat: String,
            agama: String
        )
    }

    interface View {
        fun initListener()
        fun onLoading(loading: Boolean)
        fun showMessage(message: String)
        fun doPermission()
        fun doValidate(
            email: String,
            alamat: String,
            nama: String,
            id_qr: String,
            kodepos: String,
            telp: String,
            agama: String,
            token: String,
            file: File?,
        )
    }


}