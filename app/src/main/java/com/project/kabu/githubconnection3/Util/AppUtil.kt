package com.project.kabu.githubconnection3.Util

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.net.ConnectivityManager
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat.getSystemService
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_main.*

class AppUtil {
    companion object {

        /**
         * オンライン判定
         * @param context
         * @return オンラインの場合true
         */
        fun isConnected(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val newworkinfo = cm.activeNetworkInfo
            if (newworkinfo != null) return cm.activeNetworkInfo.isConnected
            return false
        }

        /**
         * webviewの設定
         * @param context
         * @return Webviewクラス
         */
        @SuppressLint("SetJavaScriptEnabled")
        fun settingWebview(context: Context) : WebView {
            val webView = WebView(context)
            val settings = webView.settings
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            webView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest): Boolean {
                    view?.loadUrl(request.url.toString())
                    return true
                }
            }
            return webView
        }

        /**
         * Snackbarの表示
         * @param view
         * @param message
         */
        fun showSnackbar(view: View, message : String) {
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction("Action", null).show()
        }

        /**
         * dialogの設定
         * @param context
         * @param view
         * @return Dialog
         */
        fun settingDialog(context: Context, view: View) : Dialog {
            val alertDialog = AlertDialog.Builder(context)
            alertDialog.setView(view)
            val ad = alertDialog.show()
            return ad
        }
    }
}