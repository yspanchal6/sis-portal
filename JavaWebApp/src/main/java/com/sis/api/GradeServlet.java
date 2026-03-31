package com.sis.api;

import com.google.gson.Gson;
import com.sis.db.GradeDAO;
import jakarta.servlet.http.*;
import java.io.*;
import java.util.*;

public class GradeServlet extends HttpServlet {
    private final GradeDAO gradeDAO = new GradeDAO();
    private final Gson gson = new Gson();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession(false);
        String role = session != null ? (String) session.getAttribute("role") : null;
        
        if ("STUDENT".equals(role)) {
            Integer rollNo = (Integer) session.getAttribute("studentRollNo");
            List<Map<String, Object>> grades = gradeDAO.getGradesByStudent(rollNo);
            response.getWriter().print(gson.toJson(grades));
        } else {
            List<Map<String, Object>> grades = gradeDAO.getAllGrades();
            response.getWriter().print(gson.toJson(grades));
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
        
        if ("add".equals(action)) {
            boolean success = gradeDAO.addGrade(
                ((Double) data.get("roll_no")).intValue(),
                (String) data.get("courseCode"),
                (Double) data.get("marks"),
                (String) data.get("semester"),
                ((Double) data.get("academicYear")).intValue()
            );
            result.put("success", success);
        } else if ("delete".equals(action)) {
            boolean success = gradeDAO.deleteGrade(((Double) data.get("id")).intValue());
            result.put("success", success);
        }
        
        response.getWriter().print(gson.toJson(result));
    }
}
