package com.tuinite.timemanagement.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.tuinite.timemanagement.entities.Reminder;

import java.util.Date;
import java.util.List;

@androidx.room.Dao
public interface ReminderDao {

    //Insert Reminder
    @Insert
    void reminderInsertion(Reminder reminder);

    //Get Reaminders
    @Query("SELECT * FROM Reminder WHERE dateTime BETWEEN :dayStart AND :dayEnd")
    LiveData<List<Reminder>> getRemainder(Date dayStart, Date dayEnd);

    //Delete a Reminder
    @Delete
    void deleteReminder(Reminder reminder);

    //Delete All Remainders
    @Query("DELETE  FROM Reminder")
    void deleteAllReminders();

    @Query("SELECT * FROM Reminder WHERE reminderId=:reminderId")
    LiveData<List<Reminder>> getSingleReminder(int reminderId);

    @Update
    void updateReminder(Reminder reminder);


    //  @Query("UPDATE Reminder SET priority= :priority WHERE id= :uuid")
    //  public int updatePriority(int uuid,int priority);

 /*   public class RemainderUuid {
        @ColumnInfo(name = "uuid")
        public long uuid;
    }


    @Query("SELECT uuid FROM Reminder WHERE title = :title")
    LiveData<List<RemainderUuid>> getRemainderUuid(String title);*/
}






/*
What is the DAO?
A DAO (data access object) validates  SQL at compile-time and associates it with a method. In
Room DAO, we can  use handy annotations, like @Insert, to represent the most common database
operations! Room uses the DAO to create a clean API for your code.

The DAO must be an interface or abstract class. By default, all queries must be executed on a
separate thread.




 */
