package com.project.kabu.githubconnection3.UI

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.list_item.view.*

class RepoListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    // リストアイテムのレイアウト設定
    val itemTextView : TextView = view.itemTextView

}