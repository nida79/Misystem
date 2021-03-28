package com.ekr.mis.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.ekr.mis.R


class GlideHelper {
    companion object {

        fun setImage(context: Context, urlImage: String, imageView: ImageView){
            Glide .with(context)
                .load(urlImage)
                .centerCrop()
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(imageView)
        }
    }
}