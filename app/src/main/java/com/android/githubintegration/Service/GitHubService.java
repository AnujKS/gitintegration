package com.android.githubintegration.Service;

import com.android.githubintegration.BuildConfig;
import com.android.githubintegration.Util.NetworkUtil;
import com.android.githubintegration.model.UsersData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface GitHubService {

 @Headers({
         "Accept: application/json",
         "content-type: application/json"})
 @GET("users")
 Observable<UsersData> getUsers(@Query("q") String q,@Query("sort") String sort);

 class Creator {

  public static GitHubService getService() {
   Gson gson = new GsonBuilder()
           .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
           .create();
   Retrofit retrofit = new Retrofit.Builder()
           .baseUrl(BuildConfig.BASE_URL)
           .client(NetworkUtil.createClient())
           .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
           .addConverterFactory(GsonConverterFactory.create(gson))
           .build();
   return retrofit.create(GitHubService.class);
  }
 }

}
