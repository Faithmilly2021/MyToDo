package com.faith.mytodo;

import com.faith.mytodo.Model.ToDoModel;

import java.io.Serializable;
import java.util.List;

    public class TaskGroup implements Serializable {
        private String name;
        private String groupId;
        private String groupName;
        public String userId;
        private List<String> tasks;
        private int iconResourceId;
        private boolean clickable;
        public boolean isCategory;


        public TaskGroup(String name, String groupId, String groupName, List<String> tasks, List<Integer> colors, int iconResourceId, boolean clickable, String userId, boolean isCategory) {
            this.name = name;
            this.groupId = groupId;
            this.groupName = groupName;
            this.tasks = tasks;
            this.iconResourceId = iconResourceId;
            this.clickable = clickable;
            this.userId = userId;
             this.isCategory = isCategory;
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

    public String getUserId(){
            return userId;
    }
        public int getIconResourceId() {
            return iconResourceId;
        }


    public List<String> getTasks() {
        return tasks;
    }

        public boolean isClickable() {
            return clickable;
        }

        public boolean isCategory() {
            return isCategory;
        }
    }
