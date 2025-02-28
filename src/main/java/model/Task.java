/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.time.LocalDate;
/**
 *
 * @author Administrator
 */
public class Task {
    private int taskId;
    private String taskName;
    private String description;
    private LocalDate deadline;
    private String priority;
    private String status;
    private int userId;
    private int subjectId;

    public Task(int taskId, String taskName, String description, LocalDate deadline, String priority, String status, int userId, int subjectId) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.description = description;
        this.deadline = deadline;
        this.priority = priority;
        this.status = status;
        this.userId = userId;
        this.subjectId = subjectId;
    }

    public int getTaskId() {
        return taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public String getPriority() {
        return priority;
    }

    public String getStatus() {
        return status;
    }

    public int getUserId() {
        return userId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

   
    
}
