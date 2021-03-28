package com.ekr.mis.ui.splash

import com.ekr.mis.data.splash.ResponseSplash
import com.ekr.mis.utils.SessionManager

interface SplashContract {
    interface Presenter {
        fun doGetData(email:String,phone_number:String,lat_long:String)
        fun setSession(sessionManager: SessionManager,responseSplash: ResponseSplash)
    }

    interface View {
        fun initListener()
        fun onResult(responseSplash: ResponseSplash)
        fun showMessage(message:String)
        fun loading(loading : Boolean)
    }
}