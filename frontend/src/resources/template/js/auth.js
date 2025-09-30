// Configuration
const API_BASE_URL = 'http://localhost:8080/api';
const AUTH_ENDPOINTS = {
    LOGIN: '/users/login',
    REGISTER: '/users/register'
};

// Utility Functions
class AuthUtils {
    static showAlert(message, type = 'error', containerId = 'alertContainer') {
        const container = document.getElementById(containerId);
        const alert = document.createElement('div');
        alert.className = `alert alert-${type}`;
        alert.innerHTML = `
            <i class="fas fa-${type === 'success' ? 'check-circle' : 'exclamation-circle'}"></i>
            <span>${message}</span>
        `;

        container.innerHTML = '';
        container.appendChild(alert);

        if (type === 'success') {
            setTimeout(() => alert.remove(), 5000);
        }
    }

    static hideAlert(containerId = 'alertContainer') {
        const container = document.getElementById(containerId);
        container.innerHTML = '';
    }

    static setLoading(button, isLoading) {
        const btn = typeof button === 'string' ? document.getElementById(button) : button;
        if (!btn) return;

        if (isLoading) {
            btn.classList.add('loading');
            btn.disabled = true;
        } else {
            btn.classList.remove('loading');
            btn.disabled = false;
        }
    }

    static showError(fieldId, message) {
        const errorElement = document.getElementById(fieldId);
        if (errorElement) {
            errorElement.textContent = message;
            errorElement.style.display = 'block';
        }
    }

    static hideError(fieldId) {
        const errorElement = document.getElementById(fieldId);
        if (errorElement) {
            errorElement.textContent = '';
            errorElement.style.display = 'none';
        }
    }

    static validateEmail(email) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    }

    static validatePassword(password) {
        return password.length >= 6;
    }

    static calculatePasswordStrength(password) {
        let strength = 0;
        if (password.length >= 8) strength++;
        if (/[A-Z]/.test(password)) strength++;
        if (/[a-z]/.test(password)) strength++;
        if (/[0-9]/.test(password)) strength++;
        if (/[^A-Za-z0-9]/.test(password)) strength++;

        return Math.min(strength, 5);
    }

    static updatePasswordStrength(password) {
        const strength = this.calculatePasswordStrength(password);
        const strengthBar = document.getElementById('passwordStrength');
        const strengthText = document.getElementById('passwordStrengthText');

        if (!strengthBar || !strengthText) return;

        const percentages = [0, 20, 40, 60, 80, 100];
        const colors = ['#ef4444', '#f59e0b', '#f59e0b', '#10b981', '#10b981'];
        const texts = ['Very Weak', 'Weak', 'Fair', 'Good', 'Strong', 'Very Strong'];

        strengthBar.style.width = `${percentages[strength]}%`;
        strengthBar.style.background = colors[strength - 1] || colors[0];
        strengthText.textContent = texts[strength];
        strengthText.style.color = colors[strength - 1] || colors[0];
    }
}

// API Service
class AuthService {
    static async login(credentials) {
        try {
            const response = await fetch(`${API_BASE_URL}${AUTH_ENDPOINTS.LOGIN}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(credentials)
            });

            const data = await response.json();

            if (!response.ok) {
                throw new Error(data.error || 'Login failed');
            }

            return data;
        } catch (error) {
            throw new Error(error.message || 'Network error. Please try again.');
        }
    }

    static async register(userData) {
        try {
            const response = await fetch(`${API_BASE_URL}${AUTH_ENDPOINTS.REGISTER}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(userData)
            });

            const data = await response.json();

            if (!response.ok) {
                throw new Error(data.error || 'Registration failed');
            }

            return data;
        } catch (error) {
            throw new Error(error.message || 'Network error. Please try again.');
        }
    }
}

// Login Page Functionality
if (document.getElementById('loginForm')) {
    const loginForm = document.getElementById('loginForm');
    const passwordToggle = document.getElementById('passwordToggle');
    const passwordInput = document.getElementById('password');

    // Password toggle functionality
    passwordToggle?.addEventListener('click', () => {
        const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
        passwordInput.setAttribute('type', type);
        passwordToggle.innerHTML = type === 'password' ? '<i class="fas fa-eye"></i>' : '<i class="fas fa-eye-slash"></i>';
    });

    // Form submission
    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        AuthUtils.hideAlert();

        // Clear previous errors
        AuthUtils.hideError('usernameError');
        AuthUtils.hideError('passwordError');

        const username = document.getElementById('username').value.trim();
        const password = document.getElementById('password').value;
        const rememberMe = document.getElementById('rememberMe').checked;

        // Validation
        let isValid = true;

        if (!username) {
            AuthUtils.showError('usernameError', 'Username is required');
            isValid = false;
        }

        if (!password) {
            AuthUtils.showError('passwordError', 'Password is required');
            isValid = false;
        }

        if (!isValid) return;

        // Set loading state
        AuthUtils.setLoading('loginBtn', true);

        try {
            const result = await AuthService.login({ username, password });

            // Store user data
            localStorage.setItem('user', JSON.stringify({
                id: result.data.userId,
                username: result.data.username,
                email: result.data.email,
                fullName: result.data.fullName,
                rememberMe: rememberMe
            }));

            localStorage.setItem('isLoggedIn', 'true');
            localStorage.setItem('loginTime', new Date().toISOString());

            AuthUtils.showAlert('Login successful! Redirecting...', 'success');

            // Redirect to dashboard
            setTimeout(() => {
                window.location.href = 'dashboard.html';
            }, 1500);

        } catch (error) {
            AuthUtils.showAlert(error.message);
        } finally {
            AuthUtils.setLoading('loginBtn', false);
        }
    });
}

