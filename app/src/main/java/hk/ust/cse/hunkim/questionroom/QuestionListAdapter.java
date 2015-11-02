package hk.ust.cse.hunkim.questionroom;

import android.content.Context;
import android.database.DataSetObserver;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

//<<<<<<< HEAD
import java.util.ArrayList;
//=======
import org.w3c.dom.Text;

//>>>>>>> UI_enhancement
import java.util.Collections;
import java.util.List;

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
    public QuestionListAdapter(Context context, int mLayout, List<Question> questionList) {
        super(context, mLayout, questionList);

        // Must be MainActivity
        assert (context instanceof MainActivity);
        this.mQuestionList = questionList;
    }

    @Override
    protected void populateView(final View view, final Question question) {
//<<<<<<< HEAD
        super.populateView(view, question);
//=======
        // Map a Chat object to an entry in our listview
        String[] likesArr = question.getLikes();
        int likes = 0;
        if (likesArr != null)
            likes = likesArr.length;
        final Button likeButton = (Button) view.findViewById(R.id.echo);
        TextView numberOfLikes=(TextView) view.findViewById(R.id.numberOfLikes);
        numberOfLikes.setText("" + likes);
        likeButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MainActivity m = (MainActivity) view.getContext();
                        m.updateLikes(question);
                    }
                }
        );

        // check if we already clicked
        boolean clickable = !dbUtil.contains(question.getId());
        likeButton.setClickable(clickable);
        likeButton.setEnabled(clickable);

        // Set question Text.
        String msgString = "";
        question.updateNewQuestion();
        if (question.isNewQuestion()) {
            msgString += "<font color=red>NEW </font>";
        }
        msgString += question.getText();
        ((TextView) view.findViewById(R.id.head_desc)).setText(Html.fromHtml(msgString + " (" + question.getAnswers().size() + ")"));
//>>>>>>> UI_enhancement

        final ListView answerList = ((ListView) view.findViewById(R.id.answerlist));
        final AnswerListAdapter mChatListAdapter = new AnswerListAdapter(view.getContext(), R.layout.answer, question, question.getAnswers());
        answerList.setAdapter(mChatListAdapter);

        mChatListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                answerList.setSelection(mChatListAdapter.getCount() - 1);
            }
        });

        view.findViewById(R.id.reply).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mChatListAdapter.sendAnswer(view);
            }
        });

        //REPLY
        Button replyBtn = (Button) view.findViewById(R.id.reply);
        replyBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LinearLayout answerFooter = (LinearLayout) view.findViewById(R.id.answerlistFooter);

                if (answerFooter.getVisibility() != View.VISIBLE) {
                    answerFooter.setVisibility(View.VISIBLE);
                } else {
                    answerFooter.setVisibility(View.GONE);
                }
            }
        });

        //EXPAND AND COLLAPSE
        Button collapseBtn = (Button) view.findViewById(R.id.btn_collapse);
        collapseBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ListView answerList = (ListView) view.findViewById(R.id.answerlist);
                Button collapseBtn = (Button) view.findViewById(R.id.btn_collapse);

                if (answerList.getVisibility() != View.VISIBLE) {
                    answerList.setVisibility(View.VISIBLE);
                    collapseBtn.setText("Collapse");

                } else {
                    answerList.setVisibility(View.GONE);
                    collapseBtn.setText("Expand");
                }
            }
        });
    }

    @Override
    public void push(final Question question, String baseID) {
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


    @Override
    protected void sortModels(List<Question> mModels) {
        Collections.sort(mModels);
    }
}
