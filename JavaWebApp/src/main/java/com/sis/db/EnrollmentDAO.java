package com.sis.db;

import java.sql.*;
import java.util.*;

public class EnrollmentDAO {
    
    public List<Map<String, Object>> getAllEnrollments() {
        List<Map<String, Object>> enrollments = new ArrayList<>();
        String sql = "SELECT e.*, s.name as student_name, c.course_name FROM enroll e " +
                     "JOIN student s ON e.roll_no = s.roll_no " +
                     "JOIN course c ON e.courseCode = c.courseCode " +
                     "ORDER BY e.id DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Map<String, Object> enrollment = new HashMap<>();
                enrollment.put("id", rs.getInt("id"));
                enrollment.put("roll_no", rs.getInt("roll_no"));
                enrollment.put("student_name", rs.getString("student_name"));
                enrollment.put("courseCode", rs.getString("courseCode"));
                enrollment.put("course_name", rs.getString("course_name"));
                enrollment.put("enrollment_date", rs.getDate("enrollment_date"));
                enrollment.put("status", rs.getString("status"));
                enrollments.add(enrollment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return enrollments;
    }
    
    public List<Map<String, Object>> getEnrollmentsByStudent(int rollNo) {
        List<Map<String, Object>> enrollments = new ArrayList<>();
        String sql = "SELECT e.*, c.course_name FROM enroll e " +
                     "JOIN course c ON e.courseCode = c.courseCode " +
                     "WHERE e.roll_no = ? ORDER BY e.enrollment_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, rollNo);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> enrollment = new HashMap<>();
                enrollment.put("id", rs.getInt("id"));
                enrollment.put("courseCode", rs.getString("courseCode"));
                enrollment.put("course_name", rs.getString("course_name"));
                enrollment.put("enrollment_date", rs.getDate("enrollment_date"));
                enrollment.put("status", rs.getString("status"));
                enrollments.add(enrollment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return enrollments;
    }
    
    public boolean enrollStudent(int rollNo, String courseCode, String status) {
        String checkSql = "SELECT * FROM enroll WHERE roll_no = ? AND courseCode = ?";
        String insertSql = "INSERT INTO enroll (roll_no, courseCode, enrollment_date, status) VALUES (?, ?, CURDATE(), ?)";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, rollNo);
            checkStmt.setString(2, courseCode);
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next()) {
                return false;
            }
            
            PreparedStatement insertStmt = conn.prepareStatement(insertSql);
            insertStmt.setInt(1, rollNo);
            insertStmt.setString(2, courseCode);
            insertStmt.setString(3, status);
            
            if (insertStmt.executeUpdate() > 0) {
                PreparedStatement updateCourse = conn.prepareStatement(
                    "UPDATE course SET enrolledCount = enrolledCount + 1 WHERE courseCode = ?");
                updateCourse.setString(1, courseCode);
                updateCourse.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean deleteEnrollment(int id) {
        String sql = "DELETE FROM enroll WHERE id = ?";
        
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
