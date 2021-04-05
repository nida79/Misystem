package com.ekr.mis.ui.guest.register

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.ekr.mis.databinding.FragmentRegisterBinding
import com.ekr.mis.utils.DialogHelper
import com.ekr.mis.utils.SentenceMessage
import com.ekr.mis.utils.SessionManager
import com.ekr.mis.utils.Validator
import com.github.dhaval2404.imagepicker.ImagePicker
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.File


class RegisterFragment : Fragment(), RegisterContract.View {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager
    private lateinit var presenter: RegisterPresenter
    private lateinit var dialog: Dialog
    private var file: File? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = DialogHelper.globalLoading(requireActivity())
        sessionManager = SessionManager(requireContext())
        presenter = RegisterPresenter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sessionManager = SessionManager(requireContext())
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        if (sessionManager.prefIdQR.isNotEmpty()) {
            binding.idQrRegister.setText(sessionManager.prefIdQR)
        }
        binding.emailRegister.setText(sessionManager.prefEmail)
        return binding.root
    }

    override fun initListener() {


        binding.btnSubmitRegist.setOnClickListener {
            val religionCode = binding.spinnerRegis.selectedItemPosition + 1
            val email = binding.emailRegister.text.toString()
            val alamat = binding.alamatRegister.text.toString()
            val nama = binding.namaRegister.text.toString()
            val id_qr = binding.idQrRegister.text.toString()
            val kodepos = binding.kodeposRegister.text.toString()
            val agama = religionCode.toString()
            val telp = binding.nohpRegister.text.toString()
            val token = sessionManager.prefToken
            doValidate(email, alamat, nama, id_qr, kodepos, telp, agama, token, file)


        }
        binding.btnUploadImageRegist.setOnClickListener {
            doPermission()
        }
    }

    override fun onLoading(loading: Boolean) {
        when (loading) {
            true -> dialog.show()
            false -> dialog.dismiss()
        }
    }

    override fun showMessage(message: String) {
        if (activity != null) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }

    }

    override fun doPermission() {

        Dexter.withContext(requireContext())
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        ImagePicker.with(requireActivity())
                            .crop()
                            .compress(1024)
                            .maxResultSize(1080, 1080)
                            .start()
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

    override fun doValidate(
        email: String,
        alamat: String,
        nama: String,
        id_qr: String,
        kodepos: String,
        telp: String,
        agama: String,
        token: String,
        file: File?,
    ) {
        when {
            id_qr.isEmpty() -> {
                binding.idQrRegister.error = SentenceMessage.NULL_MESSAGE
                binding.idQrRegister.requestFocus()
            }
            nama.isEmpty() -> {
                binding.namaRegister.error = SentenceMessage.NULL_MESSAGE
                binding.namaRegister.requestFocus()
            }
            email.isEmpty() -> {
                binding.emailRegister.error = SentenceMessage.NULL_MESSAGE
                binding.emailRegister.requestFocus()
            }
            !Validator.validateEmail(email) -> {
                binding.emailRegister.error = "Format Email Tidak Sesuai"
                binding.emailRegister.requestFocus()
            }

            kodepos.isEmpty() -> {
                binding.kodeposRegister.error = SentenceMessage.NULL_MESSAGE
                binding.kodeposRegister.requestFocus()
            }
            telp.isEmpty() -> {
                binding.nohpRegister.error = SentenceMessage.NULL_MESSAGE
                binding.nohpRegister.requestFocus()
            }
            alamat.isEmpty() -> {
                binding.alamatRegister.error = SentenceMessage.NULL_MESSAGE
                binding.alamatRegister.requestFocus()
            }
            else -> {
                presenter.doRegister(file, email, token, nama, kodepos, telp, alamat, agama)
            }
        }


    }

    private fun showSettingsDialog() {
        val builder = AlertDialog.Builder(requireContext())
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
        val uri = Uri.fromParts("package", requireActivity().packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                val fileUri = data?.data
                binding.imgRegist.setImageURI(fileUri)
                binding.imgRegist.visibility = View.VISIBLE
                file = ImagePicker.getFile(data)!!
                showMessage(file!!.path.toString())
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT)
                    .show()
            }
            else -> {
                Toast.makeText(requireContext(), "Batal Ambil Gambar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance() = RegisterFragment()
    }
}