package hk.ust.cse.hunkim.questionroom;

import android.content.Context;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.List;

import hk.ust.cse.hunkim.questionroom.question.Question;

/**
 * Created by Joel on 29/10/2015.
 */
public class QuestionListAdapter extends DatabaseListAdapter{
// The mUsername for this client. We use this to indicate which messages originated from this user
    //Context activity;

    public QuestionListAdapter(Context context, int mLayout) {
        super(context, mLayout);

        // Must be MainActivity
        //assert (activity instanceof MainActivity);
    }

    @Override
    protected void populateView(final View view, final Question question) {
        // Map a Chat object to an entry in our listview
        String[] likesArr = question.getLikes();
        int likes = 0;
        if (likesArr != null)
            likes = likesArr.length;
        final Button likeButton = (Button) view.findViewById(R.id.echo);
        TextView numberOfLikes=(TextView) view.findViewById(R.id.numberOfLikes);
        numberOfLikes.setText("" + likes);
        //likeButton.setTextColor(Color.BLUE);
        //likeButton.setTag(question.getId()); // Set tag for button
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

        final ListView answerList = ((ListView) view.findViewById(R.id.answerlist));

        answerList.setAdapter(new AnswerListAdapter(context, R.id.answerlist,question.getAnswers()));

        // http://stackoverflow.com/questions/8743120/how-to-grey-out-a-button
        // grey out our button
        if (clickable) {
            likeButton.getBackground().setColorFilter(null);
        } else {
            likeButton.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        }

        ///////////////////////////////
        // display image under text
        ImageView iv = (ImageView) view.findViewById(R.id.imageView);
        iv.setImageBitmap(null);
        iv.setImageDrawable(null);
        // only if URL exist
        if (!question.getImageURL().equals("")) {
            Picasso.with(view.getContext())
                    .load(question.getImageURL())
                    .resize(400, 240)   // image can stretch up to 240x140 max.
                    .centerInside()
                    .into(iv);
            // upon clicking image view, pop up dialog
            iv.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.e("Debug", "clicked");
                    //Toast.makeText(activity, question.getImageURL(), Toast.LENGTH_SHORT).show();
                    if (!question.getImageURL().equals("")) {
                        Dialog dialog = new Dialog(context);
                        dialog.setTitle("View image");
                        dialog.setContentView(R.layout.imageview_dialog);
                        ImageView iv = (ImageView) dialog.findViewById(R.id.dialog_image);
                        iv.setImageBitmap(null);
                        Picasso.with(dialog.getContext())
                                .load(question.getImageURL())
                                .into(iv);
                        dialog.show();
                    }
                }
            });
        }

        //-----EXPAND AND COLLAPSE (not nested)----------//
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
        //----------------------------------------------//

        view.setTag(question.getId());  // store key in the view
    }


    @Override
    protected void sortModels(List<Question> mModels) {
        Collections.sort(mModels);
    }

}
