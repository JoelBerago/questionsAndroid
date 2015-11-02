package hk.ust.cse.hunkim.questionroom;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.util.ArrayList;
import java.util.List;

import hk.ust.cse.hunkim.questionroom.db.DBHelper;
import hk.ust.cse.hunkim.questionroom.db.DBUtil;
import hk.ust.cse.hunkim.questionroom.db.ImageHelper;
import hk.ust.cse.hunkim.questionroom.question.Answer;
import hk.ust.cse.hunkim.questionroom.question.FollowUp;
import hk.ust.cse.hunkim.questionroom.question.Question;
import hk.ust.cse.hunkim.questionroom.services.ErrorIdResponse;
import hk.ust.cse.hunkim.questionroom.services.ImageResponse;
import hk.ust.cse.hunkim.questionroom.services.QuestionService;
import hk.ust.cse.hunkim.questionroom.services.PhotoService;
import retrofit.Call;
import retrofit.Callback;
import retrofit.JacksonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Joel on 29/10/2015.
 */
public abstract class DatabaseListAdapter extends BaseAdapter {
    protected Context context;
    private static final String BASE_URL = "http://questions-backend.herokuapp.com/api/";
    private final static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    protected DBUtil dbUtil;
    private int mLayoutID;
    private List<Question> mQuestionList;

    public DatabaseListAdapter(Context context, int mLayoutID) {
        DBHelper mDbHelper = new DBHelper(context);
        dbUtil = new DBUtil(mDbHelper);

        this.context = context;
        this.mLayoutID = mLayoutID;
        this.mQuestionList = new ArrayList<Question>();
    }

    public void uploadPhoto(String picturePath, final Question question) {
        PhotoService service = retrofit.create(PhotoService.class);
        final Call<ImageResponse> responseURL = service.uploadPhoto(
                RequestBody.create(
                        MediaType.parse("text/plain"),
                        ImageHelper.compressFile(picturePath)));

        responseURL.enqueue(
                new Callback<ImageResponse>() {
                    @Override
                    public void onResponse(Response<ImageResponse> response, Retrofit retrofit) {
                        question.setImageURL(response.body().Filepath);
                        push(question);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.e("QuestionRoom", "uploadPhoto() failed at callback", t);
                    }
                }
        );
    }

    //---------add like & push to database
    public void add_like(final Question question, final String user){
        QuestionService service=retrofit.create(QuestionService.class);

        Call<ErrorIdResponse> response=service.addLike(
            question.getId(),user
        );

        Log.i("add_like","ADDED");
        Log.i("add_like",question.getId());
        Log.i("add_like",user);

        response.enqueue(new Callback<ErrorIdResponse>() {
            @Override
            public void onResponse(Response<ErrorIdResponse> response, Retrofit retrofit) {
                question.addLikes(user);
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("QUESTIONROOM", "Failed at DatabaseListAdapter.add_like():", t);
            }
        });

    }
    //--------------------------------------

    public void push(final Question question) {
        QuestionService service = retrofit.create(QuestionService.class);

        Call<ErrorIdResponse> response = service.createQuestion(
                question.getText(),
                question.getImageURL(),
                question.getRoom()
        );

        response.enqueue(new Callback<ErrorIdResponse>() {
            @Override
            public void onResponse(Response<ErrorIdResponse> response, Retrofit retrofit) {
                question.setId(response.body().id);
                mQuestionList.add(question);
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("QUESTIONROOM", "Failed at DatabaseListAdapter.push():", t);
            }
        });
    }

    public void push(final int questionIndex, final Answer answer) {
        QuestionService service = retrofit.create(QuestionService.class);
        final Question question = mQuestionList.get(questionIndex);

        // TODO: Check if question is null?
        Call<ErrorIdResponse> response = service.createQuestion(
                question.getId(),
                answer.getText(),
                answer.getImageURL()
        );

        response.enqueue(new Callback<ErrorIdResponse>() {
            @Override
            public void onResponse(Response<ErrorIdResponse> response, Retrofit retrofit) {
                answer.setId(response.body().id);
                question.addAnswer(answer);
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("QUESTIONROOM", "Failed at DatabaseListAdapter.push():", t);
            }
        });
    }

    public void push(int questionIndex, final int answerIndex, final FollowUp followup) {
        QuestionService service = retrofit.create(QuestionService.class);

        final Question question = mQuestionList.get(questionIndex);
        Call<ErrorIdResponse> response = service.createQuestion(
                question.getAnswer(answerIndex).getId(),
                followup.getText(),
                followup.getImageURL()
        );

        response.enqueue(new Callback<ErrorIdResponse>() {
            @Override
            public void onResponse(Response<ErrorIdResponse> response, Retrofit retrofit) {
                followup.setId(response.body().id);
                question.addFollowup(answerIndex, followup);
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("QUESTIONROOM", "Failed at DatabaseListAdapter.push():", t);
            }
        });
    }



    public void pull(final String roomName) {
        QuestionService service = retrofit.create(QuestionService.class);
        Call<List<Question>> response;

        if (roomName == "")
            response = service.getQuestions();
        else
            response = service.getQuestions(roomName);

        response.enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Response<List<Question>> response, Retrofit retrofit) {
                for (Question q: response.body()) {
                    mQuestionList.add(q);
                }

                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("QUESTIONROOM", "Failed at DatabaseListAdapter.pull():", t);
            }
        });
    }



    public void cleanup() {
        mQuestionList.clear();
    }

    @Override
    public int getCount() {
        return mQuestionList.size();
    }

    @Override
    public Object getItem(int i) {
        return mQuestionList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getViewTypeCount() {

        if (getCount() != 0)
            return getCount();

        return 1;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = ((Activity)context).getLayoutInflater().inflate(mLayoutID, viewGroup, false);
        }

        // FIXME: Perhaps this is the first time to show data
        // Let's order the list
        if (i == 0) {
            sortModels(mQuestionList);
        }
        // Let's get keys and models
        Question question = mQuestionList.get(i);

        // Call out to subclass to marshall this model into the provided view
        populateView(view, question);
        return view;
    }

    protected abstract void populateView(View view, Question question);

    protected abstract void sortModels(List<Question> mModels);
}
