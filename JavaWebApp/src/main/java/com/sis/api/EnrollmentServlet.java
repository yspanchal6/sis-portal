package com.sis.api;

import com.google.gson.Gson;
import com.sis.db.EnrollmentDAO;
import jakarta.servlet.http.*;
import java.io.*;
import java.util.*;

public class EnrollmentServlet extends HttpServlet {
    private final EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
    private final Gson gson = new Gson();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession(false);
        String role = session != null ? (String) session.getAttribute("role") : null;
        
        if ("STUDENT".equals(role)) {
            Integer rollNo = (Integer) session.getAttribute("studentRollNo");
            List<Map<String, Object>> enrollments = enrollmentDAO.getEnrollmentsByStudent(rollNo);
            response.getWriter().print(gson.toJson(enrollments));
        } else {
            List<Map<String, Object>> enrollments = enrollmentDAO.getAllEnrollments();
            response.getWriter().print(gson.toJson(enrollments));
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        
        BufferedReader reader = request.getReader();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        
        Map<String, Object> data = gson.fromJson(sb.toString(), Map.class);
        String action = (String) data.get("action");
        
        Map<String, Object> result = new HashMap<>();
        
        if ("enroll".equals(action)) {
            boolean success = enrollmentDAO.enrollStudent(
                ((Double) data.get("roll_no")).intValue(),
                (String) data.get("courseCode"),
                "Active"
            );
            result.put("success", success);
        } else if ("delete".equals(action)) {
            boolean success = enrollmentDAO.deleteEnrollment(((Double) data.get("id")).intValue());
            result.put("success", success);
        }
        
        response.getWriter().print(gson.toJson(result));
    }
}
