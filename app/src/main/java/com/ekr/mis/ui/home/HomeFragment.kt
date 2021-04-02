package com.ekr.mis.ui.home

import android.annotation.SuppressLint
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ekr.mis.databinding.FragmentHomeBinding
import com.ekr.mis.ui.home.info.general.InfoFragmentGeneral
import com.ekr.mis.ui.home.info.organization.InfoFragmentOranization
import com.ekr.mis.utils.SessionManager
import com.ekr.mis.utils.TagLog
import java.util.*


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private var lokasi =""
    @SuppressLint("VisibleForTests")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(requireContext())
        var textHeader = ""
        when (sessionManager.prefRole) {
            TagLog.STUDENT.toString() -> textHeader = "Info Sekolah"
            TagLog.WORKER.toString() -> textHeader = "Info Organisasi"
        }
        val lalo = sessionManager.prefLatlong
        val geoCoder = Geocoder(requireActivity(), Locale.getDefault())
        val address = geoCoder.getFromLocation(lalo.split(",")[0].toDouble(),
            lalo.split(",")[1].toDouble(), 1)
        lokasi = address[0].getAddressLine(0)
        viewPagerAdapter = ViewPagerAdapter(childFragmentManager)
        viewPagerAdapter.addFragment(InfoFragmentGeneral(), "Info Umum")
        viewPagerAdapter.addFragment(InfoFragmentOranization(), textHeader)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.tvHeaderGuest.text = "Selamat Datang,"
        binding.tvNameMain.text = "Management Information System"
        binding.tvLocationHome.text  = lokasi
        binding.vpNews.adapter = viewPagerAdapter
        binding.tabLayoutHome.setupWithViewPager(binding.vpNews)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}