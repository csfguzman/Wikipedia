package com.example.wikipedia.fragments


import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager

import com.example.wikipedia.R
import com.example.wikipedia.WikiApplication
import com.example.wikipedia.activities.SearchActivity
import com.example.wikipedia.adapters.ArticleCardRecyclerAdapter
import com.example.wikipedia.databinding.FragmentExploreBinding
import com.example.wikipedia.managers.WikiManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.coroutines.launch

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


        binding.exploreArticleRecycler!!.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)//LinearLayoutManager(context)
        binding.exploreArticleRecycler!!.adapter = adapter



        binding.refresher?.setOnRefreshListener {
            lifecycleScope.launch {
                getArticles()
            }
        }

        lifecycleScope.launch {
            getArticles()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            val channelId = getString(R.string.channel_id)
            val channelName = getString(R.string.channel_name)

            val notificationManager = activity?.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(
                NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW)
            )
        }

        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        activity?.intent?.extras?.let {
            for (key in it.keySet()) {
                val value = activity?.intent?.extras?.get(key)
                Log.d("intentStuff", "Key: $key Value: $value")
            }
        }

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("getInstanceIdFailed", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                // Log and toast
                val msg = getString(R.string.msg_token_fmt, token)
                Log.d("token", msg)
                //Toast.makeText(activity?.baseContext, msg, Toast.LENGTH_SHORT).show()
            })

        return binding.root
    }

    private fun getArticles(){
        binding.refresher?.isRefreshing = true

        try{

            wikiManager?.getRandom(15, { wikiResult ->
                adapter.currentResults.clear()
                adapter.currentResults.addAll(wikiResult.query!!.pages)

                wikiResult.query!!.pages.get(0).categories?.forEach{
                    Log.i("currentCategory", it.title)
                }

//                Log.i("categoryTotal", wikiResult.query!!.pages.get(0).categories?.get(0)?.title)
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
