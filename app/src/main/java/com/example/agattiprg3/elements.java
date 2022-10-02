package com.example.agattiprg3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

public class elements extends AppCompatActivity {

    private int counter2 = 1;
    ArrayList<myListE> theListE;
    myListAdapterE itemAdapter;
    private int position;
    View adaptView;
    private int counterFile2 = 1;
    boolean nameExists = false;

    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        /**
         * When an element is clicked on gets the data
         * @param adapterView
         * @param view
         * @param i
         * @param l
         */
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            itemAdapter.setMostRecentlyClickedPosition(i);
            itemAdapter.notifyDataSetChanged();
            position = i;
            adaptView = view;
        }
    };

    /**
     * Method called when app is started
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elements);

        generateList2();

        getEListFromFile();

        itemAdapter = new myListAdapterE(this, theListE);
        ListView listViews = findViewById(R.id.listViews);
        listViews.setAdapter(itemAdapter);

        listViews.setOnItemClickListener(listener);

    }

    /**
     * Method that updates a file when an element is added or deleted
     */
    private void updateEFile() {
        Log.i("update", "Updating File E");
        Intent callerIntent = getIntent();
        String name = callerIntent.getStringExtra("name");
        String fileName = name.concat(".txt");
        try {
            Log.i("update", "TryE");
            FileOutputStream fos = this.openFileOutput(fileName, Context.MODE_PRIVATE);
            if (counter2 > 1) {
                for (int i = 2; i <= counter2; i++) {
                    fos.write((itemAdapter.getItem(i - 2).getEnglishText()).getBytes());
                    fos.write('\n');
                }
            }
        } catch (IOException e) {
            Toast.makeText(this, "Problem with output file", Toast.LENGTH_SHORT);
        }
    }

    /**
     * Gets stored elements from specific list
     */
    private void getEListFromFile() {
        Log.i("getListFromFile", "Getting List");
        Intent callerIntent = getIntent();
        String name = callerIntent.getStringExtra("name");
        String fileName = name.concat(".txt");
        File file = getBaseContext().getFileStreamPath(fileName);
        if (file.exists()) {
            try {
                FileInputStream fis = this.openFileInput(fileName);
                InputStreamReader inputStreamReader = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(inputStreamReader);
                String theLine = br.readLine();
                Log.i("The Line is", theLine);

                while (theLine != null) {
                    theListE.add(new myListE(counterFile2, theLine));
                    counterFile2++;
                    theLine = br.readLine();
                }
                counter2 = counterFile2;

                if (counter2 == 1) {
                    TextView elementEmptyOrNot = findViewById(R.id.elementEmptyOrNot);
                    elementEmptyOrNot.setText(name + " Is Currently Empty");
                } else {
                    TextView elementEmptyOrNot = findViewById(R.id.elementEmptyOrNot);
                    elementEmptyOrNot.setText(name + ":");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //nothing needed, probably the first time the app was opened
        }
    }

    /**
     * Method that creates an empty list of elements
     */
    private void generateList2() {
        theListE = new ArrayList<myListE>();
    }

    /**
     * Method associated with add element button that prompts the user for the name of the element
     * @param view
     */
    public void createElement(View view) {
        TextView createElement = findViewById(R.id.createElement);
        createElement.setVisibility(View.INVISIBLE);

        TextView listName = findViewById(R.id.elementName);
        listName.setVisibility(View.VISIBLE);

        TextView addButton = findViewById(R.id.addElement);
        addButton.setVisibility(View.VISIBLE);

        itemAdapter = new myListAdapterE(this, theListE);
        ListView listViews = findViewById(R.id.listViews);
        listViews.setAdapter(itemAdapter);

        listViews.setOnItemClickListener(listener);
    }

    /**
     * Method that adds the new element to the list once the add button is clicked
     * @param view
     */
    public void addElement(View view) {
        TextView elementName = findViewById(R.id.elementName);

        if (counter2 > 1) {
            for (int i = 2; i <= counter2; i++) {
                if (itemAdapter.getItem(i - 2).getEnglishText().equals(elementName.getText().toString())) {
                    nameExists = true;
                    Toast.makeText(this, "Element Already Exists", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    nameExists = false;
                }
            }
        }

        if (TextUtils.isEmpty(elementName.getText().toString()) == false && nameExists == false) {
            theListE.add(new myListE(counter2, elementName.getText().toString()));
            counter2++;

            //Unhides the create Element button
            TextView createElement = findViewById(R.id.createElement);
            createElement.setVisibility(View.VISIBLE);

            //Hides the listName plainText
            elementName.setVisibility(View.INVISIBLE);
            elementName.setText("");

            //Hides the add button
            TextView addElement = findViewById(R.id.addElement);
            addElement.setVisibility(View.INVISIBLE);

            Intent callerIntent = getIntent();
            String name = callerIntent.getStringExtra("name");

            TextView elementEmptyOrNot = findViewById(R.id.elementEmptyOrNot);
            elementEmptyOrNot.setText(name + ":");

            //Hides The Keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

            itemAdapter = new myListAdapterE(this, theListE);
            ListView listViews = findViewById(R.id.listViews);
            listViews.setAdapter(itemAdapter);

            listViews.setOnItemClickListener(listener);
            updateEFile();

        }

    }
    /**
     * Method associated with delete button that deletes the element from the list
     * @param view
     */
    public void deleteListE(View view) {
        ListView listViews = findViewById(R.id.listViews);
        listViews.setAdapter(itemAdapter);
        listViews.setOnItemClickListener(listener);

        theListE.remove(itemAdapter.getItem(position));
        counter2--;
        if (counter2 == 1) {
            Intent callerIntent = getIntent();
            String name = callerIntent.getStringExtra("name");
            TextView elementEmptyOrNot = findViewById(R.id.elementEmptyOrNot);
            elementEmptyOrNot.setText(name + " Is Currently Empty");
        }
        updateEFile();
    }

    /**
     * Method that hides the element when the hide button is pressed
     * @param view
     */
    public void hideListE(View view) {
        LinearLayout firstRowTV = adaptView.findViewById(R.id.firstRow);
        LinearLayout secondRowTV = adaptView.findViewById(R.id.secondRow);
        firstRowTV.setVisibility(View.GONE);
        secondRowTV.setVisibility(View.GONE);

        itemAdapter = new myListAdapterE(this, theListE);
        ListView listViews = findViewById(R.id.listViews);
        listViews.setAdapter(itemAdapter);

        listViews.setOnItemClickListener(listener);
    }

    /**
     * Method moves element list to new position and puts old list in the old position, basically swapping the lists
     * @param view
     */
    public void moveListE(View view) {

        ListView listViews = findViewById(R.id.listViews);
        listViews.setAdapter(itemAdapter);

        EditText posTv = adaptView.findViewById(R.id.posTextE);
        String posPosition = posTv.getText().toString();

        if(TextUtils.isEmpty(posTv.getText().toString()) == false) { //Checks if editText is empty
            int num = Integer.parseInt(posPosition);
            if (num <= 0 || num >= counter2) { //if counter out of bounds
                Toast.makeText(this, "Pos Out of Bounds", Toast.LENGTH_SHORT).show();
                itemAdapter = new myListAdapterE(this, theListE);
                listViews.setAdapter(itemAdapter);
                listViews.setOnItemClickListener(listener);
                itemAdapter.notifyDataSetChanged();

                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            } else {
                myListE currentList = new myListE(itemAdapter.getItem(position));
                int oldCurrentPos = position;
                myListE currentList2 = new myListE(itemAdapter.getItem(num - 1));

                //Hides The Keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0); //hides keyboard

                theListE.remove(itemAdapter.getItem(position));
                itemAdapter.insert(currentList2, oldCurrentPos);
                theListE.remove(itemAdapter.getItem(num - 1));
                itemAdapter.insert(currentList, num - 1);

                itemAdapter = new myListAdapterE(this, theListE);
                listViews.setAdapter(itemAdapter);
                listViews.setOnItemClickListener(listener);
                itemAdapter.notifyDataSetChanged();

                updateEFile();

            }

        }

        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0); // hides keyboard

    }

}