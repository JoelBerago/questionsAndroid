package hk.ust.cse.hunkim.questionroom;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.util.Collections;
import java.util.List;

import hk.ust.cse.hunkim.questionroom.db.ImageHelper;
import hk.ust.cse.hunkim.questionroom.question.Answer;
import hk.ust.cse.hunkim.questionroom.question.BaseQuestion;
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
    }

    @Override
    protected void populateView(final View view, final Answer answer) {
        super.populateView(view, answer);

        final ListView followupList = ((ListView) view.findViewById(R.id.followuplist));
        final FollowupListAdapter mChatListAdapter = new FollowupListAdapter(view.getContext(), R.layout.followup, answer, answer.getFollow_ups());
        followupList.setAdapter(mChatListAdapter);

        view.findViewById(R.id.sendButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mChatListAdapter.sendFollowup(view);
                    }
                }
        );

        mChatListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                followupList.setSelection(mChatListAdapter.getCount() - 1);
            }
        });
    }

    @Override
    public void push(final Answer answer, String baseID) {
        QuestionService service = retrofit.create(QuestionService.class);

        Call<ErrorIdResponse> response = service.createAnswer(
                baseID,
                answer.getText(),
                answer.getImageURL()
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

    protected void sendAnswer(View view) {
        EditText inputText = (EditText) ((MainActivity) context).findViewById(R.id.messageInput);
        String input = inputText.getText().toString();
        Answer answer;
        if (!input.equals("")) {
            answer = new Answer(input);

            // Clear inputText.
            inputText.setText("");

            if (!ImageHelper.picturePath.equals("")) {
                uploadPhoto(ImageHelper.picturePath, answer, question.getId());
            } else {
                push(answer, question.getId());
            }
        }
    }
}
