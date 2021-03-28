package com.ekr.mis.utils

import android.annotation.SuppressLint
import android.widget.TextView

object CodeChangers {
    @SuppressLint("SetTextI18n")
    fun religionChanger(religion: Int, textView: TextView) {
        when (religion) {
            1 -> textView.text = "Islam"
            2 -> textView.text = "Kristen"
            3 -> textView.text = "Katolik"
            4 -> textView.text = "Hindu"
            5 -> textView.text = "Budha"
            6 -> textView.text = "Konguchu"
        }
    }

    @SuppressLint("SetTextI18n")
    fun genderChanger(gender: Int, textView: TextView){
        when (gender) {
            1 -> textView.text = "Laki - Laki"
            2 -> textView.text = "Perempuan"
        }
    }
}

