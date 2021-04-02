package com.ekr.mis.utils

import android.accounts.Account
import android.accounts.AccountManager
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.os.Looper
import android.util.Log
import android.widget.TextView
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task


class UserDataFetcher {
    companion object{
        fun getEmail(context: Context?): String? {
            val accountManager = AccountManager.get(context)
            val account = getAccount(accountManager)
            return account?.name
        }

        private fun getAccount(accountManager: AccountManager): Account? {
            val accounts = accountManager.getAccountsByType("com.google")
            return if (accounts.isNotEmpty()) {
                accounts[0]
            } else {
                null
            }
        }

        @SuppressLint("MissingPermission", "SetTextI18n")
        fun getLocation(textView: TextView, context: Context): String {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            fusedLocationClient.lastLocation.addOnSuccessListener {
                if (it != null) {textView.text = "${it.latitude},${it.longitude}"}
            }
            return textView.text.toString()

        }

        @SuppressLint("MissingPermission")
        fun startLocationUpdates(
            textView: TextView,
            fusedLocationClient: FusedLocationProviderClient,
            context: Activity
        ) {
            val locationRequest = LocationRequest.create().apply {
                interval = 10000
                fastestInterval = 5000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            val locationCallback = object : LocationCallback() {
                @SuppressLint("SetTextI18n")
                override fun onLocationResult(locationResult: LocationResult?) {
                    locationResult ?: return
                    for (location in locationResult.locations) {
                        textView.text = "${location.latitude},${location.longitude}"
                    }
                }
            }
            val locationSettingsRequest =
                LocationSettingsRequest.Builder().addLocationRequest(locationRequest).build()
            val client = LocationServices.getSettingsClient(context)
            val task: Task<LocationSettingsResponse> =
                client.checkLocationSettings(locationSettingsRequest)

            task.addOnSuccessListener {
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }.addOnFailureListener {
                if (it is ResolvableApiException) {
                    try {

                        // and check the result in onActivityResult().
                        it.startResolutionForResult(
                            context,
                            17
                        )
                    } catch (sendEx: IntentSender.SendIntentException) {
                        Log.e("Lokasi Error", "startLocationUpdates: ${sendEx.message}")
                    }
                }
            }
        }

        fun stopLocationUpdates(fusedLocationClient: FusedLocationProviderClient) {
            val locationCallback = object : LocationCallback() {}
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }


}