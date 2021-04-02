package com.ekr.mis.ui.home.info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ekr.mis.data.info.DataInfo
import com.ekr.mis.databinding.ItemNewsBinding
import com.ekr.mis.utils.GlideHelper
import kotlinx.android.synthetic.main.item_news.view.*

class InfoAdapter(val baseUrl: String, private var data: ArrayList<DataInfo>) :
    RecyclerView.Adapter<InfoAdapter.ViewHolder>() {
    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int, data: DataInfo)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        notifyDataSetChanged()
        mListener = listener
    }

    fun setData(firstResult: List<DataInfo>) {
        data.clear()
        data.addAll(firstResult)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemNewsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), mListener
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position], baseUrl)
    }

    override fun getItemCount() = data.size

    class ViewHolder(
        binding: ItemNewsBinding,
        private val listener: OnItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dataInfo: DataInfo, baseUrl: String) {
            val position = adapterPosition
            val imageUrl = baseUrl + dataInfo.iconPath
            with(itemView) {
                GlideHelper.setImage(context, imageUrl, img_item_news)
                tv_title_item_news.text = dataInfo.namaOrganisasi
                tv_desc_item_news.text = dataInfo.alamat
                setOnClickListener {
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position, dataInfo)
                    }

                }
            }

        }

    }
}