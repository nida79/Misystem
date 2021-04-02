package com.ekr.mis.ui.home.info.organization

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekr.mis.data.info.DataInfo
import com.ekr.mis.data.info.ResponseInfo
import com.ekr.mis.databinding.FragmentInfoOranizationBinding
import com.ekr.mis.ui.home.info.InfoActivityDetail
import com.ekr.mis.ui.home.info.InfoAdapter
import com.ekr.mis.utils.SessionManager


class InfoFragmentOranization : Fragment(), OrganizationContract.View {
    private lateinit var infoAdapter: InfoAdapter
    private var _binding: FragmentInfoOranizationBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager
    private lateinit var manager: LinearLayoutManager
    private lateinit var presenter: OrgPresenter
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sessionManager = SessionManager(requireContext())
        presenter = OrgPresenter(this)
    }


    override fun onStart() {
        super.onStart()
        presenter.getNews(
            sessionManager.prefToken, "0923",
            sessionManager.prefRole.toInt(), sessionManager.prefLatlong,
            sessionManager.prefEmail
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoOranizationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun initListener() {
        infoAdapter = InfoAdapter(sessionManager.mainDefaultPath, arrayListOf())
        manager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.rcvHomeOrg.apply {
            layoutManager = manager
            adapter = infoAdapter
            setHasFixedSize(true)
        }
        binding.swpHomeOrg.setOnRefreshListener {
            binding.swpHomeOrg.isRefreshing = false
            presenter.getNews(
                sessionManager.prefToken, sessionManager.prefNohp,
                sessionManager.prefRole.toInt(), sessionManager.prefLatlong,
                sessionManager.prefEmail
            )
        }
        infoAdapter.setOnItemClickListener(object : InfoAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, data: DataInfo) {
                val intent = Intent(requireActivity(), InfoActivityDetail::class.java)
                intent.putExtra("data", data)
                requireActivity().startActivity(intent)
            }
        })
    }

    override fun loading(loading: Boolean) {
        when (loading) {
            true -> {
                binding.rcvHomeOrg.visibility = View.GONE
                binding.progressBarHomeOrg.visibility = View.VISIBLE
                binding.shimmerHomeOrg.visibility = View.VISIBLE
                binding.shimmerHomeOrg.startShimmer()
            }
            false -> {
                binding.rcvHomeOrg.visibility = View.VISIBLE
                binding.progressBarHomeOrg.visibility = View.GONE
                binding.shimmerHomeOrg.visibility = View.GONE
                binding.shimmerHomeOrg.stopShimmer()
            }
        }
    }

    override fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onResult(response: ResponseInfo) {
        response.dataInfo?.let { infoAdapter.setData(it) }
    }
}