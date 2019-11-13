package com.example.wikipedia.adapters

//import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wikipedia.R
import com.example.wikipedia.holders.CardHolder

/**
 * Created by alex on 10/25/17.
 */
class ArticleCardRecyclerAdapter() : RecyclerView.Adapter<CardHolder>() {

    override fun getItemCount(): Int {
        return 15 // temporary
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        var cardItem = LayoutInflater.from(parent?.context).inflate(R.layout.article_card_item, parent, false)
        return CardHolder(cardItem)
    }

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

    }

//    override fun onBindViewHolder(holder: CardHolder?, position: Int) {
//        // this is where we will update our view
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CardHolder {
//        var cardItem = LayoutInflater.from(parent?.context).inflate(R.layout.article_card_item, parent, false)
//        return CardHolder(cardItem)
//    }
}