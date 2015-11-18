package hk.ust.cse.hunkim.questionroom;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Collections;
import java.util.List;

import hk.ust.cse.hunkim.questionroom.question.BaseQuestion;
import hk.ust.cse.hunkim.questionroom.question.Question;
import hk.ust.cse.hunkim.questionroom.services.ErrorIdResponse;
import hk.ust.cse.hunkim.questionroom.services.QuestionService;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Joel on 29/10/2015.
 */
public class QuestionListAdapter extends DatabaseListAdapter<Question> {
    public QuestionListAdapter(Context context, int textViewResourceId, List<Question> questionList) {
        super(context, textViewResourceId, questionList);

        // Must be MainActivity
        assert (context instanceof MainActivity);
        this.mQuestionList = questionList;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        String tag;
        if (view == null) {
            view = inflater.inflate(R.layout.questionfirst, viewGroup, false);
        }

        populateView(view, mQuestionList.get(i));
        tag =  mQuestionList.get(i).getId();

        view.setTag(tag);
        return view;
    }

    @Override
    protected void populateView(final View view, final BaseQuestion question) {
        super.populateView(view, question);

        //REPLY
        Button replyBtn = (Button) view.findViewById(R.id.reply);
        String replyText = "Answer (" + Integer.toString(((Question)question).getAnswers().size()) + ")";
        replyBtn.setText(replyText);
        replyBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, AnswerActivity.class);
                Bundle b = new Bundle();
                b.putSerializable(AnswerActivity.QUESTION, question);
                intent.putExtras(b);
                intent.putExtra(MainActivity.ROOM_NAME, ((MainActivity) context).roomName);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public void push(final Question question, String baseID) {
        QuestionService service = retrofit.create(QuestionService.class);

        Call<ErrorIdResponse> response = service.createQuestion(
                question.getText(),
                question.getImageURL(),
                question.getRoom(),
                ((MainActivity) context).getUserId()

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
                for (Question q : response.body()) {
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
}
