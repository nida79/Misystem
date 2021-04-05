package com.ekr.mis.ui.guest.qrcode

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.budiyev.android.codescanner.*
import com.ekr.mis.R
import com.ekr.mis.databinding.FragmentQrScanBinding
import com.ekr.mis.ui.guest.register.RegisterFragment
import com.ekr.mis.utils.SentenceMessage
import com.ekr.mis.utils.SessionManager

class QrScanFragment : Fragment() {
    private lateinit var codeScanner: CodeScanner
    private var _binding: FragmentQrScanBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())

        setUpPermission()
        setUpScanner()
        binding.btnSendQr.setOnClickListener {
            val idQR = binding.tvResultQr.text.toString()
           if (idQR.isEmpty()){
               binding.tvResultQr.error = SentenceMessage.NULL_MESSAGE
               binding.tvResultQr.requestFocus()
           }else{
               sessionManager.prefIdQR = idQR
               val registerFragment = RegisterFragment()
               if (activity!=null){
                   activity?.supportFragmentManager?.beginTransaction()
                       ?.replace(R.id.fragment_container_guest, registerFragment)?.addToBackStack(null)
                       ?.commit()
               }
           }


        }
    }

    private fun setUpPermission() {
        val permission: Int = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }

    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.CAMERA),
            100
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            100 -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        requireContext(),
                        "Aktifkan Izin Camera untuk menggunakan scan QR",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun setUpScanner() {
        codeScanner = CodeScanner(requireActivity(), binding.scanQr)
        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS
            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false
            decodeCallback = DecodeCallback {
                activity?.runOnUiThread {
                    binding.tvResultQr.setText(it.text)
                }
            }
            errorCallback = ErrorCallback {
                activity?.runOnUiThread {
                    Log.e(SentenceMessage.TAG_ERROR, "Camera Scan Error: ${it.message}")
                }
            }

        }
        binding.scanQr.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQrScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}