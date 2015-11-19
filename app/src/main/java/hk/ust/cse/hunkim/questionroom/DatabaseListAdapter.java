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

    protected Context context;
    protected LayoutInflater inflater;
    protected List<T> mQuestionList;

    public DatabaseListAdapter(Context context, int mLayoutID, List<T> mQuestionList) {
        this.context = context;
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

    public abstract void push(final T basequestion, String baseID);

    public void add_like(final BaseQuestion question, final String user){
        QuestionService service=retrofit.create(QuestionService.class);

        Call<ErrorIdResponse> response=service.addLike(
            question.getId(),user
        );

        response.enqueue(new Callback<ErrorIdResponse>() {
            @Override
            public void onResponse(Response<ErrorIdResponse> response, Retrofit retrofit) {
                question.addLikes(user);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("QUESTIONROOM", "Failed at DatabaseListAdapter.add_like():", t);
            }
        });
    }

    public void pull(final String roomName) {
        QuestionService service = retrofit.create(QuestionService.class);
        Call<List<Question>> response;

        if (roomName.equals(""))
            response = service.getQuestions();
        else
            response = service.getQuestions(roomName);

        response.enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Response<List<Question>> response, Retrofit retrofit) {
                mQuestionList = (List<T>) response.body();
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("QUESTIONROOM", "Failed at DatabaseListAdapter.pull():", t);
            }
        });
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

    protected class Holder {
        Button likeButton;
        TextView numberOfLikes;
        TextView textView;
        ImageView iv;
        Button replyBtn;
    }

    protected void populateView(final Holder holder, final BaseQuestion baseQuestion) {
        /// SETUP LIKES
        // Map a Chat object to an entry in our listview
        List<String> likesArr = baseQuestion.getLikes();
        // check if we already clicked
        boolean clickable = !likesArr.contains(Integer.toString(((BaseActivity)context).getUserId()));
        holder.likeButton.setClickable(clickable);
        holder.likeButton.setEnabled(clickable);
        if (clickable) {
            holder.likeButton.getBackground().setColorFilter(null);
            holder.likeButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            add_like(baseQuestion, Integer.toString(((BaseActivity) context).getUserId()));
                            holder.likeButton.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                            holder.likeButton.getBackground().invalidateSelf();
                            holder.numberOfLikes.setText(Integer.toString(baseQuestion.getLikes().size() + 1));
                            holder.likeButton.setClickable(false);
                            holder.likeButton.setEnabled(false);
                        }
                    }
            );
        } else {
            holder.likeButton.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        }
        holder.likeButton.getBackground().invalidateSelf();

        if (holder.numberOfLikes != null)
            holder.numberOfLikes.setText(Integer.toString(likesArr.size()));


        /// SETUP TEXT
        holder.textView.setText(baseQuestion.getText());

        /// display image under text
        holder.iv.setImageBitmap(null);
        holder.iv.setImageDrawable(null);
        // only if URL exist
        if (!baseQuestion.getImageURL().equals("")) {
            Picasso.with(context)
                    .load(baseQuestion.getImageURL())
                    .resize(240, 140)   // image can stretch up to 240x140 max.
                    .centerInside()
                    .into(holder.iv);
            // upon clicking image view, pop up dialog
            holder.iv.setOnClickListener(new View.OnClickListener() {
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

    protected void setList(List<T> questionList) {
        this.mQuestionList = questionList;
        notifyDataSetChanged();
    }
}
