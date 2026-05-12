package com.sis.db;

import java.sql.*;
import java.util.*;

public class TeacherDAO {
    
    public List<Map<String, Object>> getAllTeachers() {
        List<Map<String, Object>> teachers = new ArrayList<>();
        String sql = "SELECT * FROM teacher_db ORDER BY teacher_id DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                teachers.add(extractTeacher(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return teachers;
    }
    
    public Map<String, Object> getTeacherById(int teacherId) {
        String sql = "SELECT * FROM teacher_db WHERE teacher_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, teacherId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractTeacher(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean addTeacher(String name, String email, String qualification, 
                            String phone, double salary, String dept) {
        String sql = "INSERT INTO teacher_db (name, email_id, qualification, phone_number, salary, dept_name) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, qualification);
            pstmt.setString(4, phone);
            pstmt.setDouble(5, salary);
            pstmt.setString(6, dept);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean updateTeacher(int teacherId, String name, String email, 
                                 String qualification, String phone, 
                                 double salary, String dept) {
        String sql = "UPDATE teacher_db SET name=?, email_id=?, qualification=?, phone_number=?, salary=?, dept_name=? WHERE teacher_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, qualification);
            pstmt.setString(4, phone);
            pstmt.setDouble(5, salary);
            pstmt.setString(6, dept);
            pstmt.setInt(7, teacherId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean deleteTeacher(int teacherId) {
        String sql = "DELETE FROM techer_db WHERE teacher_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, teacherId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private Map<String, Object> extractTeacher(ResultSet rs) throws SQLException {
        Map<String, Object> teacher = new HashMap<>();
        teacher.put("teacher_id", rs.getInt("teacher_id"));
        teacher.put("name", rs.getString("name"));
        teacher.put("email_id", rs.getString("email_id"));
        teacher.put("qualification", rs.getString("qualification"));
        teacher.put("phone_number", rs.getString("phone_number"));
        teacher.put("salary", rs.getDouble("salary"));
        teacher.put("dept_name", rs.getString("dept_name"));
        return teacher;
    }
}
