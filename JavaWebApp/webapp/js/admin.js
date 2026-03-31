// ===== Admin Dashboard Logic =====
let students = [];
let teachers = [];
let courses = [];

document.addEventListener('DOMContentLoaded', async () => {
    const auth = await checkAuth();
    if (!auth) return;
    
    document.getElementById('userName').textContent = auth.fullName;
    
    loadDashboard();
    loadStudents();
    loadTeachers();
    loadCourses();
    loadEnrollments();
    loadGrades();
    
    setupForms();
});

async function loadDashboard() {
    const data = await API.getDashboard();
    document.getElementById('totalStudents').textContent = data.totalStudents;
    document.getElementById('totalTeachers').textContent = data.totalTeachers;
    document.getElementById('totalCourses').textContent = data.totalCourses;
    document.getElementById('totalEnrollments').textContent = data.totalEnrollments;
}

async function loadStudents() {
    students = await API.getStudents();
    const tbody = document.querySelector('#studentsTable tbody');
    tbody.innerHTML = '';
    
    students.forEach(s => {
        tbody.innerHTML += `
            <tr>
                <td>${s.roll_no}</td>
                <td>${s.name}</td>
                <td>${s.class || '-'}</td>
                <td>${s.email}</td>
                <td>${s.phone_number || '-'}</td>
                <td>
                    <button class="btn btn-danger btn-sm" onclick="deleteStudent(${s.roll_no})">Delete</button>
                </td>
            </tr>
        `;
    });
}

async function loadTeachers() {
    teachers = await API.getTeachers();
    const tbody = document.querySelector('#teachersTable tbody');
    tbody.innerHTML = '';
    
    teachers.forEach(t => {
        tbody.innerHTML += `
            <tr>
                <td>${t.teacher_id}</td>
                <td>${t.name}</td>
                <td>${t.email_id}</td>
                <td>${t.dept_name}</td>
                <td>${t.qualification || '-'}</td>
                <td>
                    <button class="btn btn-danger btn-sm" onclick="deleteTeacher(${t.teacher_id})">Delete</button>
                </td>
            </tr>
        `;
    });
}

async function loadCourses() {
    courses = await API.getCourses();
    const tbody = document.querySelector('#coursesTable tbody');
    tbody.innerHTML = '';
    
    courses.forEach(c => {
        tbody.innerHTML += `
            <tr>
                <td><strong>${c.courseCode}</strong></td>
                <td>${c.course_name}</td>
                <td>${c.teacher_name || '-'}</td>
                <td>${c.credits}</td>
                <td>${c.capacity}</td>
                <td>${c.enrolledCount}</td>
                <td>
                    <button class="btn btn-danger btn-sm" onclick="deleteCourse('${c.courseCode}')">Delete</button>
                </td>
            </tr>
        `;
    });
}

async function loadEnrollments() {
    const enrollments = await API.getEnrollments();
    const tbody = document.querySelector('#enrollmentsTable tbody');
    tbody.innerHTML = '';
    
    enrollments.forEach(e => {
        tbody.innerHTML += `
            <tr>
                <td>${e.id}</td>
                <td>${e.student_name} (${e.roll_no})</td>
                <td>${e.course_name}</td>
                <td>${e.enrollment_date}</td>
                <td><span class="status status-active">${e.status}</span></td>
                <td>
                    <button class="btn btn-danger btn-sm" onclick="deleteEnrollment(${e.id})">Remove</button>
                </td>
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
                <td>${g.id}</td>
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
    // Add Student Form
    document.getElementById('addStudentForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        const data = Object.fromEntries(formData);
        data.age = parseInt(data.age);
        data.roll_no = parseInt(data.roll_no);
        data.enrollmentYear = parseInt(data.enrollmentYear);
        
        const result = await API.saveStudent(data);
        if (result.success) {
            hideModal('addStudentModal');
            loadStudents();
            e.target.reset();
        } else {
            alert('Failed to add student');
        }
    });
    
    // Add Teacher Form
    document.getElementById('addTeacherForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        const data = Object.fromEntries(formData);
        data.salary = parseFloat(data.salary);
        
        const result = await API.saveTeacher(data);
        if (result.success) {
            hideModal('addTeacherModal');
            loadTeachers();
            e.target.reset();
        } else {
            alert('Failed to add teacher');
        }
    });
    
    // Add Course Form
    document.getElementById('addCourseForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        const data = Object.fromEntries(formData);
        data.capacity = parseInt(data.capacity);
        data.teacherId = 1;
        
        const result = await API.saveCourse(data);
        if (result.success) {
            hideModal('addCourseModal');
            loadCourses();
            e.target.reset();
        } else {
            alert('Failed to add course');
        }
    });
    
    // Enroll Form
    populateEnrollSelects();
    document.getElementById('enrollForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        const formData = new FormData(e.target);
        const data = Object.fromEntries(formData);
        data.roll_no = parseInt(data.roll_no);
        
        const result = await API.saveEnrollment(data);
        if (result.success) {
            hideModal('enrollModal');
            loadEnrollments();
        } else {
            alert('Failed to enroll student');
        }
    });
    
    // Grade Form
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

function populateEnrollSelects() {
    const studentSelect = document.getElementById('enrollStudentSelect');
    const courseSelect = document.getElementById('enrollCourseSelect');
    
    studentSelect.innerHTML = '<option value="">Select Student</option>';
    students.forEach(s => {
        studentSelect.innerHTML += `<option value="${s.roll_no}">${s.roll_no} - ${s.name}</option>`;
    });
    
    courseSelect.innerHTML = '<option value="">Select Course</option>';
    courses.forEach(c => {
        courseSelect.innerHTML += `<option value="${c.courseCode}">${c.courseCode} - ${c.course_name}</option>`;
    });
}

function populateGradeSelects() {
    const studentSelect = document.getElementById('gradeStudentSelect');
    const courseSelect = document.getElementById('gradeCourseSelect');
    
    studentSelect.innerHTML = '<option value="">Select Student</option>';
    students.forEach(s => {
        studentSelect.innerHTML += `<option value="${s.roll_no}">${s.roll_no} - ${s.name}</option>`;
    });
    
    courseSelect.innerHTML = '<option value="">Select Course</option>';
    courses.forEach(c => {
        courseSelect.innerHTML += `<option value="${c.courseCode}">${c.courseCode} - ${c.course_name}</option>`;
    });
}

async function deleteStudent(rollNo) {
    if (confirm('Are you sure you want to delete this student?')) {
        const result = await API.saveStudent({ action: 'delete', roll_no: rollNo });
        if (result.success) {
            loadStudents();
        }
    }
}

async function deleteTeacher(teacherId) {
    if (confirm('Are you sure you want to delete this teacher?')) {
        const result = await API.saveTeacher({ action: 'delete', teacher_id: teacherId });
        if (result.success) {
            loadTeachers();
        }
    }
}

async function deleteCourse(courseCode) {
    if (confirm('Are you sure you want to delete this course?')) {
        const result = await API.saveCourse({ action: 'delete', courseCode });
        if (result.success) {
            loadCourses();
        }
    }
}

async function deleteEnrollment(id) {
    if (confirm('Are you sure you want to remove this enrollment?')) {
        const result = await API.saveEnrollment({ action: 'delete', id });
        if (result.success) {
            loadEnrollments();
        }
    }
}
