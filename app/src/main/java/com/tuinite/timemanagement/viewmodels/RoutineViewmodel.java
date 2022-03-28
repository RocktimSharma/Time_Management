package com.tuinite.timemanagement.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.tuinite.timemanagement.Repositories.RoutineRepository;
import com.tuinite.timemanagement.entities.Routine;
import com.tuinite.timemanagement.relations.RoutineWithRoutineTask;

import java.util.List;

public class RoutineViewmodel extends AndroidViewModel {

    private RoutineRepository routineRepository;
    private LiveData<List<Routine>> routineLiveData;
    private LiveData<List<RoutineWithRoutineTask>> routineWithroutineTasks;

    public RoutineViewmodel(@NonNull Application application) {
        super(application);
        routineRepository = new RoutineRepository(application);
        routineLiveData = routineRepository.getRoutine();

    }

    public int routineInsertion(Routine routine) {
        return routineRepository.routineInsertion(routine);


    }

    public void deleteRoutine(Routine routine) {
        routineRepository.deleteRoutine(routine);
    }

    public LiveData<List<Routine>> getRoutine() {
        return routineLiveData;
    }


    public LiveData<List<RoutineWithRoutineTask>> getRoutinewithRoutineTasks(int routineId) {
        return routineRepository.getRoutineWithroutineTasks(routineId);
    }

    public void updateRoutineStatus(int routineId, boolean isActive) {
        routineRepository.updateRoutineStatus(routineId, isActive);
    }

    public void updateRoutine(Routine routine) {
        routineRepository.updateRoutine(routine);
    }
}
