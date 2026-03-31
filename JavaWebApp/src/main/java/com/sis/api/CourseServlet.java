package com.sis.api;

import com.google.gson.Gson;
import com.sis.db.CourseDAO;
import jakarta.servlet.http.*;
import java.io.*;
import java.util.*;

public class CourseServlet extends HttpServlet {
    private final CourseDAO courseDAO = new CourseDAO();
    private final Gson gson = new Gson();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        List<Map<String, Object>> courses = courseDAO.getAllCourses();
        response.getWriter().print(gson.toJson(courses));
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
            boolean success = courseDAO.addCourse(
                (String) data.get("courseCode"),
                (String) data.get("course_name"),
                ((Double) data.get("teacherId")).intValue(),
                (String) data.get("description"),
                (String) data.get("credits"),
                ((Double) data.get("capacity")).intValue()
            );
            result.put("success", success);
        } else if ("delete".equals(action)) {
            boolean success = courseDAO.deleteCourse((String) data.get("courseCode"));
            result.put("success", success);
        }
        
        response.getWriter().print(gson.toJson(result));
    }
}
