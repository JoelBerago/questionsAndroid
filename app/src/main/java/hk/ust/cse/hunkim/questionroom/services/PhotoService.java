package hk.ust.cse.hunkim.questionroom.services;

import com.squareup.okhttp.RequestBody;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Streaming;

/**
 * Created by Joel on 28/10/2015.
 */
public interface PhotoService {
    @Multipart
    @POST("uploadphoto")
    Call<ImageResponse> uploadPhoto(
            @Part("image\"; filename=\"image.jpg") RequestBody userPhoto
    );
}
