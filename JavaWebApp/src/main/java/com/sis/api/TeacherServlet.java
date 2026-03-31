package com.sis.api;

import com.google.gson.Gson;
import com.sis.db.TeacherDAO;
import jakarta.servlet.http.*;
import java.io.*;
import java.util.*;

public class TeacherServlet extends HttpServlet {
    private final TeacherDAO teacherDAO = new TeacherDAO();
    private final Gson gson = new Gson();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        List<Map<String, Object>> teachers = teacherDAO.getAllTeachers();
        response.getWriter().print(gson.toJson(teachers));
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
            boolean success = teacherDAO.addTeacher(
                (String) data.get("name"),
                (String) data.get("email_id"),
                (String) data.get("qualification"),
                (String) data.get("phone_number"),
                (Double) data.get("salary"),
                (String) data.get("dept_name")
            );
            result.put("success", success);
        } else if ("update".equals(action)) {
            boolean success = teacherDAO.updateTeacher(
                ((Double) data.get("teacher_id")).intValue(),
                (String) data.get("name"),
                (String) data.get("email_id"),
                (String) data.get("qualification"),
                (String) data.get("phone_number"),
                (Double) data.get("salary"),
                (String) data.get("dept_name")
            );
            result.put("success", success);
        } else if ("delete".equals(action)) {
            boolean success = teacherDAO.deleteTeacher(((Double) data.get("teacher_id")).intValue());
            result.put("success", success);
        }
        
        response.getWriter().print(gson.toJson(result));
    }
}
