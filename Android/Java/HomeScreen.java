package com.evanbunge.canoga;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.io.File;

public class HomeScreen extends AppCompatActivity implements View.
        OnClickListener{

    /*********************************************************************
    Function Name:  OnCreate
    Purpose: set up the homescreen, get user input
    Parameters: savedInstanceState, Bundle
    Return Value: Void
    Local Variables:
        fileselector, spinner to select saved files
        path, file datatype to get path to saved game directory
        files, array of saved game files
        filenames, array of files as strings to send to spinner
        newadapter, adapter to fill spinner
        newGameButton, button to start new game
        loadGameButton, button to load game
    Algorithm:
        get file path
        convert file names to simplified strings
        fill spinner with file names
        set click listeners for buttons
    Assistance Received: None
    *********************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);

        Spinner fileselector = (Spinner) findViewById(R.id.fileselect);
        File path = new File(getExternalFilesDir(null).toString());
        File files[] = path.listFiles();
        String[] filenames = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            filenames[i] = files[i].toString();
            filenames[i] = filenames[i].substring(60).replace(".txt", "");
        }
        ArrayAdapter<Integer> newadapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, filenames);
        fileselector.setAdapter(newadapter);

        Button newGameButton = (Button) findViewById(R.id.newGameButton);
        Button loadGameButton = (Button) findViewById(R.id.loadGameButton);
        newGameButton.setOnClickListener(this);
        loadGameButton.setOnClickListener(this);
    }

    /*********************************************************************
     Function Name:  OnClick
     Purpose: respond to button clicks
     Parameters: view
     Return Value: void
     Local Variables:
        newgame, intent to start a new game
        loadgame, intent to load and continue game
     Algorithm:
        check which button was pressed
        add extra necessary information to intent
        start new activity
     Assistance Received:
     *********************************************************************/
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.newGameButton:
                Intent newgame = new Intent(this, Tournament.class);
                newgame.putExtra("serialized", false);
                startActivity(newgame);
                break;
            case R.id.loadGameButton:

                Spinner fileselector = (Spinner) findViewById(R.id.fileselect);
                String filename = (String) fileselector.getSelectedItem();
                Intent loadgame = new Intent(this, Tournament.class);
                loadgame.putExtra("serialized", true);
                loadgame.putExtra("filename", filename);
                startActivity(loadgame);
                break;
        }
    }
}
