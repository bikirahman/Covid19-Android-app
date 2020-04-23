package com.anupom.covidinfo.Api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {

    // get list of all corona patients
    @GET("api/maplist")
    Call<ResponseBody> getMapList(
            @Query("api_token") String tokenId
    );

    // get list of news
    @GET("api/liveupdates")
    Call<ResponseBody> getNewsList(
            @Query("api_token") String tokenId
    );

    @GET("api/latestnews")
    Call<ResponseBody> getLatestNews(
            @Query("api_token") String tokenId
    );

    @GET("api/helpline")
    Call<ResponseBody> getInformationData(
            @Query("api_token") String tokenId
    );

    @GET("api/graphdata")
    Call<ResponseBody> getGraphCall(
            @Query("api_token") String tokenId
    );

}
