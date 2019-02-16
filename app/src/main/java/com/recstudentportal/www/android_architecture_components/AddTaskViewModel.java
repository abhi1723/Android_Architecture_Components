package com.recstudentportal.www.android_architecture_components;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

public class AddTaskViewModel extends ViewModel {

    private LiveData<TaskEntry>taskEntryLiveData;
    public AddTaskViewModel(AppDatabase database,int task) {
     taskEntryLiveData=database.taskDao().loadTasksById(task);
    }

    public LiveData<TaskEntry> getTaskEntryLiveData() {
        return taskEntryLiveData;
    }
}
