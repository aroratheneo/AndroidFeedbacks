package com.example.aroras.commentscollector;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.FileOutputStream;

public class MainActivity  extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText mEdit;
    EditText NameText;
    MyDBHandler dbHandler;
    Button button;
    Spinner spinner;
    static final String[]paths = {"Select Session", "item 2", "item 3"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setTitle("Tech Stock Feedback");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEdit   = (EditText)findViewById(R.id.editText);
        NameText = (EditText)findViewById(R.id.NameText);
        button = (Button)findViewById(R.id.button);
        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        mEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                enableSubmitIfReady();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        dbHandler = new MyDBHandler(this, null, null, 1);
    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
    {
        if(position != 0) {
            String currentmessage = mEdit.getText().toString();
            mEdit.setText(currentmessage + " @" + parent.getItemAtPosition(position).toString() + " ");
            mEdit.setSelection(mEdit.getText().length());

        }
    }

    public void onNothingSelected (AdapterView<?> parent)
    {

    }

    public void enableSubmitIfReady() {

        boolean isReady =mEdit.getText().toString().length()>3;
        button.setEnabled(isReady);
    }

    public void getComments(View view)
    {

        String str = mEdit.getText().toString();
        String name = NameText.getText().toString();
        dbHandler.AddComments(str, name);
        dbHandler.ReadFromTable();
        dbHandler.CopyDB();
        mEdit.setText("");
        NameText.setText("");
        spinner.setSelection(0);


    }

    public void viewComments(View view) {

    }

}
