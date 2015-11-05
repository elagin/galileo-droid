package elagin.pasha.galileo.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import elagin.pasha.galileo.MyApp;
import elagin.pasha.galileo.R;
import elagin.pasha.galileo.seven_gis.Answer;

public class MainActivityFragment extends Fragment {

    private View messagesTable;
    private MyApp myApp;
    private TextView answerBody;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewMain = inflater.inflate(R.layout.fragment_main, container, false);
        messagesTable = viewMain.findViewById(R.id.messages_table);

        answerBody = (TextView) viewMain.findViewById(R.id.answerBody);

        myApp = (MyApp) getActivity().getApplicationContext();
        update();
        return viewMain;
    }

    public void update() {
        ViewGroup messageView = (ViewGroup) messagesTable;
        final TableLayout messagesTableLayout = (TableLayout) messagesTable;

        messageView.removeAllViews();
        for (int i = 0; i < myApp.getSmsHistory().size(); i++) {
            Answer answer = myApp.getSmsHistory().get(i);
            answer.inflateRow(getActivity(), messageView);
        }

        for (int i = 0; i < messagesTableLayout.getChildCount(); i++) {
            final View row = messagesTableLayout.getChildAt(i);
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int row_id = messagesTableLayout.indexOfChild(row);
                    Answer answer = myApp.getSmsHistory().get(row_id);
                    String detail = answer.getDetail();
                    answerBody.setText(detail);
                }
            });
        }
    }
}
