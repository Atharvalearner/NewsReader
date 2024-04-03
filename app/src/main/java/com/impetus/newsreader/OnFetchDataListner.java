package com.impetus.newsreader;

import com.impetus.newsreader.Models.NewsHeadlines;

import java.util.List;

public interface OnFetchDataListner<NewsApiResponse> {
    void onFetchData(List<NewsHeadlines> list,String message);
    void onError(String message);
}
