import { AuthApi } from '../shared/api.js';
import { UIUtils, AuthUtils, initializePasswordToggle } from '../shared/utils.js';

const loginForm = document.getElementById('loginForm');
const emailInput = document.getElementById('email');
const passwordInput = document.getElementById('password');
const rememberMeCheckbox = document.getElementById('rememberMe');

initializePasswordToggle('password', 'passwordToggle');

loginForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    UIUtils.hideAlert();

    UIUtils.hideError('emailError');
    UIUtils.hideError('passwordError');

    const email = emailInput.value.trim();
    const password = passwordInput.value;
    const rememberMe = rememberMeCheckbox.checked;

    let isValid = true;

    if (!email) {
        UIUtils.showError('emailError', 'Email is required');
        isValid = false;
    }

    if (!password) {
        UIUtils.showError('passwordError', 'Password is required');
        isValid = false;
    }

    if (!isValid) return;

    UIUtils.setLoading('loginBtn', true);

    try {
        const response = await AuthApi.login(email, password);

        const user = {
            email: email,
            fullName: response.fullName || email.split('@')[0],
            rememberMe: rememberMe
        };

        AuthUtils.setUser(user, response.token);

        UIUtils.showAlert('Login successful! Redirecting...', 'success');

        setTimeout(() => {
            window.location.href = '/auctions.html';
        }, 1000);

    } catch (error) {
        UIUtils.showAlert(error.message || 'Login failed. Please try again.');
    } finally {
        UIUtils.setLoading('loginBtn', false);
    }
});

AuthUtils.checkAuth();