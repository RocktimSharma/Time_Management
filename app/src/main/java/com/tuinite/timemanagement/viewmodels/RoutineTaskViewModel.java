package com.tuinite.timemanagement.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.tuinite.timemanagement.Repositories.RoutineTaskRepository;
import com.tuinite.timemanagement.entities.RoutineTasks;

import java.util.Date;
import java.util.List;

public class RoutineTaskViewModel extends AndroidViewModel {

    RoutineTaskRepository routineTaskRepository;

    LiveData<List<RoutineTasks>> routineTaskLiveData;


    public RoutineTaskViewModel(@NonNull Application application) {
        super(application);
        routineTaskRepository = new RoutineTaskRepository(application);

    }

    public void routineTaskInsertion(RoutineTasks routineTasks) {
        routineTaskRepository.routineTaskInsertion(routineTasks);
    }

    public LiveData<List<RoutineTasks>> getRoutineTasks(int routineId) {
        return routineTaskRepository.getRoutineTasks(routineId);
    }

    public LiveData<List<RoutineTasks>> getSingleRoutineTask(int taskId) {
        return routineTaskRepository.getSingleRoutineTask(taskId);
    }

    public LiveData<List<RoutineTasks>> getNextTask(Date endTime) {
        return routineTaskRepository.getNextTask(endTime);
    }

    public void updateRoutineTask(RoutineTasks routineTask) {
        routineTaskRepository.updateRoutineTask(routineTask);
    }

    public void deleteRoutineTask(RoutineTasks routineTasks) {
        routineTaskRepository.deleteRoutineTask(routineTasks);
    }
}
