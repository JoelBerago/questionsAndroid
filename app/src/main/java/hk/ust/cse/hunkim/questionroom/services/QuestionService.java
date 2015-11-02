package hk.ust.cse.hunkim.questionroom.services;

import java.util.List;

import hk.ust.cse.hunkim.questionroom.question.Question;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Joel on 29/10/2015.
 */
public interface QuestionService {
    @FormUrlEncoded
    @POST("v1/question")
    Call<ErrorIdResponse> createQuestion(
            @Field("text") String text,
            @Field("imageURL") String imageURL,
            @Field("room") String room
    );

    @GET("v1/question")
    Call<List<Question>> getQuestions();

    @GET("v1/questions/room/{room}")
    Call<List<Question>> getQuestions(@Path("room") String room);

    @FormUrlEncoded
    @POST("v1/answer")
    Call<ErrorIdResponse> createAnswer(
            @Field("id") String id,
            @Field("text") String text,
            @Field("imageURL") String imageURL
    );

    @FormUrlEncoded
    @POST("v1/followup")
    Call<ErrorIdResponse> createFollowup(
            @Field("id") String id,
            @Field("text") String text,
            @Field("imageURL") String imageURL
    );

    @FormUrlEncoded
    @POST("v1/like")
    Call<ErrorIdResponse> addLike(
            @Field("id") String id,
            @Field("user") String user
    );
}
