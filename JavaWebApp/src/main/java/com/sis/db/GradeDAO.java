package com.sis.db;

import java.sql.*;
import java.util.*;

public class GradeDAO {
    
    public List<Map<String, Object>> getAllGrades() {
        List<Map<String, Object>> grades = new ArrayList<>();
        String sql = "SELECT g.*, s.name as student_name, c.course_name FROM grades g " +
                     "JOIN student s ON g.roll_no = s.roll_no " +
                     "JOIN course c ON g.courseCode = c.courseCode " +
                     "ORDER BY g.id DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Map<String, Object> grade = new HashMap<>();
                grade.put("id", rs.getInt("id"));
                grade.put("roll_no", rs.getInt("roll_no"));
                grade.put("student_name", rs.getString("student_name"));
                grade.put("courseCode", rs.getString("courseCode"));
                grade.put("course_name", rs.getString("course_name"));
                grade.put("marks", rs.getDouble("marks"));
                grade.put("semester", rs.getString("semester"));
                grade.put("academicYear", rs.getInt("academicYear"));
                grades.add(grade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return grades;
    }
    
    public List<Map<String, Object>> getGradesByStudent(int rollNo) {
        List<Map<String, Object>> grades = new ArrayList<>();
        String sql = "SELECT g.*, c.course_name FROM grades g " +
                     "JOIN course c ON g.courseCode = c.courseCode " +
                     "WHERE g.roll_no = ? ORDER BY g.academicYear DESC, g.semester";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, rollNo);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> grade = new HashMap<>();
                grade.put("id", rs.getInt("id"));
                grade.put("courseCode", rs.getString("courseCode"));
                grade.put("course_name", rs.getString("course_name"));
                grade.put("marks", rs.getDouble("marks"));
                grade.put("semester", rs.getString("semester"));
                grade.put("academicYear", rs.getInt("academicYear"));
                grades.add(grade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return grades;
    }
    
    public boolean addGrade(int rollNo, String courseCode, double marks, String semester, int year) {
        String sql = "INSERT INTO grades (roll_no, courseCode, marks, semester, academicYear) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, rollNo);
            pstmt.setString(2, courseCode);
            pstmt.setDouble(3, marks);
            pstmt.setString(4, semester);
            pstmt.setInt(5, year);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean deleteGrade(int id) {
        String sql = "DELETE FROM grades WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
