package com.project.kabu.githubconnection3.UI.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.project.kabu.githubconnection3.R
import com.project.kabu.githubconnection3.UI.RepoListViewHolder

class RepoListAdapter(private val context: Context,
                      private val itemClickListener: ItemClickListener,
                      private val itemList:List<String>) : RecyclerView.Adapter<RepoListViewHolder>() {


    /** リストタップ処理用のインターフェース */
    interface ItemClickListener {
        fun onItemClick(view: View, position: Int)
    }


    /** RecyclerViewのアダプター設定 */
    private var mRecyclerView : RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        // RecyclerViewの設定
        mRecyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        // RecyclerViewの破棄
        mRecyclerView = null
    }

    override fun onBindViewHolder(holder: RepoListViewHolder, position: Int) {
        // データをビューに設定
        holder.itemTextView.text = itemList.get(position)
    }

    override fun getItemCount(): Int {
        // アダプタが保持するデータの合計数
        return itemList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoListViewHolder {
        // レイアウトの設定
        val layoutInflater = LayoutInflater.from(context)
        val mView = layoutInflater.inflate(R.layout.list_item, parent, false)

        // リスト項目にオンクリックリスナー設定
        mView.setOnClickListener {
            view -> mRecyclerView?.let {
                itemClickListener.onItemClick(view, it.getChildAdapterPosition(view))
            }
        }

        return RepoListViewHolder(mView)
    }

}