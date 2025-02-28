/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author Administrator
 */
public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/student_task_manager";
    private static final String USER = "root";
    private static final String PASSWORD = "123456a@";
    
    public static Connection getConnection(){
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }
}
