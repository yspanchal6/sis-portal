// ===== Login Page Logic =====
document.addEventListener('DOMContentLoaded', async () => {
    // Check if already logged in
    const auth = await API.getDashboard();
    if (auth.loggedIn) {
        redirectByRole(auth.role);
        return;
    }
    
    // Get role from URL if present
    const urlParams = new URLSearchParams(window.location.search);
    const role = urlParams.get('role');
    if (role) {
        document.getElementById('username').value = role.toLowerCase();
    }
    
    // Handle login form submission
    const loginForm = document.getElementById('loginForm');
    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const username = document.getElementById('username').value.trim();
        const password = document.getElementById('password').value;
        
        if (!username || !password) {
            showAlert('Please enter both username and password.');
            return;
        }
        
        const result = await API.login(username, password);
        
        if (result.success) {
            redirectByRole(result.user.role);
        } else {
            showAlert(result.message || 'Invalid username or password.');
        }
    });
});

function redirectByRole(role) {
    switch (role) {
        case 'ADMIN':
            window.location.href = '/admin.html';
            break;
        case 'TEACHER':
            window.location.href = '/teacher.html';
            break;
        case 'STUDENT':
            window.location.href = '/student.html';
            break;
        default:
            window.location.href = '/login.html';
    }
}
