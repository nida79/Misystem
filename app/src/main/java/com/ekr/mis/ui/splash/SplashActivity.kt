package com.ekr.mis.ui.splash

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ekr.mis.data.splash.ResponseSplash
import com.ekr.mis.databinding.ActivitySplashBinding
import com.ekr.mis.ui.guest.ChooseRoleActivity
import com.ekr.mis.ui.home.HomeMemberActivity
import com.ekr.mis.utils.DialogHelper
import com.ekr.mis.utils.SentenceMessage
import com.ekr.mis.utils.SessionManager
import com.ekr.mis.utils.UserDataFetcher
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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
    private lateinit var dialogLoading: Dialog

    @SuppressLint("VisibleForTests", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = _binding.root
        setContentView(view)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        sessionManager = SessionManager(this)
        dialog = DialogHelper.splashPhoneNumber(this)
        dialogLoading = DialogHelper.globalLoading(this)
        presenter = SplashPresenter(this)
    }

    override fun onStart() {
        super.onStart()
        postData()
    }


    @SuppressLint("SetTextI18n")
    override fun initListener() {
        val manager = this.packageManager
        val info = manager.getPackageInfo(this.packageName, PackageManager.GET_ACTIVITIES)
        _binding.splashAppVer.text = "App V.${info.versionName}"
        dialog.splash_submit.setOnClickListener {
            requestPermission()
            if (dialog.edt_nohp_splash.text.toString().isEmpty()) {
                dialog.edt_nohp_splash.error = "Kolom Tidak Boleh Kosong"
                dialog.edt_nohp_splash.requestFocus()
            } else if (kordinat.isEmpty() || kordinat == "") {
                showMessage("Lokasi Belum Ditemukan Mohon Tunggu Beberapa Saat")
                requestPermission()
            } else {
                sessionManager.prefLatlong = kordinat
                sessionManager.prefEmail = email
                presenter.doGetData(
                    email,
                    dialog.edt_nohp_splash.text.toString(),
                    kordinat
                )
                dialog.dismiss()
            }

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
        responseSplash.let {
            presenter.setSession(
                sessionManager,
                responseSplash,
                kordinat
            )
        }
        if (responseSplash.status) {
            startActivity(Intent(this, HomeMemberActivity::class.java))
            finishAffinity()
            finish()
        } else {
            startActivity(Intent(this, ChooseRoleActivity::class.java))
            finishAffinity()
            finish()
        }


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
            if (message == SentenceMessage.ERROR_MESSAGE) {
                postData()
            }
        }
    }

    override fun loading(loading: Boolean) {
        when (loading) {
            true -> _binding.progressBarHorizontalSplash.visibility = View.VISIBLE
            false -> _binding.progressBarHorizontalSplash.visibility = View.GONE
        }
    }

    private fun postData() {
        requestPermission()
        if (sessionManager.prefNohp.isEmpty()) {
            dialog.show()
        } else {
            Handler(Looper.myLooper()!!).postDelayed(
                { _binding.splashProgress.performClick() },
                1000
            )
            _binding.splashProgress.setOnClickListener {
                kordinat = _binding.lokasiSplash.text.toString()
                presenter.doGetData(
                    sessionManager.prefEmail,
                    sessionManager.prefNohp,
                    kordinat
                )
            }

        }

    }

    override fun onResume() {
        super.onResume()
        when {
            sessionManager.prefNohp.isEmpty() -> {
                dialog.show()
            }
            kordinat == "" -> {
                UserDataFetcher.startLocationUpdates(
                    _binding.lokasiSplash,
                    fusedLocationProviderClient,
                    this@SplashActivity
                )
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

    private fun requestPermission() {
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.GET_ACCOUNTS,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
            .withListener(object : MultiplePermissionsListener {
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
                        showSettingsDialog()
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

    private fun showSettingsDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Perizinan Diperlukan")
        builder.setMessage("Aktifkan Perizinan untuk Melakukan Absensi")
        builder.setPositiveButton("BUKA PENGATURAN") { dialog: DialogInterface, _: Int ->
            dialog.cancel()
            openSetting()
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog: DialogInterface, _: Int -> dialog.cancel() }
        builder.show()
    }

    private fun openSetting() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }

    override fun onBackPressed() {
        finishAffinity()
        finish()
    }

}