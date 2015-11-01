package hk.ust.cse.hunkim.questionroom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import hk.ust.cse.hunkim.questionroom.R;
import hk.ust.cse.hunkim.questionroom.question.Answer;
import hk.ust.cse.hunkim.questionroom.question.FollowUp;

/**
 * Created by J.T and Long on 10/31/2015.
 */
public class FollowupListAdapter extends ArrayAdapter<FollowUp> {
    private final Context context;
    private final FollowUp[] values;

    public FollowupListAdapter(Context context, int textViewResourceId, FollowUp[] values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.followup, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.head_desc_followup);

        textView.setText(values[position].getText());
        return rowView;
    }

    @Override
    public int getViewTypeCount() {

        if (getCount() != 0)
            return getCount();

        return 1;
    }
}
