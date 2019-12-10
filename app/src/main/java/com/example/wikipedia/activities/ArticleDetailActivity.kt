package com.example.wikipedia.activities

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.wikipedia.R
import com.example.wikipedia.WikiApplication
import com.example.wikipedia.databinding.ActivityArticleDetailBinding
import com.example.wikipedia.managers.WikiManager
import com.example.wikipedia.models.WikiPage
import com.example.wikipedia.models.WikiResult
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_article_detail.*
import kotlinx.coroutines.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import java.net.URL
import java.nio.charset.Charset
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.util.*
import kotlin.collections.ArrayList

class ArticleDetailActivity: AppCompatActivity(), TextToSpeech.OnInitListener {

    lateinit var binding: ActivityArticleDetailBinding
    private var wikiManager: WikiManager? = null
    private var currentPage: WikiPage? = null
    private var speakButton: Button? = null
    lateinit var tts: TextToSpeech
    lateinit var textToRead: String
    lateinit var activityJob:Job
    lateinit var uiScope: CoroutineScope
    lateinit var ioScope: CoroutineScope
    private var ttsReady: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_article_detail)

        speakButton = binding.speak
        speakButton?.setOnClickListener{speakText(it)}

        tts = TextToSpeech(this, this)

        wikiManager = (applicationContext as WikiApplication).wikiManager

        //setSupportActionBar(toolbar);
        supportActionBar!!.setDisplayHomeAsUpEnabled(true);

        // get the page from the extras
        val wikiPageJson = intent.getStringExtra("page")

        textToRead = ""

        currentPage = Gson().fromJson<WikiPage>(wikiPageJson, WikiPage::class.java)

        activityJob = Job()

        //ioScope = CoroutineScope(Dispatchers.IO + activityJob)
        uiScope = CoroutineScope(Dispatchers.Main + activityJob)

        uiScope.launch{
            textToRead = extractTextFromPage()

            if(textToRead.isNotEmpty() && ttsReady)
                speakButton?.isEnabled = true

            addHistory()
        }


        supportActionBar?.title = currentPage?.title


        article_detail_webview?.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return true
            }

        }

        article_detail_webview.loadUrl(currentPage!!.fullurl)


    }

    private fun speakText(view: View){
        tts.speak(textToRead, TextToSpeech.QUEUE_FLUSH, null,"")

//        uiScope.launch{
//            textToRead = extractTextFromPage()
//
//            if(textToRead.isNotEmpty()){
//                tts.speak(textToRead, TextToSpeech.QUEUE_FLUSH, null,"")
//            }
//            else{
//                tts.speak("nothing to see here guys!", TextToSpeech.QUEUE_FLUSH, null,"")
//            }
//        }
    }


    private suspend fun extractTextFromPage(): String{
        return withContext(Dispatchers.IO) {
            var stringReturned = ""

            val doc:Document = Jsoup.connect(currentPage?.fullurl).get()

            var pElements: Elements = doc.select("p")
            Log.i("elements", pElements.size.toString())

            for (element in pElements) {
                stringReturned = stringReturned + element.text()
            }

//            if(stringReturned.isNotEmpty()){
//                Toast.makeText(this@ArticleDetailActivity,"text to read good to go",Toast.LENGTH_SHORT)
//            }
//            else{
//                Toast.makeText(this@ArticleDetailActivity,"oh shit",Toast.LENGTH_SHORT)
//            }

            stringReturned
        }


    }

    private suspend fun addHistory(){
        withContext(Dispatchers.IO){
            wikiManager?.addHistory(currentPage!!)
        }
    }

    override fun onInit(status: Int) {

        if (status == TextToSpeech.SUCCESS) {
            // set US English as language for tts
            val result = tts.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS","The Language specified is not supported!")
            } else {
                //speakButton?.isEnabled = true
                ttsReady = true
            }

        } else {
            Log.e("TTS", "Initilization Failed!")
        }

    }

    override fun onDestroy() {
        // Shutdown TTS
        if (tts != null) {
            tts.stop()
            tts.shutdown()
        }

        activityJob.cancel()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.article_menu, menu);
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item!!.itemId == android.R.id.home){
            finish()
        }
        else if (item!!.itemId == R.id.action_favorite){
            try {
                // determine if article is already a favorite or not
                if(wikiManager!!.getIsFavorite(currentPage!!.pageid!!)){
                    wikiManager!!.removeFavorite(currentPage!!.pageid!!)
                    toast("Article removed from favorites")
                }
                else{
                    wikiManager!!.addFavorite(currentPage!!)
                    toast("Article added to favorites")
                }
            }
            catch (ex: Exception){
                toast("Unable to update this article.")
            }
        }
        return true;
    }
}