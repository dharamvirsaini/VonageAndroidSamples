package com.example.vonageandroidsamples.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface APIService {

    @GET("session")
    Call<GetSessionResponse> getSession();

    @GET("session/47807831/{room}")
    Call<SessionResponse> getSessionCredentials(@Path("room") String room);
}
