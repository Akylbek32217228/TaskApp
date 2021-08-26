package com.example.todoapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.text.DateFormat;
import java.util.Calendar;

public class FormActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    EditText editTitle;
    EditText editDesc;
    RadioGroup radioGroup;
    ImageView image;
    int imageImportance = 1;
    Task task;
    String deadline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        editTitle = findViewById(R.id.edit_title);
        editDesc = findViewById(R.id.edit_description);
        radioGroup = findViewById(R.id.radio_group);


        task = (Task) getIntent().getSerializableExtra("task");
        if ( task != null) {
            editTitle.setText(task.getTitle());
            editDesc.setText(task.getDescription());

            switch (task.getImageImportance()) {
                case 1:
                    radioGroup.check(R.id.radio_urgent);
                case 2:
                    radioGroup.check(R.id.radio_important);
                case 3:
                    radioGroup.check(R.id.radio_common);
            }
            Log.d("ololo", "cheeked" + task.getImageImportance());
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.radio_urgent:
                        imageImportance = 1;
                        break;
                    case R.id.radio_important:
                        imageImportance = 2;
                        break;
                    case R.id.radio_common:
                        imageImportance = 3;
                        break;
                }
            }
        });


    }

    public void onClick(View view) {
        String title = editTitle.getText().toString().trim();
        String desc = editDesc.getText().toString();
        if (task != null) {
            task.setTitle(title);
            task.setDescription(desc);
            task.setImageImportance(imageImportance);
            App.getInstance().getDataBase().taskDao().update(task);
        } else {
            Task task  = new Task();
            task.setTitle(title);
            task.setDescription(desc);
            task.setImageImportance(imageImportance);
            task.setCreatedTime(System.currentTimeMillis());
            task.setDeadline(deadline);
            App.getInstance().getDataBase().taskDao().insert(task);
        }
        finish();
    }

    public void onClickSetDeadline(View view) {
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "date picker");

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        deadline = DateFormat.getInstance().format(c.getTime());
    }
}
