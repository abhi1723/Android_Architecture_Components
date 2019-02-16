package com.recstudentportal.www.android_architecture_components;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Date;

public class InsertNewGoals extends AppCompatActivity {

    EditText mEditText;
    RadioGroup mRadioGroup;
    Button mButton;
    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_MEDIUM = 2;
    public static final int PRIORITY_LOW = 3;
    AppDatabase mDb;
    int checkInsertOrUpdate=1;
    int mTaskId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_new_goals);
        mDb = AppDatabase.getInstance(getApplicationContext());
        initViews();
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("ids")) {
            mButton.setText(R.string.update_button);
           mTaskId = intent.getIntExtra("ids", 1);
           AddTaskViewModelFactory factory=new AddTaskViewModelFactory(mDb,mTaskId);
           final AddTaskViewModel viewModel= ViewModelProviders.of(this,factory).get(AddTaskViewModel.class);
           viewModel.getTaskEntryLiveData().observe(this, new Observer<TaskEntry>() {
               @Override
               public void onChanged(@Nullable TaskEntry taskEntry) {
                   viewModel.getTaskEntryLiveData().removeObservers(InsertNewGoals.this);
                   checkInsertOrUpdate=2;
                   populateUI(taskEntry);
               }
           });

        }
        }
    private void initViews() {
        mEditText = findViewById(R.id.editTextTaskDescription);
        mRadioGroup = findViewById(R.id.radioGroup);
        mButton = findViewById(R.id.saveButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOnSaveButtonClicked();
            }
        });
    }
    private void populateUI(TaskEntry task) {
        if (task == null) {
            return;
        }

        mEditText.setText(task.getDescription());
        setPriorityInViews(task.getPriority());
    }

    private void getOnSaveButtonClicked() {
        //onSaveButtonClicked();
         final TaskEntry taskEntry;
        String description = mEditText.getText().toString();
        int priority = getPriorityFromViews();
        Date date = new Date();
        if(description.length()>0 && priority>0) {
            taskEntry = new TaskEntry(description, priority, date);

            if(checkInsertOrUpdate==1) {
                final TaskEntry t=taskEntry;
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDb.taskDao().insertTask(t);
                        finish();
                    }
                });
            }
            else {
                taskEntry.setId(mTaskId);
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDb.taskDao().updateTask(taskEntry);
                        finish();
                    }
                });
            }
        }
        else
            Toast.makeText(this, "One or more fields are empty", Toast.LENGTH_LONG).show();
    }


    public int getPriorityFromViews() {
        int priority = 1;
        int checkedId = ((RadioGroup) findViewById(R.id.radioGroup)).getCheckedRadioButtonId();
        switch (checkedId) {
            case R.id.radButton1:
                priority = PRIORITY_HIGH;
                break;
            case R.id.radButton2:
                priority = PRIORITY_MEDIUM;
                break;
            case R.id.radButton3:
                priority = PRIORITY_LOW;
        }
        return priority;
    }
    public void setPriorityInViews(int priority) {
        switch (priority) {
            case PRIORITY_HIGH:
                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.radButton1);
                break;
            case PRIORITY_MEDIUM:
                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.radButton2);
                break;
            case PRIORITY_LOW:
                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.radButton3);
        }
    }
}
