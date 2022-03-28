package com.tuinite.timemanagement.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.tuinite.timemanagement.Repositories.RoutineProgressRepository;
import com.tuinite.timemanagement.entities.RoutineProgress;

import java.util.Date;
import java.util.List;

public class RoutineProgressViewModel extends AndroidViewModel {
    RoutineProgressRepository routineProgressRepository;

    private LiveData<List<RoutineProgress>> routineProgressLiveData;

    public RoutineProgressViewModel(@NonNull Application application) {
        super(application);
        routineProgressRepository = new RoutineProgressRepository(application);
    }

    public void routineProgressInsertion(RoutineProgress routineProgress) {
        routineProgressRepository.routineProgressInsertion(routineProgress);
    }

    public LiveData<List<RoutineProgress>> getRoutineProgress(Date progressDate, int routineId) {
        return routineProgressRepository.getRoutineProgress(progressDate, routineId);
    }

    public void updateEndTime(Date progressEndTime, Date progressDate, int taskId) {
        routineProgressRepository.updateEndtime(progressEndTime, progressDate, taskId);
    }

}
