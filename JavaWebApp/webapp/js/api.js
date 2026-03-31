// ===== API Helper =====
const API = {
    baseUrl: '/api',
    
    async request(endpoint, options = {}) {
        try {
            const response = await fetch(this.baseUrl + endpoint, {
                headers: {
                    'Content-Type': 'application/json',
                    ...options.headers
                },
                ...options
            });
            return await response.json();
        } catch (error) {
            console.error('API Error:', error);
            return { error: error.message };
        }
    },
    
    async get(endpoint) {
        return this.request(endpoint, { method: 'GET' });
    },
    
    async post(endpoint, data) {
        return this.request(endpoint, {
            method: 'POST',
            body: JSON.stringify(data)
        });
    },
    
    async login(username, password) {
        return this.post('/login', { username, password });
    },
    
    async logout() {
        return this.post('/logout', {});
    },
    
    async getDashboard() {
        return this.get('/dashboard');
    },
    
    async getStudents() {
        return this.get('/students');
    },
    
    async getTeachers() {
        return this.get('/teachers');
    },
    
    async getCourses() {
        return this.get('/courses');
    },
    
    async getGrades() {
        return this.get('/grades');
    },
    
    async getEnrollments() {
        return this.get('/enrollments');
    },
    
    async saveStudent(data) {
        return this.post('/students', data);
    },
    
    async saveTeacher(data) {
        return this.post('/teachers', data);
    },
    
    async saveCourse(data) {
        return this.post('/courses', data);
    },
    
    async saveGrade(data) {
        return this.post('/grades', data);
    },
    
    async saveEnrollment(data) {
        return this.post('/enrollments', data);
    }
};

// ===== Utility Functions =====
function showAlert(message, type = 'danger') {
    const alertDiv = document.getElementById('alert');
    if (alertDiv) {
        alertDiv.className = `alert alert-${type}`;
        alertDiv.textContent = message;
        alertDiv.style.display = 'block';
        setTimeout(() => {
            alertDiv.style.display = 'none';
        }, 5000);
    }
}

function showModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.classList.add('show');
    }
}

function hideModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.classList.remove('show');
    }
}

function showSection(sectionId) {
    document.querySelectorAll('.section').forEach(section => {
        section.classList.remove('active');
    });
    document.getElementById(sectionId)?.classList.add('active');
    
    document.querySelectorAll('.nav-menu li').forEach(li => {
        li.classList.remove('active');
    });
    const activeLink = document.querySelector(`.nav-menu li a[href="#${sectionId}"]`);
    if (activeLink) {
        activeLink.parentElement.classList.add('active');
    }
}

function getGradeLetter(marks) {
    if (marks >= 90) return 'A';
    if (marks >= 80) return 'B';
    if (marks >= 70) return 'C';
    if (marks >= 60) return 'D';
    return 'F';
}

function getGradeColor(marks) {
    if (marks >= 90) return 'grade-a';
    if (marks >= 80) return 'grade-b';
    if (marks >= 70) return 'grade-c';
    if (marks >= 60) return 'grade-d';
    return 'grade-f';
}

async function logout() {
    await API.logout();
    window.location.href = '/login.html';
}

// Check if user is logged in
async function checkAuth() {
    const result = await API.getDashboard();
    if (!result.loggedIn) {
        window.location.href = '/login.html';
        return null;
    }
    return result;
}
