/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.User;
import util.DatabaseConnection;
import java.sql.*;

/**
 *
 * @author Administrator
 */
public class UserController {
    // Kiểm tra xem tên đăng nhập đã tồn tại chưa
    public boolean isUsernameExists(String username) {
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection(); // Sử dụng DatabaseConnection thay vì DriverManager
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0; // Trả về true nếu tên đăng nhập đã tồn tại
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Xử lý lỗi kết nối hoặc truy vấn
            return false; // Trả về false nếu có lỗi (an toàn mặc định)
        }
        return false; // Trường hợp không có kết quả
    }
    
    // Đăng nhập
    public User dangNhap(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password); // Lưu ý: Nên mã hóa password trong thực tế
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("email")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Đăng ký người dùng mới
    public void dangKy(String username, String password, String email) {
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password); // Lưu ý: Nên mã hóa password trong thực tế
            pstmt.setString(3, email);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}