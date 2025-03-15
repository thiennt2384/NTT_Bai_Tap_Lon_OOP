/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Administrator
 */
public class Subject {
    private int subjectId;
    private String subjectName;
    public Subject(int subjectId, String subjectName) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
    }
    public int getSubjectId() {
        return subjectId;
    }
    public String getSubjectName() {
        return subjectName;
    }
    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
    
}
