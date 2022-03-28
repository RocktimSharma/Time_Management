package com.tuinite.timemanagement.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.tuinite.timemanagement.entities.Routine;
import com.tuinite.timemanagement.entities.RoutineTasks;

import java.util.List;

public class RoutineWithRoutineTask {
    @Embedded
    public Routine routine;
    @Relation(
            parentColumn = "routineId",
            entityColumn = "taskRoutineId"
    )
    public List<RoutineTasks> routineTasksList;// or use simply 'List pets;'
}
