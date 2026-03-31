package com.sis.api;

import com.google.gson.Gson;
import com.sis.db.UserDAO;
import jakarta.servlet.http.*;
import java.io.*;
import java.util.*;

public class LoginServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();
    private final Gson gson = new Gson();
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        BufferedReader reader = request.getReader();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        
        Map<String, String> data = gson.fromJson(sb.toString(), Map.class);
        String username = data.get("username");
        String password = data.get("password");
        
        Map<String, Object> user = userDAO.authenticate(username, password);
        
        PrintWriter out = response.getWriter();
        if (user != null) {
            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);
            session.setAttribute("role", user.get("role"));
            session.setAttribute("fullName", user.get("fullName"));
            session.setAttribute("teacherId", user.get("teacherId"));
            session.setAttribute("studentRollNo", user.get("studentRollNo"));
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("user", user);
            out.print(gson.toJson(result));
        } else {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "Invalid username or password");
            out.print(gson.toJson(result));
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        Map<String, Object> result = new HashMap<>();
        
        if (session != null && session.getAttribute("user") != null) {
            result.put("loggedIn", true);
            result.put("user", session.getAttribute("user"));
            result.put("role", session.getAttribute("role"));
        } else {
            result.put("loggedIn", false);
        }
        
        response.setContentType("application/json");
        response.getWriter().print(gson.toJson(result));
    }
}
