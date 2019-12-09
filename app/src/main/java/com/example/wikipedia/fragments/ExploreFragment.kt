package com.example.wikipedia.fragments


import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.wikipedia.R
import com.example.wikipedia.WikiApplication
import com.example.wikipedia.activities.SearchActivity
import com.example.wikipedia.adapters.ArticleCardRecyclerAdapter
import com.example.wikipedia.databinding.FragmentExploreBinding
import com.example.wikipedia.managers.WikiManager

/**
 * A simple [Fragment] subclass.
 */
class ExploreFragment : Fragment() {

    private var wikiManager: WikiManager? = null
    var adapter: ArticleCardRecyclerAdapter = ArticleCardRecyclerAdapter()
    private lateinit var binding : FragmentExploreBinding

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        wikiManager = (activity?.applicationContext as WikiApplication).wikiManager
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate<FragmentExploreBinding>(inflater,
            R.layout.fragment_explore,container,false)

        binding.searchCardView!!.setOnClickListener{
            val searchIntent = Intent(context, SearchActivity::class.java)
            context?.startActivity(searchIntent)
        }


        binding.exploreArticleRecycler!!.layoutManager = LinearLayoutManager(context)
        binding.exploreArticleRecycler!!.adapter = adapter



        binding.refresher?.setOnRefreshListener {
            getArticles()
        }

        getArticles()

        return binding.root
    }

    private fun getArticles(){
        binding.refresher?.isRefreshing = true

        try{

            wikiManager?.getRandom(15, { wikiResult ->
                adapter.currentResults.clear()
                adapter.currentResults.addAll(wikiResult.query!!.pages)
                activity?.runOnUiThread {
                    adapter.notifyDataSetChanged()
                    binding.refresher?.isRefreshing = false
                }
            })
        }
        catch (ex :Exception){

            // show the error alert
            val builder = AlertDialog.Builder(activity)
            builder.setMessage(ex.message).setTitle("Error!")
            val dialog = builder.create()
            dialog.show()
        }
    }


}
