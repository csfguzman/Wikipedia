package com.example.wikipedia

import android.app.Application
import com.example.wikipedia.managers.WikiManager
import com.example.wikipedia.providers.ArticleDataProvider
import com.example.wikipedia.repositories.ArticleDatabaseOpenHelper
import com.example.wikipedia.repositories.FavoritesRepository
import com.example.wikipedia.repositories.HistoryRepository

class WikiApplication: Application() {
    private var dbHelper: ArticleDatabaseOpenHelper? = null
    private var favoritesRepository: FavoritesRepository? = null
    private var historyRepository: HistoryRepository? = null
    private var wikiProvider: ArticleDataProvider? = null
    var wikiManager: WikiManager? = null
        private set

    override fun onCreate() {
        super.onCreate()

        dbHelper = ArticleDatabaseOpenHelper(applicationContext)
        favoritesRepository = FavoritesRepository(dbHelper!!)
        historyRepository = HistoryRepository(dbHelper!!)
        wikiProvider = ArticleDataProvider()
        wikiManager = WikiManager(wikiProvider!!, favoritesRepository!!, historyRepository!!)
    }
}