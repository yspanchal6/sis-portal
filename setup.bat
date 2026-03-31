@echo off
echo ========================================
echo   Student Information System - Setup
echo ========================================
echo.

cd /d "%~dp0"

echo [1/4] Checking Java...
java -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Java not found. Please install JDK 17+
    pause
    exit /b 1
)

echo [2/4] Checking MySQL...
echo Make sure XAMPP MySQL is running on port 3308
echo.

echo [3/4] Setting up database...
call JavaWebApp\run_sql.bat
echo.

echo [4/4] Building application...
cd JavaWebApp
call mvnw.cmd clean package -DskipTests
cd ..

echo.
echo ========================================
echo   Setup Complete!
echo ========================================
echo.
echo To run the application:


@REM command for running project

echo   cd JavaWebApp
echo   java -jar target\student-information-system-web-1.0-SNAPSHOT.jar
echo.
echo Then open: http://localhost:8080/index.html
echo.
echo Login credentials:
echo   Admin:   admin / admin123
echo   Teacher: mrsmith / teach123
echo   Student: alice / alice123
echo.
pause
