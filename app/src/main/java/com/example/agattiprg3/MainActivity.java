package com.example.agattiprg3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private int counter = 1;
    ArrayList<myList> theList;
    myListAdapter itemAdapter;
    private int position;
    View adaptView;
    private int counterFile = 1;
    boolean nameExists = false;

    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        /**
         * When a list is clicked on gets the data
         * @param adapterView
         * @param view
         * @param i
         * @param l
         */
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            adaptView = view;
            itemAdapter.setMostRecentlyClickedPosition(i);
            itemAdapter.notifyDataSetChanged();
            position = i;
        }
    };

    /**
     * Method called when app is started
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("Create", "Oncreate");

        generateList(); //Creates an empty list

        getListFromFile(); //Fills that list with any stored lists

        itemAdapter = new myListAdapter(this, theList);
        ListView listViews = findViewById(R.id.listViews);
        listViews.setAdapter(itemAdapter);

        listViews.setOnItemClickListener(listener);

    }

    /**
     * Method that updates a file when a list is added or deleted
     */
    private void updateFile() {
        Log.i("update", "Updating File Add");
        try {
            Log.i("update", "Try");
            FileOutputStream fos = this.openFileOutput("list.txt", Context.MODE_PRIVATE);
            if (counter > 1) {
                for (int i = 2; i <= counter; i++) {
                    fos.write((itemAdapter.getItem(i - 2).getEnglishText()).getBytes());
                    fos.write('\n');
                }
            }
        } catch (IOException e) {
            Toast.makeText(this, "Problem with output file", Toast.LENGTH_SHORT);
        }
    }

    /**
     * Gets stored lists from list.txt
     */
    private void getListFromFile() {
        Log.i("getListFromFile", "Getting List");
        File file = getBaseContext().getFileStreamPath("list.txt");
        if (file.exists()) {
            try {
                FileInputStream fis = this.openFileInput("list.txt");
                InputStreamReader inputStreamReader = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(inputStreamReader);
                String theLine = br.readLine();
                //theLine = theLine.replaceAll("\\s","");
                Log.i("The Line is", theLine);

                while (theLine != null) {
                    theList.add(new myList(counterFile, theLine));
                    //myList newList = new myList(counterFile, theLine);
                    //theList.add(newList);
                    counterFile++;
                    theLine = br.readLine();
                }
                counter = counterFile;

                if (counter == 1) {
                    TextView emptyOrNot = findViewById(R.id.emptyOrNot);
                    emptyOrNot.setText("The List View Is Empty");
                } else {
                    TextView emptyOrNot = findViewById(R.id.emptyOrNot);
                    emptyOrNot.setText("Your lists are:");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //nothing needed, probably the first time the app was opened
        }
    }

    /**
     * Method that when a list is deleted so does the list of elements it has
     */
    private void removeElementFile() {
        Log.i("remove element", itemAdapter.getItem(position).getEnglishText().concat(".txt"));
        File fileE = getBaseContext().getFileStreamPath(itemAdapter.getItem(position).getEnglishText().concat(".txt"));
        if (fileE.exists()) {
            fileE.delete();
        }
    }

    /**
     * Method that creates an empty list of myList objects
     */
    private void generateList() {
        theList = new ArrayList<myList>();
    }

    /**
     * Method associated with create list button that prompts the user for the name of the list
     * @param view
     */
    public void createList(View view) {
        TextView createList = findViewById(R.id.createList);
        createList.setVisibility(View.INVISIBLE);

        TextView listName = findViewById(R.id.listName);
        listName.setVisibility(View.VISIBLE);

        TextView addButton = findViewById(R.id.addList);
        addButton.setVisibility(View.VISIBLE);

        itemAdapter = new myListAdapter(this, theList);
        ListView listViews = findViewById(R.id.listViews);
        listViews.setAdapter(itemAdapter);

        listViews.setOnItemClickListener(listener);
    }

    /**
     * Method that adds the new list to the list once the add button is clicked
     * @param view
     */
    public void addList(View view) {
        TextView listName = findViewById(R.id.listName);
        String listNameWithoutSpace = listName.getText().toString();

        if (counter > 1) { //Checks if a list already exists
            for (int i = 2; i <= counter; i++) {
                if (itemAdapter.getItem(i - 2).getEnglishText().equals(listNameWithoutSpace)) {
                    nameExists = true;
                    Toast.makeText(this, "List Already Exists", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    nameExists = false;
                }
            }
        }

        if(TextUtils.isEmpty(listName.getText().toString()) == false && nameExists == false){
            //Log.i("counter is", String.valueOf(counter));
            theList.add(new myList(counter, listName.getText().toString()));
            counter++;
            TextView emptyOrNot = findViewById(R.id.emptyOrNot);
            emptyOrNot.setText("Your lists are:");

            //Unhides the create list button
            TextView createList = findViewById(R.id.createList);
            createList.setVisibility(View.VISIBLE);

            //Hides the listName plainText
            listName.setVisibility(View.INVISIBLE);
            listName.setText("");

            //Hides the add button
            TextView addButton = findViewById(R.id.addList);
            addButton.setVisibility(View.INVISIBLE);

            //Hides The Keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

            itemAdapter = new myListAdapter(this, theList);
            ListView listViews = findViewById(R.id.listViews);
            listViews.setAdapter(itemAdapter);

            listViews.setOnItemClickListener(listener);
            updateFile();

        }

    }


    /**
     * Method associated with delete button that deletes the list from the list
     * @param view
     */
    public void deleteList(View view) {
        ListView listViews = findViewById(R.id.listViews);
        listViews.setAdapter(itemAdapter);
        listViews.setOnItemClickListener(listener);

        removeElementFile();

        theList.remove(itemAdapter.getItem(position));
        counter--;
        if (counter == 1) {
            TextView emptyOrNot = findViewById(R.id.emptyOrNot);
            emptyOrNot.setText("The List View Is Empty");
        }
        updateFile();
    }

    /**
     * Method that hides the list when the hide button is pressed
     * @param view
     */
    public void hideList(View view) {
        LinearLayout firstRowTV = adaptView.findViewById(R.id.firstRow);
        LinearLayout secondRowTV = adaptView.findViewById(R.id.secondRow);
        firstRowTV.setVisibility(View.GONE);
        secondRowTV.setVisibility(View.GONE);

        itemAdapter = new myListAdapter(this, theList);
        ListView listViews = findViewById(R.id.listViews);
        listViews.setAdapter(itemAdapter);

        listViews.setOnItemClickListener(listener);
    }

    /**
     * Method that selects the list and opens up its elements page
     * @param view
     */
    public void selectList(View view) {

        myList currentList = itemAdapter.getItem(position);
        String name = currentList.getEnglishText();

        Intent doSomethingIntent = new Intent(MainActivity.this, elements.class);
        doSomethingIntent.putExtra("name", name);
        startActivity(doSomethingIntent);

        itemAdapter = new myListAdapter(this, theList);
        ListView listViews = findViewById(R.id.listViews);
        listViews.setAdapter(itemAdapter);

        listViews.setOnItemClickListener(listener);
    }

    /**
     * Method moves list to new position and puts old list in the old position, basically swapping the lists
     * @param view
     */
    public void moveList(View view) {

        ListView listViews = findViewById(R.id.listViews);
        listViews.setAdapter(itemAdapter);

        EditText posTv = adaptView.findViewById(R.id.posText);
        String posPosition = posTv.getText().toString();
        
        if(TextUtils.isEmpty(posTv.getText().toString()) == false) { //Checks if editText is empty
            int num = Integer.parseInt(posPosition);
            if (num <= 0 || num >= counter) { //if counter out of bounds
                Toast.makeText(this, "Pos Out of Bounds", Toast.LENGTH_SHORT).show();
                itemAdapter = new myListAdapter(this, theList);
                listViews.setAdapter(itemAdapter);
                listViews.setOnItemClickListener(listener);
                itemAdapter.notifyDataSetChanged();

                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            } else {
                myList currentList = new myList(itemAdapter.getItem(position));
                int oldCurrentPos = position;
                myList currentList2 = new myList(itemAdapter.getItem(num - 1));

                //Hides The Keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0); //hides keyboard

                theList.remove(itemAdapter.getItem(position));
                itemAdapter.insert(currentList2, oldCurrentPos);
                theList.remove(itemAdapter.getItem(num - 1));
                itemAdapter.insert(currentList, num - 1);

                itemAdapter = new myListAdapter(this, theList);
                listViews.setAdapter(itemAdapter);
                listViews.setOnItemClickListener(listener);
                itemAdapter.notifyDataSetChanged();

                updateFile();

            }

        }

        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0); // hides keyboard

    }

}




