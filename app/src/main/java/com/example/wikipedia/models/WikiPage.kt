package com.example.wikipedia.models

class WikiPage {
    var pageid: Int? = null
    var title: String? = null
    var fullurl: String? = null
    var thumbnail: WikiThumbnail? = null
    var categories: ArrayList<Category>? = null

    override fun toString(): String {
        var returnString = ""

        categories?.forEach{
            returnString += "${it.title},"
        }

        return returnString + "\n" +
                "pageId: $pageid" + "\n" +
                "title: $title" + "\n" +
                "fullurl: $fullurl"
    }
}