package com.ekr.mis.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ekr.mis.R
import com.ekr.mis.databinding.ActivityHomeGuestBinding
import com.ekr.mis.ui.guest.qrcode.QrScanFragment
import com.ekr.mis.ui.guest.register.RegisterFragment
import com.ekr.mis.ui.setting.SettingFragment


class HomeGuestActivity : AppCompatActivity() {
    var exit: Long = 0
    private lateinit var _binding: ActivityHomeGuestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeGuestBinding.inflate(layoutInflater)
        val view = _binding.root
        setContentView(view)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container_guest,
                HomeFragment()
            ).commit()
        }
        _binding.animatedBottomBar.setItemSelected(R.id.navigation_home_guest)
        fragmentChanged()
    }

    private fun fragmentChanged() {

        _binding.animatedBottomBar.setOnItemSelectedListener { id ->
            var selectedFragment: Fragment? = null
            when (id) {
                R.id.navigation_home_guest -> selectedFragment = HomeFragment()
                R.id.navigation_scan_guest -> selectedFragment = QrScanFragment()
                R.id.navigation_regist_guest -> selectedFragment = RegisterFragment()
                R.id.navigation_setting_guest -> selectedFragment = SettingFragment()
            }
            selectedFragment?.let {
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_container_guest,
                    it,
                ).commit()

            }
            return@setOnItemSelectedListener
        }
    }

    override fun onBackPressed() {
        if ((System.currentTimeMillis() - exit) > 2000) {
            Toast.makeText(applicationContext, "Tekan Sekali Lagi Untuk Keluar", Toast.LENGTH_SHORT)
                .show()
            exit = System.currentTimeMillis()
        } else {
            finishAffinity()
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }
}