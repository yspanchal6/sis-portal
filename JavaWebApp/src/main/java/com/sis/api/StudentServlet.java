package com.sis.api;

import com.google.gson.Gson;
import com.sis.db.StudentDAO;
import jakarta.servlet.http.*;
import java.io.*;
import java.util.*;

public class StudentServlet extends HttpServlet {
    private final StudentDAO studentDAO = new StudentDAO();
    private final Gson gson = new Gson();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String pathInfo = request.getPathInfo();
        PrintWriter out = response.getWriter();
        
        if (pathInfo != null && pathInfo.length() > 1) {
            String idStr = pathInfo.substring(1);
            try {
                int rollNo = Integer.parseInt(idStr);
                Map<String, Object> student = studentDAO.getStudentByRollNo(rollNo);
                out.print(gson.toJson(student));
            } catch (NumberFormatException e) {
                out.print("{\"error\": \"Invalid roll number\"}");
            }
        } else {
            List<Map<String, Object>> students = studentDAO.getAllStudents();
            out.print(gson.toJson(students));
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
            boolean success = studentDAO.addStudent(
                ((Double) data.get("roll_no")).intValue(),
                (String) data.get("name"),
                ((Double) data.get("age")).intValue(),
                (String) data.get("username"),
                (String) data.get("password"),
                (String) data.get("address"),
                (String) data.get("caste"),
                (String) data.get("gender"),
                (String) data.get("class"),
                (String) data.get("religion"),
                (String) data.get("email"),
                (String) data.get("phone_number"),
                ((Double) data.get("enrollmentYear")).intValue()
            );
            result.put("success", success);
        } else if ("update".equals(action)) {
            boolean success = studentDAO.updateStudent(
                ((Double) data.get("roll_no")).intValue(),
                (String) data.get("name"),
                ((Double) data.get("age")).intValue(),
                (String) data.get("address"),
                (String) data.get("caste"),
                (String) data.get("gender"),
                (String) data.get("class"),
                (String) data.get("religion"),
                (String) data.get("email"),
                (String) data.get("phone_number"),
                ((Double) data.get("enrollmentYear")).intValue()
            );
            result.put("success", success);
        } else if ("delete".equals(action)) {
            boolean success = studentDAO.deleteStudent(((Double) data.get("roll_no")).intValue());
            result.put("success", success);
        }
        
        response.getWriter().print(gson.toJson(result));
    }
}
