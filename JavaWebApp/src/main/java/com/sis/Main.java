package com.sis;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.sis.api.*;
import com.sis.db.*;

public class Main {
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  Student Information System - Web App");
        System.out.println("========================================");
        System.out.println("Starting server on http://localhost:8080");
        System.out.println("========================================");
        
        Server server = new Server(8080);
        
        ServletContextHandler servletHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletHandler.setContextPath("/");
        
        String webappPath = System.getProperty("user.dir") + "/src/main/resources/webapp";
        System.out.println("Webapp path: " + webappPath);
        
        servletHandler.setResourceBase(webappPath);
        servletHandler.addServlet(new ServletHolder(new DefaultServlet()), "/");
        
        servletHandler.addServlet(new ServletHolder(new LoginServlet()), "/api/login");
        servletHandler.addServlet(new ServletHolder(new LogoutServlet()), "/api/logout");
        servletHandler.addServlet(new ServletHolder(new StudentServlet()), "/api/students/*");
        servletHandler.addServlet(new ServletHolder(new TeacherServlet()), "/api/teachers/*");
        servletHandler.addServlet(new ServletHolder(new CourseServlet()), "/api/courses/*");
        servletHandler.addServlet(new ServletHolder(new GradeServlet()), "/api/grades/*");
        servletHandler.addServlet(new ServletHolder(new EnrollmentServlet()), "/api/enrollments/*");
        servletHandler.addServlet(new ServletHolder(new DashboardServlet()), "/api/dashboard/*");
        servletHandler.addServlet(new ServletHolder(new StaticServlet()), "/api/static/*");
        
        server.setHandler(servletHandler);
        
        try {
            server.start();
            System.out.println("Server started successfully!");
            System.out.println("Open: http://localhost:8080/index.html");
            System.out.println("Press Ctrl+C to stop...");
            server.join();
        } catch (Exception e) {
            System.err.println("Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
