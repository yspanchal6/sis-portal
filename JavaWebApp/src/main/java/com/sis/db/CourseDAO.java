package com.sis.db;

import java.sql.*;
import java.util.*;

public class CourseDAO {
    
    public List<Map<String, Object>> getAllCourses() {
        List<Map<String, Object>> courses = new ArrayList<>();
        String sql = "SELECT c.*, t.name as teacher_name FROM course c LEFT JOIN techer_db t ON c.teacherId = t.teacher_id ORDER BY c.courseCode DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                courses.add(extractCourse(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }
    
    public boolean addCourse(String courseCode, String courseName, int teacherId,
                            String description, String credits, int capacity) {
        String sql = "INSERT INTO course (courseCode, course_name, teacherId, description, credits, active, capacity, enrolledCount) VALUES (?, ?, ?, ?, ?, 1, ?, 0)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, courseCode);
            pstmt.setString(2, courseName);
            pstmt.setInt(3, teacherId);
            pstmt.setString(4, description);
            pstmt.setString(5, credits);
            pstmt.setInt(6, capacity);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean deleteCourse(String courseCode) {
        String sql = "DELETE FROM course WHERE courseCode = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, courseCode);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private Map<String, Object> extractCourse(ResultSet rs) throws SQLException {
        Map<String, Object> course = new HashMap<>();
        course.put("courseCode", rs.getString("courseCode"));
        course.put("course_name", rs.getString("course_name"));
        course.put("teacherId", rs.getInt("teacherId"));
        course.put("teacher_name", rs.getString("teacher_name"));
        course.put("description", rs.getString("description"));
        course.put("credits", rs.getString("credits"));
        course.put("active", rs.getInt("active"));
        course.put("capacity", rs.getInt("capacity"));
        course.put("enrolledCount", rs.getInt("enrolledCount"));
        return course;
    }
}
