package com.sis.db;

import com.google.gson.Gson;
import java.sql.*;
import java.util.*;

public class UserDAO {
    private final Gson gson = new Gson();
    
    public Map<String, Object> authenticate(String username, String password) {
        String sql = "SELECT u.*, t.teacher_id, s.roll_no FROM users u " +
                     "LEFT JOIN techer_db t ON u.username = t.email_id " +
                     "LEFT JOIN student s ON u.username = s.username " +
                     "WHERE u.username = ? AND u.password = ? AND u.active = 1";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Map<String, Object> user = new HashMap<>();
                user.put("id", rs.getInt("id"));
                user.put("username", rs.getString("username"));
                user.put("role", rs.getString("role"));
                user.put("fullName", rs.getString("fullName"));
                user.put("email", rs.getString("email"));
                user.put("teacherId", rs.getObject("teacher_id"));
                user.put("studentRollNo", rs.getObject("roll_no"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Map<String, Object>> getAllUsers() {
        List<Map<String, Object>> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY id DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Map<String, Object> user = new HashMap<>();
                user.put("id", rs.getInt("id"));
                user.put("username", rs.getString("username"));
                user.put("role", rs.getString("role"));
                user.put("fullName", rs.getString("fullName"));
                user.put("email", rs.getString("email"));
                user.put("active", rs.getInt("active"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    
    public boolean register(String username, String password, String role, String fullName, String email) {
        String sql = "INSERT INTO users (username, password, role, fullName, email, active) VALUES (?, ?, ?, ?, ?, 1)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);
            pstmt.setString(4, fullName);
            pstmt.setString(5, email);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
