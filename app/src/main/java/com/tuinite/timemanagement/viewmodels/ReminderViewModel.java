package com.tuinite.timemanagement.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.tuinite.timemanagement.Repositories.ReminderRepository;
import com.tuinite.timemanagement.entities.Reminder;

import java.util.Date;
import java.util.List;

public class ReminderViewModel extends AndroidViewModel {

    private ReminderRepository reminderRepository;
    private LiveData<List<Reminder>> remainderLiveData;

    public ReminderViewModel(@NonNull Application application) {
        super(application);
        reminderRepository = new ReminderRepository(application);
    }

    public void reminderInsertion(Reminder reminder) {
        reminderRepository.reminderInsertion(reminder);
    }

    public void deleteReminder(Reminder reminder) {
        reminderRepository.deleteReminder(reminder);
    }

    public void deleteAllReminders() {
        reminderRepository.deleteAllReminders();
    }

    public LiveData<List<Reminder>> getReminder(Date dayStart, Date dayEnd) {
        return reminderRepository.getRemainder(dayStart, dayEnd);
    }

    public LiveData<List<Reminder>> getSingleReminder(int reminderId) {
        return reminderRepository.getSingleReminder(reminderId);
    }

    public void updateReminder(Reminder reminder) {
        reminderRepository.updateReminder(reminder);
    }

}
