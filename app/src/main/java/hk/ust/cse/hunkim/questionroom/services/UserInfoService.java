package hk.ust.cse.hunkim.questionroom.services;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by mikacuy on 11/16/15.
 */
public interface UserInfoService {
    @GET("v1/account/{userId}")
    Call<UserInfoResponse>getUserInfo(@Path("userId") int userId);
}
