package com.sis.api;

import com.google.gson.Gson;
import com.sis.db.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.util.*;

public class DashboardServlet extends HttpServlet {
    private final Gson gson = new Gson();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession(false);
        
        Map<String, Object> result = new HashMap<>();
        
        if (session == null || session.getAttribute("user") == null) {
            result.put("loggedIn", false);
            response.getWriter().print(gson.toJson(result));
            return;
        }
        
        result.put("loggedIn", true);
        result.put("role", session.getAttribute("role"));
        result.put("fullName", session.getAttribute("fullName"));
        
        StudentDAO studentDAO = new StudentDAO();
        TeacherDAO teacherDAO = new TeacherDAO();
        CourseDAO courseDAO = new CourseDAO();
        EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
        
        result.put("totalStudents", studentDAO.getAllStudents().size());
        result.put("totalTeachers", teacherDAO.getAllTeachers().size());
        result.put("totalCourses", courseDAO.getAllCourses().size());
        result.put("totalEnrollments", enrollmentDAO.getAllEnrollments().size());
        
        response.getWriter().print(gson.toJson(result));
    }
}
