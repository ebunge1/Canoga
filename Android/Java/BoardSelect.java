package com.evanbunge.canoga;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BoardSelect extends AppCompatActivity implements View.
        OnClickListener {

    /*********************************************************************
     Function Name: OnCreate
     Purpose: get size of board via user input
     Parameters: savedInstanceState, Bundle
     Return Value: void
     Local Variables:
     Algorithm:
     Assistance Received: none
     *********************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boardselect);

        Button size9 = (Button) findViewById(R.id.size9);
        Button size10 = (Button) findViewById(R.id.size10);
        Button size11 = (Button) findViewById(R.id.size11);
        size9.setOnClickListener(this);
        size10.setOnClickListener(this);
        size11.setOnClickListener(this);
    }

    /*********************************************************************
     Function Name: onClick
     Purpose: return the boardsize as result
     Parameters: view
     Return Value: void
     Local Variables:
     Algorithm:
     Assistance Received: none
     *********************************************************************/
    @Override
    public void onClick(View view) {
        Intent size = new Intent();
        size.putExtras( getIntent().getExtras() );
        switch (view.getId()) {
            case R.id.size9:
                size.putExtra("size", 9);
                break;
            case R.id.size10:
                size.putExtra("size", 10);
                break;
            case R.id.size11:
                size.putExtra("size", 11);
                break;
        }
        setResult(RESULT_OK, size);
        finish();
    }
}
