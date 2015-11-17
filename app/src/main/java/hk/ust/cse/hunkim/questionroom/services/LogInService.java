package hk.ust.cse.hunkim.questionroom.services;

/**
 * Created by mikacuy on 11/15/15.
 */

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

public interface LogInService {
    @FormUrlEncoded
    @POST("v1/account/login")
    Call<LogInResponse> loginRequest(
            @Field("email") String email,
            @Field("password") String password
    );




}
