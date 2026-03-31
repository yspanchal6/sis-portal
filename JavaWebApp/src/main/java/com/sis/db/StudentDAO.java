package com.sis.db;

import java.sql.*;
import java.util.*;

public class StudentDAO {
    
    public List<Map<String, Object>> getAllStudents() {
        List<Map<String, Object>> students = new ArrayList<>();
        String sql = "SELECT * FROM student ORDER BY roll_no DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                students.add(extractStudent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }
    
    public Map<String, Object> getStudentByRollNo(int rollNo) {
        String sql = "SELECT * FROM student WHERE roll_no = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, rollNo);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractStudent(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean addStudent(int rollNo, String name, int age, String username, String password,
                             String address, String caste, String gender, String cls, 
                             String religion, String email, String phone, int enrollmentYear) {
        String sql = "INSERT INTO student (roll_no, name, age, username, password, address, caste, gender, class, religion, email, phone_number, enrollmentYear) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, rollNo);
            pstmt.setString(2, name);
            pstmt.setInt(3, age);
            pstmt.setString(4, username);
            pstmt.setString(5, password);
            pstmt.setString(6, address);
            pstmt.setString(7, caste);
            pstmt.setString(8, gender);
            pstmt.setString(9, cls);
            pstmt.setString(10, religion);
            pstmt.setString(11, email);
            pstmt.setString(12, phone);
            pstmt.setInt(13, enrollmentYear);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Add Student Error: " + e.getMessage());
        }
        return false;
    }
    
    public boolean updateStudent(int rollNo, String name, int age, String address, 
                                String caste, String gender, String cls,
                                String religion, String email, String phone, int enrollmentYear) {
        String sql = "UPDATE student SET name=?, age=?, address=?, caste=?, gender=?, class=?, religion=?, email=?, phone_number=?, enrollmentYear=? WHERE roll_no=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setString(3, address);
            pstmt.setString(4, caste);
            pstmt.setString(5, gender);
            pstmt.setString(6, cls);
            pstmt.setString(7, religion);
            pstmt.setString(8, email);
            pstmt.setString(9, phone);
            pstmt.setInt(10, enrollmentYear);
            pstmt.setInt(11, rollNo);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean deleteStudent(int rollNo) {
        String sql = "DELETE FROM student WHERE roll_no = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, rollNo);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private Map<String, Object> extractStudent(ResultSet rs) throws SQLException {
        Map<String, Object> student = new HashMap<>();
        student.put("roll_no", rs.getInt("roll_no"));
        student.put("name", rs.getString("name"));
        student.put("age", rs.getInt("age"));
        student.put("username", rs.getString("username"));
        student.put("address", rs.getString("address"));
        student.put("caste", rs.getString("caste"));
        student.put("gender", rs.getString("gender"));
        student.put("class", rs.getString("class"));
        student.put("religion", rs.getString("religion"));
        student.put("email", rs.getString("email"));
        student.put("phone_number", rs.getString("phone_number"));
        student.put("enrollmentYear", rs.getInt("enrollmentYear"));
        return student;
    }
}
