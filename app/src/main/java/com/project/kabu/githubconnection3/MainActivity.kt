package com.project.kabu.githubconnection3

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.project.kabu.githubconnection3.UI.Adapter.RepoListAdapter
import com.project.kabu.githubconnection3.Util.AppUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import retrofit2.HttpException
import java.util.*

class MainActivity() : AppCompatActivity(), RepoListAdapter.ItemClickListener, SwipeRefreshLayout.OnRefreshListener, KodeinAware {

    // Kodein
    override val kodein by closestKodein()
    val githubService: GithubService by instance()

    // 取得するユーザー名
    val username = "Airbnb"
    // リポジトリー一覧
    lateinit var repoList : List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // SwipeRefreshLayoutにリスナー設定
        swiperefresh.setOnRefreshListener(this)
        swiperefresh.isRefreshing = true

        // リポジトリー一覧取得開始
        if (judgeOnline()) {
            startGetRepo()
        }

    }

    override fun onItemClick(view: View, position: Int) {
        // readme表示開始
        if (judgeOnline()) {
            showDialog(repoList.get(position))
        }
    }

    override fun onRefresh() {
        // リポジトリー一覧再取得
        if (judgeOnline()) {
            startGetRepo()
        }
    }


    /**
     * リポジトリー一覧取得開始
     */
    fun startGetRepo() {
        githubService.getRepo(username)
                .map {
                    repository ->
                    repository.map {
                        (name) ->
                        name
                    }
                }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                    strings ->
                    // recyclerviewの設定
                    repoList = strings
                    mainRecyclerView.adapter = RepoListAdapter(this, this, strings)
                    mainRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                }, {
                    throwable ->
                    // エラー時の処理
                    processError(throwable)
                }, {
                    // SwipeRefreshLayoutの非表示
                    disabledSwiperefresh()
                })
    }

    /**
     * readme情報取得開始
     * @param repo リポジトリー名
     * @param webView Webviewクラス
     * @param alertDialog Dialogクラス
     */
    fun startGetReadme(repo: String, webView: WebView, alertDialog: Dialog) {
        // readmeの取得
        githubService.getReadme(username, repo)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( {
                    strings ->
                    // readmeのurlでロード
                    webView.loadUrl(strings.html_url)
                }, {
                    throwable ->
                    // エラー時の処理
                    processError(throwable)
                    // ダイアログの非表示
                    alertDialog.dismiss()
                }, {
                    // SwipeRefreshLayoutの非表示
                    disabledSwiperefresh()
                })
    }

    /**
     * readmeダイアログ表示
     * @param repo リポジトリー名
     */
    fun showDialog(repo : String) {
        val webView = AppUtil.settingWebview(this)
        val alertDialog =  AppUtil.settingDialog(this, webView)

        // readmeの取得
        startGetReadme(repo, webView, alertDialog)
    }

    /**
     * オンライン判定
     * @return オンラインの場合true
     */
    fun judgeOnline() : Boolean {
        if (AppUtil.isConnected(this)) return true
        processOffline()
        return false
    }

    /**
     * オフライン時の処理
     */
    fun processOffline() {
        Log.w("Warning#MainActivity", "Device is offline.")
        AppUtil.showSnackbar(mainRecyclerView,"オフラインです")
        disabledSwiperefresh()
    }

    /**
     * エラー時の処理
     * @param throwable エラー情報
     */
    fun processError(throwable: Throwable) {
        Log.e("Error#MainActivity", throwable.message)
        AppUtil.showSnackbar(mainRecyclerView,"エラーが発生しました")
    }

    /**
     * SwipeRefreshLayoutの非表示
     */
    fun disabledSwiperefresh() {
        if(swiperefresh.isRefreshing) {
            swiperefresh.isRefreshing = false
        }
    }

}
