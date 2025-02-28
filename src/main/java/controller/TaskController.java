/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import model.Task;
import util.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class TaskController {
    public boolean addTask(String taskName, String description, LocalDate deadline, 
                           String priority, String status, int userId, int subjectId) {
        String sql = "INSERT INTO tasks (task_name, description, deadline, priority, status, user_id, subject_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, taskName);
            pstmt.setString(2, description);
            pstmt.setDate(3, Date.valueOf(deadline));
            pstmt.setString(4, priority);
            pstmt.setString(5, status);
            pstmt.setInt(6, userId);
            pstmt.setInt(7, subjectId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // True nếu thêm thành công
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm công việc: " + e.getMessage());
            e.printStackTrace();
            return false; // False nếu có lỗi
        }
    }
    
    public void updateTask(int taskId, String taskName, String description, LocalDate deadline, 
                           String priority, String status) {
        String sql = "UPDATE tasks SET task_name = ?, description = ?, deadline = ?, priority = ?, status = ? " +
                     "WHERE task_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, taskName);
            pstmt.setString(2, description);
            pstmt.setDate(3, Date.valueOf(deadline));
            pstmt.setString(4, priority);
            pstmt.setString(5, status);
            pstmt.setInt(6, taskId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void deleteTask(int taskId) {
        String sql = "DELETE FROM tasks WHERE task_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, taskId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void markAsCompleted(int taskId) {
        String sql = "UPDATE tasks SET status = 'Đã hoàn thành' WHERE task_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, taskId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<Task> getTasksByUser(int userId) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                tasks.add(new Task(
                    rs.getInt("task_id"),
                    rs.getString("task_name"),
                    rs.getString("description"),
                    rs.getDate("deadline").toLocalDate(),
                    rs.getString("priority"),
                    rs.getString("status"),
                    rs.getInt("user_id"),
                    rs.getInt("subject_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }
}
