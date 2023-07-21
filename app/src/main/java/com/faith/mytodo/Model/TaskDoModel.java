package com.faith.mytodo.Model;

public class TaskDoModel extends TaskId {

    private String task;
    private int status;

    public TaskDoModel(String task, int status) {
        this.task = task;
        this.status = status;

    }

    public TaskDoModel() {

    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
