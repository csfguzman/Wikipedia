package com.example.wikipedia.fragments


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.StaggeredGridLayoutManager

import com.example.wikipedia.R
import com.example.wikipedia.WikiApplication
import com.example.wikipedia.adapters.ArticleCardRecyclerAdapter
import com.example.wikipedia.databinding.FragmentFavoritesBinding
import com.example.wikipedia.managers.WikiManager
import com.example.wikipedia.models.WikiPage
import org.jetbrains.anko.doAsync

/**
 * A simple [Fragment] subclass.
 */
class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding

    private var wikiManager: WikiManager? = null
    private val adapter: ArticleCardRecyclerAdapter = ArticleCardRecyclerAdapter()

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        wikiManager = (activity?.applicationContext as WikiApplication).wikiManager
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate<FragmentFavoritesBinding>(inflater,
            R.layout.fragment_favorites,container,false)

        binding.favoritesArticleRecycler!!.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)//StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.favoritesArticleRecycler!!.adapter = adapter

        return binding.root
    }

    override fun onResume(){
        super.onResume()

        doAsync {
            val favoriteArticles = wikiManager!!.getFavorites()
            adapter.currentResults.clear()
            adapter.currentResults.addAll(favoriteArticles as ArrayList<WikiPage>)
            activity?.runOnUiThread{ adapter.notifyDataSetChanged() }
        }
    }

}// Required empty public constructor
