package elagin.pasha.galileo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Date;

public class AnswerMessage {

    Date date;
    String text;

    public AnswerMessage(String text) {
        this.date = new Date();
        this.text = text;
    }

    public void inflateRow(final Context context, ViewGroup tableLayout) {
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        TableRow tr = (TableRow) li.inflate(R.layout.answer_message_row, tableLayout, false);
        ((TextView) tr.findViewById(R.id.date_message)).setText(MyUtils.getStringTime(date, false));
        ((TextView) tr.findViewById(R.id.text_message)).setText(text);
        tableLayout.addView(tr);
    }
}
