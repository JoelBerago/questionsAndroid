package hk.ust.cse.hunkim.questionroom;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hk.ust.cse.hunkim.questionroom.db.DBHelper;
import hk.ust.cse.hunkim.questionroom.db.DBUtil;
import hk.ust.cse.hunkim.questionroom.db.ImageHelper;
import hk.ust.cse.hunkim.questionroom.question.Answer;
import hk.ust.cse.hunkim.questionroom.question.BaseQuestion;
import hk.ust.cse.hunkim.questionroom.question.FollowUp;
import hk.ust.cse.hunkim.questionroom.question.Question;
import hk.ust.cse.hunkim.questionroom.services.ErrorIdResponse;
import hk.ust.cse.hunkim.questionroom.services.ImageResponse;
import hk.ust.cse.hunkim.questionroom.services.PhotoService;
import hk.ust.cse.hunkim.questionroom.services.QuestionService;
import retrofit.Call;
import retrofit.Callback;
import retrofit.JacksonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Joel on 29/10/2015.
 */
public abstract class DatabaseListAdapter<T extends BaseQuestion> extends BaseAdapter {
    protected final static  String BASE_URL = "http://questions-backend.herokuapp.com/api/";
    protected final static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    protected DBUtil dbUtil;
    protected Context context;
    private int mLayoutID;
    private LayoutInflater inflater;
    protected List<T> mQuestionList;

    public DatabaseListAdapter(Context context, int mLayoutID, List<T> mQuestionList) {
        DBHelper mDbHelper = new DBHelper(context);
        dbUtil = new DBUtil(mDbHelper);

        this.context = context;
        this.mLayoutID = mLayoutID;
        this.mQuestionList = mQuestionList;

        inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void uploadPhoto(String picturePath, final T baseQuestion, final String baseID) {
        PhotoService service = retrofit.create(PhotoService.class);
        final Call<ImageResponse> responseURL = service.uploadPhoto(
                RequestBody.create(
                        MediaType.parse("text/plain"),
                        ImageHelper.compressFile(picturePath)));

        responseURL.enqueue(
                new Callback<ImageResponse>() {
                    @Override
                    public void onResponse(Response<ImageResponse> response, Retrofit retrofit) {
                        baseQuestion.setImageURL(response.body().Filepath);
                        push(baseQuestion, baseID);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.e("QuestionRoom", "uploadPhoto() failed at callback", t);
                    }
                }
        );
    }

//<<<<<<< HEAD
    public abstract void push(final T basequestion, String baseID);
//=======
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
                mQuestionList.add((T) question);
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
        final Question question = (Question) mQuestionList.get(questionIndex);

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

        final Question question = (Question) mQuestionList.get(questionIndex);
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
                    mQuestionList.add((T) q);
                }

                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("QUESTIONROOM", "Failed at DatabaseListAdapter.pull():", t);
            }
        });
    }
//>>>>>>> UI_enhancement



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
            view = inflater.inflate(mLayoutID, viewGroup, false);
        }

        // FIXME: Perhaps this is the first time to show data
        // Let's order the list
        if (i == 0) {
            sortModels(mQuestionList);
        }

        // Let's get keys and models
        final T basequestion = mQuestionList.get(i);

        // Call out to subclass to marshall this model into the provided view
        populateView(view, basequestion);
        view.setTag(basequestion.getId());
        return view;
    }

    protected void populateView(final View view, final T baseQuestion) {
        /// SETUP LIKES
        String[] likesArr = baseQuestion.getLikes();
        int likes = 0;
        if (likesArr != null)
            likes = likesArr.length;
        Button likeButton = (Button) view.findViewById(R.id.echo);
        likeButton.setText("" + likes);
        likeButton.setTextColor(Color.BLUE);
        likeButton.setTag(baseQuestion.getId()); // Set tag for button
        likeButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MainActivity m = (MainActivity) view.getContext();
                        m.updateLikes((Question) baseQuestion);
                    }
                }
        );
        // check if we already clicked
        /*
        boolean clickable = !dbUtil.contains(baseQuestion.getId());
        likeButton.setClickable(clickable);
        likeButton.setEnabled(clickable);
        // http://stackoverflow.com/questions/8743120/how-to-grey-out-a-button
        // grey out our button
        if (clickable) {
            likeButton.getBackground().setColorFilter(null);
        } else {
            likeButton.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        }
        */

        /// SETUP TEXT
        TextView textView = (TextView) view.findViewById(R.id.head_desc);
        textView.setText(baseQuestion.getText());

        /// display image under text
        ImageView iv = (ImageView) view.findViewById(R.id.imageView);
        iv.setImageBitmap(null);
        iv.setImageDrawable(null);
        // only if URL exist
        if (!baseQuestion.getImageURL().equals("")) {
            Picasso.with(context)
                    .load(baseQuestion.getImageURL())
                    .resize(240, 140)   // image can stretch up to 240x140 max.
                    .centerInside()
                    .into(iv);
            // upon clicking image view, pop up dialog
            iv.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.e("Debug", "clicked");
                    //Toast.makeText(activity, question.getImageURL(), Toast.LENGTH_SHORT).show();
                    if (!baseQuestion.getImageURL().equals("")) {
                        Dialog dialog = new Dialog(context);
                        dialog.setTitle("View image");
                        dialog.setContentView(R.layout.imageview_dialog);
                        ImageView iv = (ImageView) dialog.findViewById(R.id.dialog_image);
                        iv.setImageBitmap(null);
                        Picasso.with(dialog.getContext())
                                .load(baseQuestion.getImageURL())
                                .into(iv);
                        dialog.show();
                    }
                }
            });
        }
    }

    protected void sortModels(List<T> mModels) {
        Collections.sort(mModels);
    }
}
