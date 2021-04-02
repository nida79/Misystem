package com.ekr.mis.ui.home.info

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ekr.mis.data.info.DataInfo
import com.ekr.mis.databinding.ActivityInfoDetailBinding
import com.ekr.mis.utils.GlideHelper
import com.ekr.mis.utils.SessionManager

class InfoActivityDetail : AppCompatActivity() {
    private lateinit var dataInfo: DataInfo
    private lateinit var binding: ActivityInfoDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sessionManager = SessionManager(this)
        dataInfo = intent.extras!!.getParcelable("data")!!

        GlideHelper.setImage(
            this,
            sessionManager.mainDefaultPath + dataInfo.logoPath,
            binding.imgDetail
        )
        binding.tvTittleDetail.text = dataInfo.namaOrganisasi
        binding.tvEmailDetail.text = dataInfo.email
        binding.tvContactDetail.text = dataInfo.telpon
        binding.tvAlamatDetail.text = dataInfo.alamat
    }
}