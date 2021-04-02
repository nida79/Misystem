package com.ekr.mis.ui.home.info.organization

import com.ekr.mis.data.info.ResponseInfo

interface OrganizationContract {
    interface Presenter {
        fun getNews(token: String, telpon: String, mode: Int, latlong: String, email: String)
    }

    interface View {
        fun initListener()
        fun loading(loading: Boolean)
        fun showMessage(message: String)
        fun onResult(response: ResponseInfo)
    }
}