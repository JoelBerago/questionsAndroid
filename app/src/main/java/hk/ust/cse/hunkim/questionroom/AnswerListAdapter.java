package hk.ust.cse.hunkim.questionroom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import hk.ust.cse.hunkim.questionroom.question.Answer;

/**
 * Created by JT and Long on 10/31/2015.
 */
public class AnswerListAdapter extends ArrayAdapter<Answer> {
    private final Context context;
    private final Answer[] values;

    public AnswerListAdapter(Context context, int textViewResourceId, Answer[] values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.answer, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.head_desc_answers);

        textView.setText(values[position].getText());

        final ListView followupList = ((ListView) rowView.findViewById(R.id.followuplist));

        followupList.setAdapter(new FollowupListAdapter(context, R.id.followuplist, values[position].getFollow_ups()));

        return rowView;
    }

    @Override
    public int getViewTypeCount() {

        if (getCount() != 0)
            return getCount();

        return 1;
    }
}
