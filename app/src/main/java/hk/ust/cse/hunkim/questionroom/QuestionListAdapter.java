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

import java.util.Collections;
import java.util.List;

import hk.ust.cse.hunkim.questionroom.db.UserHelper;
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
        Holder holder;
        final Question question = mQuestionList.get(i);

        if (view == null) {
            view = inflater.inflate(R.layout.questionfirst, viewGroup, false);

            holder = new Holder();
            holder.likeButton = (Button) view.findViewById(R.id.echo);
            holder.numberOfLikes = (TextView) view.findViewById(R.id.numberOfLikes);
            holder.textView = (TextView) view.findViewById(R.id.head_desc);
            holder.iv = (ImageView) view.findViewById(R.id.imageView);
            holder.replyBtn = (Button) view.findViewById(R.id.reply);
            holder.img_userCharacter=(ImageView)view.findViewById(R.id.questionUserCharacter);
            holder.txt_userClass=(TextView)view.findViewById(R.id.questionUserClass);
            holder.txt_userLvl=(TextView)view.findViewById(R.id.questionUserLvl);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        //set profile info
        int experience= question.getExperience();
        int level=UserHelper.getLevel(experience);
        holder.txt_userClass.setText(UserHelper.getUserClass(level));
        holder.txt_userLvl.setText("Lvl "+String.valueOf(level));
        UserHelper.setCharacterImage(level, holder.img_userCharacter);

        //REPLY
        String replyText = "Answer (" + Integer.toString(question.getAnswers().size()) + ")";
        holder.replyBtn.setText(replyText);
        holder.replyBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, AnswerActivity.class);
                Bundle b = new Bundle();
                b.putSerializable(AnswerActivity.QUESTION, question);
                intent.putExtras(b);
                intent.putExtra(MainActivity.ROOM_NAME, ((MainActivity) context).roomName);
                context.startActivity(intent);
            }
        });

        populateView(holder, question);

        return view;
    }

    @Override
    public void push(final Question question, String baseID) {
        QuestionService service = retrofit.create(QuestionService.class);

        final Call<ErrorIdResponse> response = service.createQuestion(
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
}
