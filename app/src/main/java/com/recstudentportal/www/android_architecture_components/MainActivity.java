package com.recstudentportal.www.android_architecture_components;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.RecoverySystem;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.util.List;

public class MainActivity extends AppCompatActivity implements DisplayTaskAdapter.ItemClickListener {

    FloatingActionButton fabButton;
    RecyclerView recyclerView;
    DisplayTaskAdapter displayTaskAdapter;
    AppDatabase mDb;
    LiveData <List<TaskEntry>> taskEntries=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerViewTasks);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        displayTaskAdapter=new DisplayTaskAdapter(this,this);
        recyclerView.setAdapter(displayTaskAdapter);
        fabButton=(FloatingActionButton)findViewById(R.id.fab);
        mDb=AppDatabase.getInstance(this);
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,InsertNewGoals.class);
                startActivity(intent);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                 final int position=viewHolder.getAdapterPosition();
                  final List<TaskEntry> Tasks=displayTaskAdapter.findTasks();
                  AppExecutors.getInstance().diskIO().execute(new Runnable() {
                      @Override
                      public void run() {
                               mDb.taskDao().deleteTask(Tasks.get(position));
                      }
                  });
                 // updateUi();
            }
        }).attachToRecyclerView(recyclerView);
        updateUi();
    }


    void updateUi() {

        MainViewModel mainViewModel= ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getTasks().observe(this, new Observer<List<TaskEntry>>() {
            @Override
            public void onChanged(@Nullable List<TaskEntry> taskEntries) {
                displayTaskAdapter.getTasks(taskEntries);
            }
        });
          }

    @Override
    public void onItemClickListener(int itemId) {
        Intent intent = new Intent(MainActivity.this, InsertNewGoals.class);
        intent.putExtra("ids", itemId);
        startActivity(intent);
    }
}
