package com.ekr.mis.ui.splash

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ekr.mis.data.splash.ResponseSplash
import com.ekr.mis.databinding.ActivitySplashBinding
import com.ekr.mis.ui.guest.ChooseRoleActivity
import com.ekr.mis.ui.home.HomeMemberActivity
import com.ekr.mis.utils.DialogHelper
import com.ekr.mis.utils.SessionManager
import com.ekr.mis.utils.UserDataFetcher
import com.google.android.gms.location.FusedLocationProviderClient
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.dialog_phone_number.*
import java.util.*


class SplashActivity : AppCompatActivity(), SplashContract.View {
    private lateinit var _binding: ActivitySplashBinding
    private lateinit var presenter: SplashPresenter
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var sessionManager: SessionManager
    private var kordinat = ""
    private var email = ""
    private lateinit var dialog: Dialog
    private lateinit var loading_dialog: Dialog

    @SuppressLint("VisibleForTests")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = _binding.root
        setContentView(view)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        sessionManager = SessionManager(this)
        dialog = DialogHelper.splashPhonenumber(this)
        loading_dialog = DialogHelper.globalLoading(this)
        presenter = SplashPresenter(this)
        fusedLocationProviderClient = FusedLocationProviderClient(this)


    }

    override fun onStart() {
        super.onStart()
        requestPermission()
        when {
            sessionManager.prefNohp.isEmpty() || sessionManager.prefNohp == "" -> {
                dialog.show()
            }
            kordinat == "" && email == "" -> {
                requestPermission()
            }
            kordinat != "" && email != "" && sessionManager.prefNohp != "" -> {
                presenter.doGetData(
                    email,
                    sessionManager.prefNohp,
                    kordinat
                )
            }
            else -> {
                requestPermission()
            }
        }
    }

    private fun requestPermission() {
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.GET_ACCOUNTS,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
            .withListener(object : MultiplePermissionsListener {
                @SuppressLint("MissingPermission")
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        //GET PRIMARY EMAIL
                        email = UserDataFetcher.getEmail(this@SplashActivity).toString()
                        // GET MY CURRENT LOCATION
                        kordinat = UserDataFetcher.getLocation(
                            _binding.lokasiSplash,
                            applicationContext
                        )

                    }

                    if (report.isAnyPermissionPermanentlyDenied) {
                        DialogHelper.showSettingsDialog(this@SplashActivity)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            })
            .withErrorListener {
                Log.e("Splash", "requestPermission: ${it.name}")
            }
            .onSameThread()
            .check()

    }

    @SuppressLint("SetTextI18n")
    override fun initListener() {
        val manager = this.packageManager
        val info = manager.getPackageInfo(this.packageName, PackageManager.GET_ACTIVITIES)
        _binding.splashAppVer.text = "App V.${info.versionName}"
        dialog.splash_submit.setOnClickListener {
            requestPermission()
            if (dialog.splash_phone_number.text.toString().isEmpty()) {
                dialog.splash_phone_number.error = "Kolom Tidak Boleh Kosong"
                dialog.splash_phone_number.requestFocus()
            } else if (kordinat.isEmpty() || kordinat == "") {
                showMessage("Lokasi Belum Ditemukan Mohon Tunggu Beberapa Saat")
                requestPermission()
            } else {
                presenter.doGetData(
                    email,
                    dialog.splash_phone_number.text.toString(),
                    kordinat
                )
                dialog.dismiss()
            }

        }
        dialog.splash_cancel_dialog.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setOnKeyListener { _, key, _ ->
            if (key == KeyEvent.KEYCODE_BACK) {
                finishAffinity()
                finish()
            }
            true
        }
    }

    override fun onResult(responseSplash: ResponseSplash) {
        responseSplash.let { presenter.setSession(sessionManager, responseSplash) }
        if (responseSplash.status) {
            startActivity(Intent(this, HomeMemberActivity::class.java))
            finishAffinity()
            finish()
        }
        startActivity(Intent(this, ChooseRoleActivity::class.java))
       /* if (responseSplash.dataUser.penilaian != null) {
            val json = JSONObject(responseSplash.dataUser.penilaian)
            val keys: Iterator<*> = json.keys()
            while (keys.hasNext()) {
                val kunci = keys.next() as String
                val isi = json.getString(kunci)
            }
        } */


    }

    override fun showMessage(message: String) {
        if (applicationContext != null) {
            Toast.makeText(
                applicationContext,
                message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun loading(loading: Boolean) {
        when (loading) {
            true -> _binding.progressBarHorizontalSplash.visibility = View.VISIBLE
            false -> _binding.progressBarHorizontalSplash.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        when {
            kordinat == "" -> {
                UserDataFetcher.startLocationUpdates(
                    _binding.lokasiSplash,
                    fusedLocationProviderClient,
                    this@SplashActivity
                )
            }
            sessionManager.prefNohp.isEmpty() -> {
                dialog.show()
            }
            kordinat != "" && sessionManager.prefNohp.isNotEmpty() -> {
                presenter.doGetData(
                    email,
                    sessionManager.prefNohp,
                    kordinat
                )
            }

        }

    }

    override fun onPause() {
        super.onPause()
        UserDataFetcher.stopLocationUpdates(fusedLocationProviderClient)
    }

    override fun onBackPressed() {
        finishAffinity()
        finish()
    }
}