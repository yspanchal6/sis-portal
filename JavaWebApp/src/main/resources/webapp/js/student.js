// ===== Student Dashboard Logic =====
document.addEventListener('DOMContentLoaded', async () => {
    const auth = await checkAuth();
    if (!auth) return;
    
    document.getElementById('userName').textContent = auth.fullName;
    
    await loadData();
});

async function loadData() {
    const [grades, enrollments, dashboard] = await Promise.all([
        API.getGrades(),
        API.getEnrollments(),
        API.getDashboard()
    ]);
    
    // Dashboard stats
    document.getElementById('enrolledCourses').textContent = enrollments.length;
    document.getElementById('totalGrades').textContent = grades.length;
    
    // Load courses
    loadCourses(enrollments);
    
    // Load grades
    loadGrades(grades);
    
    // Load enrollments
    loadEnrollments(enrollments);
}

function loadCourses(enrollments) {
    const tbody = document.querySelector('#coursesTable tbody');
    tbody.innerHTML = '';
    
    enrollments.forEach(e => {
        tbody.innerHTML += `
            <tr>
                <td><strong>${e.courseCode}</strong></td>
                <td>${e.course_name}</td>
                <td><span class="status status-active">${e.status}</span></td>
            </tr>
        `;
    });
    
    if (enrollments.length === 0) {
        tbody.innerHTML = '<tr><td colspan="3" style="text-align:center;">No courses enrolled</td></tr>';
    }
}

function loadGrades(grades) {
    const tbody = document.querySelector('#gradesTable tbody');
    tbody.innerHTML = '';
    
    grades.forEach(g => {
        const gradeClass = getGradeColor(g.marks);
        tbody.innerHTML += `
            <tr>
                <td>${g.course_name}</td>
                <td>${g.marks}</td>
                <td class="${gradeClass}">${getGradeLetter(g.marks)}</td>
                <td>${g.semester}</td>
                <td>${g.academicYear}</td>
            </tr>
        `;
    });
    
    if (grades.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5" style="text-align:center;">No grades available</td></tr>';
    }
}

function loadEnrollments(enrollments) {
    const tbody = document.querySelector('#enrollmentsTable tbody');
    tbody.innerHTML = '';
    
    enrollments.forEach(e => {
        tbody.innerHTML += `
            <tr>
                <td>${e.course_name}</td>
                <td>${e.enrollment_date}</td>
                <td><span class="status status-active">${e.status}</span></td>
            </tr>
        `;
    });
    
    if (enrollments.length === 0) {
        tbody.innerHTML = '<tr><td colspan="3" style="text-align:center;">No enrollments</td></tr>';
    }
}
