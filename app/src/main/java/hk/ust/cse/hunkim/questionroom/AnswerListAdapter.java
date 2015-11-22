package hk.ust.cse.hunkim.questionroom;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import hk.ust.cse.hunkim.questionroom.question.Answer;
import hk.ust.cse.hunkim.questionroom.question.BaseQuestion;
import hk.ust.cse.hunkim.questionroom.question.FollowUp;
import hk.ust.cse.hunkim.questionroom.question.Question;
import hk.ust.cse.hunkim.questionroom.services.ErrorIdResponse;
import hk.ust.cse.hunkim.questionroom.services.QuestionService;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by JT and Long on 10/31/2015.
 */
public class AnswerListAdapter extends DatabaseListAdapter<Answer> {
    private final Question question;

    public AnswerListAdapter(Context context, int textViewResourceId, Question question, List<Answer> answerList) {
        super(context, textViewResourceId, answerList);
        this.context = context;
        this.question = question;
        this.mQuestionList = question.getAnswers();

        // Must be MainActivity
        assert (context instanceof AnswerActivity);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;

        if (view == null) {
            holder = new Holder();

            if (i == 0) {
                view = inflater.inflate(R.layout.questionfirst, viewGroup, false);
            }
            else {
                view = inflater.inflate(R.layout.answers, viewGroup, false);
            }
            holder.likeButton = (Button) view.findViewById(R.id.echo);
            holder.numberOfLikes = (TextView) view.findViewById(R.id.numberOfLikes);
            holder.textView = (TextView) view.findViewById(R.id.head_desc);
            holder.iv = (ImageView) view.findViewById(R.id.imageView);
            holder.replyBtn = (Button) view.findViewById(R.id.reply);
            view.setTag(holder);

        } else {
            holder = (Holder) view.getTag();
        }

        if (i == 0) {
            holder.replyBtn.setVisibility(View.GONE);
            populateView(holder, question);
        }
        else {
            final Answer answer = mQuestionList.get(i-1);
            String replyText = "Followup (" + Integer.toString(answer.getFollow_ups().size()) + ")";
            holder.replyBtn.setText(replyText);
            holder.replyBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(context, FollowupActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable(FollowupActivity.ANSWER, answer);
                    intent.putExtras(b);
                    intent.putExtra(MainActivity.ROOM_NAME, ((AnswerActivity) context).roomName);
                    context.startActivity(intent);
                }
            });

            populateView(holder, answer);
        }

        return view;
    }

    @Override
    public int getCount() {
        return super.getCount() + 1;
    }

    @Override
    public void push(final Answer answer, String baseID) {
        QuestionService service = retrofit.create(QuestionService.class);

        Call<ErrorIdResponse> response = service.createAnswer(
                baseID,
                answer.getText(),
                answer.getImageURL(),
                ((AnswerActivity) context).getUserId()
        );

        response.enqueue(new Callback<ErrorIdResponse>() {
            @Override
            public void onResponse(Response<ErrorIdResponse> response, Retrofit retrofit) {
                answer.setId(response.body().id);
                mQuestionList.add(answer);
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("QUESTIONROOM", "Failed at DatabaseListAdapter.push():", t);
            }
        });
    }
}
