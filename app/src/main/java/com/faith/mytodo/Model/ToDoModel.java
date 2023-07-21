package com.faith.mytodo.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ToDoModel implements Parcelable{
    private String task;
    private String id;
    private String group;
    private String groupName;
    private String CategoryName;
    private int status;
    private boolean isImportant;
    // private String categoryName;
    private List<String> tasks;

    public ToDoModel(String task, String id, String group, String groupName, String CategoryName, List<String> tasks, int status, boolean isImportant) {
        this.task = task;
        this.id = id;
        this.group = group;
        this.groupName = groupName;
        this.CategoryName = CategoryName;
        this.tasks = tasks;
        this.status = status;
        this.isImportant = isImportant;
    }

    public ToDoModel() {
        // Empty constructor required by Firebase Firestore.
    }

    public ToDoModel withId(String id) {
        this.id = id;
        return this;
    }

    public ToDoModel(Parcel in) {
        task = in.readString();
        id = in.readString();
        group = in.readString();
        groupName = in.readString();
        CategoryName = in.readString();
        tasks = in.createStringArrayList();
        status = in.readInt();
        isImportant = in.readByte() != 0;
    }
    public ToDoModel(String taskDetails) {
        // Initialize your fields here
        // For example:
        // this.taskDetails = taskDetails;
    }

    public static final Creator<ToDoModel> CREATOR = new Creator<ToDoModel>() {
        @Override
        public ToDoModel createFromParcel(Parcel in) {
            return new ToDoModel(in);
        }

        @Override
        public ToDoModel[] newArray(int size) {
            return new ToDoModel[size];
        }
    };

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public List<String> getTasks() {
        return tasks;
    }

    public void setTasks(List<String> tasks) {
        this.tasks = tasks;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        this.CategoryName = CategoryName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isImportant() {
        return isImportant;
    }

    public void setImportant(boolean important) {
        isImportant = important;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(task);
        dest.writeString(group);
        dest.writeString(groupName);
        dest.writeInt(status);
        dest.writeByte((byte) (isImportant ? 1 : 0));
    }
}

