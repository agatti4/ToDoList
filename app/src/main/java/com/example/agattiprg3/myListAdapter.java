package com.example.agattiprg3;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class myListAdapter extends ArrayAdapter<myList> {

    private int mostRecentlyClickedPosition;

    /**
     * Initializes the adapter
     * @param context
     * @param theList
     */
    public myListAdapter(Activity context, ArrayList<myList> theList) {
        super(context, 0, theList);
        mostRecentlyClickedPosition = -1;
    }

    /**
     * Gets the data from the listview so the position and view can be passed or used
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        // modify index textView
        TextView indexTV = listItemView.findViewById(R.id.indexTextView);
        indexTV.setText(String.valueOf(position + 1));

        // other TextViews from MyNumber content
        myList currentList = getItem(position);
        TextView englishTV = listItemView.findViewById(R.id.englishTextView);
        englishTV.setText(currentList.getEnglishText());

        TextView selectTV = listItemView.findViewById(R.id.selectButton);
        TextView deleteTV = listItemView.findViewById(R.id.deleteButton);
        TextView hideTV = listItemView.findViewById(R.id.hideButton);
        TextView moveTV = listItemView.findViewById(R.id.moveButton);
        TextView posTV = listItemView.findViewById(R.id.posText);
        LinearLayout firstRowTV = listItemView.findViewById(R.id.firstRow);
        LinearLayout secondRowTV = listItemView.findViewById(R.id.secondRow);

        if (position == mostRecentlyClickedPosition) {
            firstRowTV.setVisibility(View.VISIBLE);
            secondRowTV.setVisibility(View.VISIBLE);
            selectTV.setVisibility(View.VISIBLE);
            deleteTV.setVisibility(View.VISIBLE);
            hideTV.setVisibility(View.VISIBLE);
            moveTV.setVisibility(View.VISIBLE);
            posTV.setVisibility(View.VISIBLE);
            //alreadyClicked = true;
        }
        else {
            firstRowTV.setVisibility(View.GONE);
            secondRowTV.setVisibility(View.GONE);
            selectTV.setVisibility(View.GONE);
            deleteTV.setVisibility(View.GONE);
            hideTV.setVisibility(View.GONE);
            moveTV.setVisibility(View.GONE);
            posTV.setVisibility(View.GONE);
        }

        return listItemView;
    }

    /**
     * Sets the most recently clicked position so which object was clicked is registered
     * @param mostRecentlyClickedPosition
     */
    public void setMostRecentlyClickedPosition(int mostRecentlyClickedPosition) {
        this.mostRecentlyClickedPosition = mostRecentlyClickedPosition;
    }


}
