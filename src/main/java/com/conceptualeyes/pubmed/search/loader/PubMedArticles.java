package com.conceptualeyes.pubmed.search.loader;

import com.conceptualeyes.pubmed.models.PubmedArticle;

import java.util.List;

class PubMedArticles {
    final String name;
    final List<PubmedArticle> list;

    PubMedArticles(String name, List<PubmedArticle> list) {
        this.name = name;
        this.list = list;
    }

    @Override
    public String toString() {
        return "PubMedArticles{" +
                "name='" + name + '\'' +
                ", list.length=" + String.valueOf(list.size()) +
                '}';
    }
}
