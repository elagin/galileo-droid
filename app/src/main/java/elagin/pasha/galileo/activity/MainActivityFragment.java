package elagin.pasha.galileo.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import elagin.pasha.galileo.MyApp;
import elagin.pasha.galileo.R;

public class MainActivityFragment extends Fragment {

    private View messagesTable;
    private MyApp myApp;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View viewMain = inflater.inflate(R.layout.fragment_main, container, false);
        messagesTable = viewMain.findViewById(R.id.messages_table);

        myApp = (MyApp) getActivity().getApplicationContext();

        update();

        return viewMain;
    }

    public void update() {
        ViewGroup messageView = (ViewGroup) messagesTable;
        messageView.removeAllViews();

        for (int i = 0; i < myApp.Messages().size(); i++) {
            myApp.Messages().get(i).inflateRow(getActivity(), messageView);
        }
    }
}