// Signup Page Functionality
if (document.getElementById('signupForm')) {
    const signupForm = document.getElementById('signupForm');
    const passwordInput = document.getElementById('password');
    const confirmPasswordInput = document.getElementById('confirmPassword');
    const passwordToggle = document.getElementById('passwordToggle');
    const confirmPasswordToggle = document.getElementById('confirmPasswordToggle');

    // Password strength calculation
    passwordInput?.addEventListener('input', (e) => {
        AuthUtils.updatePasswordStrength(e.target.value);
    });

    // Password toggle functionality
    passwordToggle?.addEventListener('click', () => {
        const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
        passwordInput.setAttribute('type', type);
        passwordToggle.innerHTML = type === 'password' ? '<i class="fas fa-eye"></i>' : '<i class="fas fa-eye-slash"></i>';
    });

    confirmPasswordToggle?.addEventListener('click', () => {
        const type = confirmPasswordInput.getAttribute('type') === 'password' ? 'text' : 'password';
        confirmPasswordInput.setAttribute('type', type);
        confirmPasswordToggle.innerHTML = type === 'password' ? '<i class="fas fa-eye"></i>' : '<i class="fas fa-eye-slash"></i>';
    });

    // Form submission
    signupForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        AuthUtils.hideAlert();

        // Clear previous errors
        const errorFields = ['fullNameError', 'usernameError', 'emailError', 'phoneNumberError', 'passwordError', 'confirmPasswordError'];
        errorFields.forEach(field => AuthUtils.hideError(field));

        const formData = {
            fullName: document.getElementById('fullName').value.trim(),
            username: document.getElementById('username').value.trim(),
            email: document.getElementById('email').value.trim(),
            phoneNumber: document.getElementById('phoneNumber').value.trim(),
            password: document.getElementById('password').value,
            confirmPassword: document.getElementById('confirmPassword').value
        };

        const termsAgreed = document.getElementById('termsAgreement').checked;

        // Validation
        let isValid = true;

        if (!formData.fullName) {
            AuthUtils.showError('fullNameError', 'Full name is required');
            isValid = false;
        }

        if (!formData.username) {
            AuthUtils.showError('usernameError', 'Username is required');
            isValid = false;
        } else if (formData.username.length < 3) {
            AuthUtils.showError('usernameError', 'Username must be at least 3 characters');
            isValid = false;
        }

        if (!formData.email) {
            AuthUtils.showError('emailError', 'Email is required');
            isValid = false;
        } else if (!AuthUtils.validateEmail(formData.email)) {
            AuthUtils.showError('emailError', 'Please enter a valid email address');
            isValid = false;
        }

        if (!formData.password) {
            AuthUtils.showError('passwordError', 'Password is required');
            isValid = false;
        } else if (!AuthUtils.validatePassword(formData.password)) {
            AuthUtils.showError('passwordError', 'Password must be at least 6 characters');
            isValid = false;
        }

        if (!formData.confirmPassword) {
            AuthUtils.showError('confirmPasswordError', 'Please confirm your password');
            isValid = false;
        } else if (formData.password !== formData.confirmPassword) {
            AuthUtils.showError('confirmPasswordError', 'Passwords do not match');
            isValid = false;
        }

        if (!termsAgreed) {
            AuthUtils.showAlert('Please agree to the terms and conditions');
            isValid = false;
        }

        if (!isValid) return;

        // Set loading state
        AuthUtils.setLoading('signupBtn', true);

        try {
            // Remove confirmPassword before sending to API
            const { confirmPassword, ...userData } = formData;

            const result = await AuthService.register(userData);

            AuthUtils.showAlert('Account created successfully! Redirecting to login...', 'success');

            // Redirect to login page
            setTimeout(() => {
                window.location.href = 'index.html';
            }, 2000);

        } catch (error) {
            AuthUtils.showAlert(error.message);
        } finally {
            AuthUtils.setLoading('signupBtn', false);
        }
    });
}

// Authentication Check
function checkAuth() {
    const isLoggedIn = localStorage.getItem('isLoggedIn');
    const currentPage = window.location.pathname;

    if (isLoggedIn && currentPage.includes('index.html')) {
        window.location.href = 'dashboard.html';
        return;
    }

    if (!isLoggedIn && currentPage.includes('dashboard.html')) {
        window.location.href = 'index.html';
        return;
    }
}

// Initialize when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    checkAuth();

    // Add smooth transitions
    document.body.style.opacity = '0';
    setTimeout(() => {
        document.body.style.transition = 'opacity 0.3s ease';
        document.body.style.opacity = '1';
    }, 100);
});

// Theme Management
function initTheme() {
    const savedTheme = localStorage.getItem('theme') || 'light';
    document.documentElement.setAttribute('data-theme', savedTheme);
}

// Initialize theme
initTheme();