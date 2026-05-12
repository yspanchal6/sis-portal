// ===== Teacher Dashboard Logic =====
let courses = [];

document.addEventListener('DOMContentLoaded', async () => {
    const auth = await checkAuth();
    if (!auth) return;
    
    document.getElementById('userName').textContent = auth.fullName;
    
    await loadData();
    setupForms();
});

async function loadData() {
    const dashboard = await API.getDashboard();
    document.getElementById('myCourses').textContent = dashboard.totalCourses;
    document.getElementById('totalStudents').textContent = dashboard.totalStudents;
    
    courses = await API.getCourses();
    loadCourses();
    loadGrades();
}

function loadCourses() {
    const tbody = document.querySelector('#coursesTable tbody');
    tbody.innerHTML = '';
    
    courses.forEach(c => {
        tbody.innerHTML += `
            <tr>
                <td><strong>${c.courseCode}</strong></td>
                <td>${c.course_name}</td>
                <td>${c.credits}</td>
                <td>${c.capacity}</td>
                <td>${c.enrolledCount}</td>
            </tr>
        `;
    });
}

async function loadGrades() {
    const grades = await API.getGrades();
    const tbody = document.querySelector('#gradesTable tbody');
    tbody.innerHTML = '';
    
    grades.forEach(g => {
        const gradeClass = getGradeColor(g.marks);
        tbody.innerHTML += `
            <tr>
                <td>${g.student_name} (${g.roll_no})</td>
                <td>${g.course_name}</td>
                <td>${g.marks}</td>
                <td class="${gradeClass}">${getGradeLetter(g.marks)}</td>
                <td>${g.semester}</td>
                <td>${g.academicYear}</td>
            </tr>
        `;
    });
}

function setupForms() {
    populateGradeSelects();
    
    document.getElementById('addGradeForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        const data = Object.fromEntries(formData);
        data.roll_no = parseInt(data.roll_no);
        data.marks = parseFloat(data.marks);
        data.academicYear = parseInt(data.academicYear);
        
        const result = await API.saveGrade(data);
        if (result.success) {
            hideModal('addGradeModal');
            loadGrades();
            e.target.reset();
        } else {
            alert('Failed to add grade');
        }
    });
}

async function populateGradeSelects() {
    const students = await API.getStudents();
    const courseSelect = document.getElementById('gradeCourseSelect');
    const studentSelect = document.getElementById('gradeStudentSelect');
    
    studentSelect.innerHTML = '<option value="">Select Student</option>';
    students.forEach(s => {
        studentSelect.innerHTML += `<option value="${s.roll_no}">${s.roll_no} - ${s.name}</option>`;
    });
    
    courseSelect.innerHTML = '<option value="">Select Course</option>';
    courses.forEach(c => {
        courseSelect.innerHTML += `<option value="${c.courseCode}">${c.courseCode} - ${c.course_name}</option>`;
    });
}
