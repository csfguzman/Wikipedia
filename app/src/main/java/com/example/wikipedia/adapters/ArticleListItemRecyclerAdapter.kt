package com.example.wikipedia.activities.adapters

//import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wikipedia.R
import com.example.wikipedia.holders.ListItemHolder

//import org.alexdunn.wikipedia.R

/**
 * Created by alex on 10/25/17.
 */
class ArticleListItemRecyclerAdapter() : RecyclerView.Adapter<ListItemHolder>() {

    override fun getItemCount(): Int {
        return 15 // temporary
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemHolder {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        var cardItem = LayoutInflater.from(parent?.context).inflate(R.layout.article_list_item, parent, false)
        return ListItemHolder(cardItem)
    }

    override fun onBindViewHolder(holder: ListItemHolder, position: Int) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

//    override fun onBindViewHolder(holder: ListItemHolder?, position: Int) {
//        // this is where we will update our view
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ListItemHolder {
//        var cardItem = LayoutInflater.from(parent?.context).inflate(R.layout.article_list_item, parent, false)
//        return ListItemHolder(cardItem)
//    }
}