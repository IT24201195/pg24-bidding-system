import { AuthApi } from '../shared/api.js';
import { UIUtils, AuthUtils, initializePasswordToggle } from '../shared/utils.js';

const signupForm = document.getElementById('signupForm');
const fullNameInput = document.getElementById('fullName');
const emailInput = document.getElementById('email');
const passwordInput = document.getElementById('password');

initializePasswordToggle('password', 'passwordToggle');

signupForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    UIUtils.hideAlert();

    const errorFields = ['fullNameError', 'emailError', 'passwordError'];
    errorFields.forEach(field => UIUtils.hideError(field));

    const formData = {
        fullName: fullNameInput.value.trim(),
        email: emailInput.value.trim(),
        password: passwordInput.value
    };

    let isValid = true;

    if (!formData.fullName) {
        UIUtils.showError('fullNameError', 'Full name is required');
        isValid = false;
    }

    if (!formData.email) {
        UIUtils.showError('emailError', 'Email is required');
        isValid = false;
    } else if (!validateEmail(formData.email)) {
        UIUtils.showError('emailError', 'Please enter a valid email address');
        isValid = false;
    }

    if (!formData.password) {
        UIUtils.showError('passwordError', 'Password is required');
        isValid = false;
    } else if (formData.password.length < 6) {
        UIUtils.showError('passwordError', 'Password must be at least 6 characters');
        isValid = false;
    }

    if (!isValid) return;

    UIUtils.setLoading('signupBtn', true);

    try {
        await AuthApi.register(formData);

        UIUtils.showAlert('Account created successfully! Redirecting to login...', 'success');

        setTimeout(() => {
            window.location.href = '/index.html';
        }, 2000);

    } catch (error) {
        UIUtils.showAlert(error.message || 'Registration failed. Please try again.');
    } finally {
        UIUtils.setLoading('signupBtn', false);
    }
});

function validateEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

AuthUtils.checkAuth();