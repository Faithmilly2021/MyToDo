package com.faith.mytodo;

import com.faith.mytodo.Model.ToDoModel;

public interface ToDoActivity {

        void updateTaskStatus(ToDoModel toDoModel, boolean isChecked);
    }


