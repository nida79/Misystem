package com.ekr.mis.ui.guest

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ekr.mis.databinding.ActivityChooseRoleBinding
import com.ekr.mis.ui.home.HomeGuestActivity
import com.ekr.mis.utils.SessionManager
import com.ekr.mis.utils.TagLog

class ChooseRoleActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityChooseRoleBinding
    private lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityChooseRoleBinding.inflate(layoutInflater)
        val view = _binding.root
        setContentView(view)
        sessionManager = SessionManager(this)
        _binding.cvPegawai.setOnClickListener {
            sessionManager.prefRole = TagLog.WORKER.toString()
            startActivity(Intent(this, HomeGuestActivity::class.java))
            finishAffinity()
            finish()
        }
        _binding.cvPelajar.setOnClickListener {
            sessionManager.prefRole = TagLog.STUDENT.toString()
            startActivity(Intent(this, HomeGuestActivity::class.java))
            finishAffinity()
            finish()
        }
    }
}