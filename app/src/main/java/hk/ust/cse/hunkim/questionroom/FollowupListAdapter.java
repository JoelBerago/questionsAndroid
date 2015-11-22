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

import hk.ust.cse.hunkim.questionroom.db.UserHelper;
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
        Holder holder;

        if (view == null) {
            holder = new Holder();

            if (i == 0) {
                view = inflater.inflate(R.layout.questionfirst, viewGroup, false);
                holder.likeButton = (Button) view.findViewById(R.id.echo);
                holder.numberOfLikes = (TextView) view.findViewById(R.id.numberOfLikes);
                holder.textView = (TextView) view.findViewById(R.id.head_desc);
                holder.iv = (ImageView) view.findViewById(R.id.imageView);
                holder.replyBtn = (Button) view.findViewById(R.id.reply);
                holder.img_userCharacter=(ImageView)view.findViewById(R.id.questionUserCharacter);
                holder.txt_userClass=(TextView)view.findViewById(R.id.questionUserClass);
                holder.txt_userLvl=(TextView)view.findViewById(R.id.questionUserLvl);
            }
            else {
                view = inflater.inflate(R.layout.followups, viewGroup, false);
                holder.textView = (TextView) view.findViewById(R.id.head_desc);
                holder.iv = (ImageView) view.findViewById(R.id.imageView);
                holder.img_userCharacter=(ImageView)view.findViewById(R.id.questionUserCharacter);
                holder.txt_userClass=(TextView)view.findViewById(R.id.questionUserClass);
                holder.txt_userLvl=(TextView)view.findViewById(R.id.questionUserLvl);
            }

            view.setTag(holder);

        } else {
            holder = (Holder) view.getTag();
        }

        if (i == 0) {
            //set profile info
            int experience= answer.getExperience();
            int level= UserHelper.getLevel(experience);
            holder.txt_userClass.setText(UserHelper.getUserClass(level));
            holder.txt_userLvl.setText("Lvl "+String.valueOf(level));
            UserHelper.setCharacterImage(level,holder.img_userCharacter);

            holder.replyBtn.setVisibility(View.GONE);
            populateView(holder, answer);

        } else {
            //set profile info
            int experience= (mQuestionList.get(i-1)).getExperience();
            int level= UserHelper.getLevel(experience);
            holder.txt_userClass.setText(UserHelper.getUserClass(level));
            holder.txt_userLvl.setText("Lvl "+String.valueOf(level));
            UserHelper.setCharacterImage(level,holder.img_userCharacter);

            populateView(holder, mQuestionList.get(i-1));
        }
        return view;
    }

    protected void populateView(Holder holder, final FollowUp followUp) {
        /// SETUP TEXT
        holder.textView.setText(followUp.getText());

        /// display image under text
        holder.iv.setImageBitmap(null);
        holder.iv.setImageDrawable(null);
        // only if URL exist
        if (!followUp.getImageURL().equals("")) {
            Picasso.with(context)
                    .load(followUp.getImageURL())
                    .resize(240, 140)   // image can stretch up to 240x140 max.
                    .centerInside()
                    .into(holder.iv);
            // upon clicking image view, pop up dialog
            holder.iv.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.e("Debug", "clicked");
                    //Toast.makeText(activity, question.getImageURL(), Toast.LENGTH_SHORT).show();
                    if (!followUp.getImageURL().equals("")) {
                        Dialog dialog = new Dialog(context);
                        dialog.setTitle("View image");
                        dialog.setContentView(R.layout.imageview_dialog);
                        ImageView iv = (ImageView) dialog.findViewById(R.id.dialog_image);
                        iv.setImageBitmap(null);
                        Picasso.with(dialog.getContext())
                                .load(followUp.getImageURL())
                                .into(iv);
                        dialog.show();
                    }
                }
            });
        }
    }

    @Override
    public int getCount() {
        return super.getCount() + 1;
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
