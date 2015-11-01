package hk.ust.cse.hunkim.questionroom;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import hk.ust.cse.hunkim.questionroom.question.Question;

/**
 * Created by Joel on 29/10/2015.
 */
public class QuestionListAdapter extends DatabaseListAdapter {
// The mUsername for this client. We use this to indicate which messages originated from this user
    Context activity;

    public QuestionListAdapter(Context context, int mLayout) {
        super(context, mLayout);

        // Must be MainActivity
        assert (activity instanceof MainActivity);
    }

    @Override
    protected void populateView(View view, Question question) {
        // Map a Chat object to an entry in our listview
        String[] likesArr = question.getLikes();
        int likes = 0;
        if (likesArr != null)
            likes = likesArr.length;
        Button likeButton = (Button) view.findViewById(R.id.echo);
        likeButton.setText("" + likes);
        likeButton.setTextColor(Color.BLUE);
        likeButton.setTag(question.getId()); // Set tag for button
        likeButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MainActivity m = (MainActivity) view.getContext();
                        m.updateLikes((String) view.getTag());
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

        final ListView answerList = ((ListView) view.findViewById(R.id.answerlist));

        answerList.setAdapter(new AnswerListAdapter(context, R.id.answerlist,question.getAnswers()));

        // http://stackoverflow.com/questions/8743120/how-to-grey-out-a-button
        // grey out our button
        if (clickable) {
            likeButton.getBackground().setColorFilter(null);
        } else {
            likeButton.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        }

        if (!question.getImageURL().equals("")) {
            ImageView iv = (ImageView) view.findViewById(R.id.imageView);
            iv.setImageDrawable(null);

            if (view.getTag() == question.getId()) {
                Picasso.with(context)
                        .load(question.getImageURL())
                        .tag(question.getId())
                        .into(iv);
            }
        }

        view.setTag(question.getId());  // store key in the view
    }

    @Override
    protected void sortModels(List<Question> mModels) {
        Collections.sort(mModels);
    }
}
