package hk.ust.cse.hunkim.questionroom;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import hk.ust.cse.hunkim.questionroom.question.Answer;
import hk.ust.cse.hunkim.questionroom.question.BaseQuestion;
import hk.ust.cse.hunkim.questionroom.question.FollowUp;
import hk.ust.cse.hunkim.questionroom.services.ErrorIdResponse;
import hk.ust.cse.hunkim.questionroom.services.QuestionService;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by J.T and Long on 10/31/2015.
 */
public class FollowupListAdapter extends DatabaseListAdapter<FollowUp> {
    private final Answer answer;

    public FollowupListAdapter(Context context, int textViewResourceId, Answer answer, List<FollowUp> followUpList) {
        super(context, textViewResourceId, followUpList);
        this.context = context;
        this.answer = answer;
        this.mQuestionList = answer.getFollow_ups();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        String tag;
        if (view == null) {
            if (i == 0)
                view = inflater.inflate(R.layout.questionfirst, viewGroup, false);
            else
                view = inflater.inflate(R.layout.followups, viewGroup, false);
        }

        if (i == 0) {
            populateView(view, answer);
            Button replyButton = (Button) view.findViewById(R.id.reply);
            replyButton.setVisibility(View.GONE);
            tag = answer.getId();
        }
        else {
            populateView(view, mQuestionList.get(i-1));
            tag = mQuestionList.get(i-1).getId();
        }

        view.setTag(tag);
        return view;
    }

    @Override
    public int getCount() {
        return super.getCount() + 1;
    }

    protected void populateView(final View view, final FollowUp followup) {
        /// SETUP TEXT
        TextView textView = (TextView) view.findViewById(R.id.head_desc);
        textView.setText(followup.getText());

        /// display image under text
        ImageView iv = (ImageView) view.findViewById(R.id.imageView);
        iv.setImageBitmap(null);
        iv.setImageDrawable(null);
        // only if URL exist
        if (!followup.getImageURL().equals("")) {
            Picasso.with(context)
                    .load(followup.getImageURL())
                    .resize(240, 140)   // image can stretch up to 240x140 max.
                    .centerInside()
                    .into(iv);
            // upon clicking image view, pop up dialog
            iv.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.e("Debug", "clicked");
                    //Toast.makeText(activity, question.getImageURL(), Toast.LENGTH_SHORT).show();
                    if (!followup.getImageURL().equals("")) {
                        Dialog dialog = new Dialog(context);
                        dialog.setTitle("View image");
                        dialog.setContentView(R.layout.imageview_dialog);
                        ImageView iv = (ImageView) dialog.findViewById(R.id.dialog_image);
                        iv.setImageBitmap(null);
                        Picasso.with(dialog.getContext())
                                .load(followup.getImageURL())
                                .into(iv);
                        dialog.show();
                    }
                }
            });
        }
    }

    @Override
    public void push(final FollowUp followUp, String baseID) {
        QuestionService service = retrofit.create(QuestionService.class);

        Call<ErrorIdResponse> response = service.createFollowup(
                baseID,
                followUp.getText(),
                followUp.getImageURL(),
                ((FollowupActivity) context).getUserId()
        );

        response.enqueue(new Callback<ErrorIdResponse>() {
            @Override
            public void onResponse(Response<ErrorIdResponse> response, Retrofit retrofit) {
                followUp.setId(response.body().id);
                mQuestionList.add(followUp);
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("QUESTIONROOM", "Failed at DatabaseListAdapter.push():", t);
            }
        });
    }

}
