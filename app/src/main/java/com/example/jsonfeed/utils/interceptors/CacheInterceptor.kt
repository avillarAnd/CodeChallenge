package com.example.jsonfeed.utils.interceptors

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.example.jsonfeed.App
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

class CacheInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if(!isNetworkAvailable()){
            val cacheControl = CacheControl.Builder()
                .maxStale(7, TimeUnit.DAYS)
                .build()
            request = request.newBuilder()
                .removeHeader("Pragma")
                .removeHeader("Cache-Control")
                .cacheControl(cacheControl)
                .build()
        }
        return chain.proceed(request)
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = App.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val networkInfo = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                networkInfo.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                networkInfo.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                networkInfo.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                networkInfo.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            return networkInfo.isConnected
        }
    }
}