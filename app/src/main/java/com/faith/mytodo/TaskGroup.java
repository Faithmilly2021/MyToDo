package com.faith.mytodo;

import com.faith.mytodo.Model.ToDoModel;

import java.io.Serializable;
import java.util.List;

    public class TaskGroup implements Serializable {
        private String name;
        private String groupId;
        private String groupName;
        private String dueTime;
        private String duedate;
        private List<String> tasks;
        private List<Integer> colors;
        private boolean includeDueDateTime;
        private int iconResourceId;
        private boolean clickable;
        public boolean isCategory;


        public TaskGroup(String name, String groupId, String groupName, String dueTime, String duedate, List<String> tasks, List<Integer> colors, boolean includeDueDateTime, int iconResourceId, boolean clickable) {
            this.name = name;
            this.groupId = groupId;
            this.groupName = groupName;
            this.dueTime = dueTime;
            this.duedate = duedate;
            this.tasks = tasks;
            this.colors = colors;
            this.includeDueDateTime = includeDueDateTime;
            this.iconResourceId = iconResourceId;
            this.clickable = clickable;
        }


        public String getName() {
        return name;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getDueTime(){
            return dueTime;
    }

    public  String getDuedate(){
            return duedate;
    }

        public int getIconResourceId() {
            return iconResourceId;
        }

    public List<String> getTasks() {
        return tasks;
    }

    public List<Integer> getColors() {
        return colors;
    }

        public boolean isClickable() {
            return clickable;
        }

        public boolean isCategory() {
            return isCategory;
        }
    }
