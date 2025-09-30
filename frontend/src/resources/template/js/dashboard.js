// Configuration
const API_BASE_URL = 'http://localhost:8080/api';
const DASHBOARD_ENDPOINTS = {
    USERS: '/users/all',
    STATS: '/dashboard/stats',
    USER_PROFILE: '/users'
};

// State Management
let dashboardState = {
    currentUser: null,
    users: [],
    currentPage: 1,
    itemsPerPage: 10,
    totalUsers: 0,
    sortField: 'id',
    sortDirection: 'asc'
};

// Utility Functions
class DashboardUtils {
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

        setTimeout(() => {
            alert.style.opacity = '0';
            alert.style.transition = 'opacity 0.3s ease';
            setTimeout(() => alert.remove(), 300);
        }, 5000);
    }

    static setLoading(show = true) {
        const overlay = document.getElementById('loadingOverlay');
        if (overlay) {
            overlay.style.display = show ? 'flex' : 'none';
        }
    }

    static formatDate(dateString) {
        const date = new Date(dateString);
        return date.toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'short',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    }

    static formatPhoneNumber(phone) {
        if (!phone) return '-';
        // Simple phone formatting
        return phone.replace(/(\d{3})(\d{3})(\d{4})/, '($1) $2-$3');
    }

    static debounce(func, wait) {
        let timeout;
        return function executedFunction(...args) {
            const later = () => {
                clearTimeout(timeout);
                func(...args);
            };
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
        };
    }
}

// API Service
class DashboardService {
    static async fetchUsers() {
        try {
            const response = await fetch(`${API_BASE_URL}${DASHBOARD_ENDPOINTS.USERS}`);
            const data = await response.json();

            if (!response.ok) {
                throw new Error(data.error || 'Failed to fetch users');
            }

            return data;
        } catch (error) {
            throw new Error(error.message || 'Network error. Please try again.');
        }
    }

    static async fetchDashboardStats() {
        try {
            const response = await fetch(`${API_BASE_URL}${DASHBOARD_ENDPOINTS.STATS}`);
            const data = await response.json();

            if (!response.ok) {
                throw new Error(data.error || 'Failed to fetch dashboard stats');
            }

            return data;
        } catch (error) {
            throw new Error(error.message || 'Network error. Please try again.');
        }
    }

    static async fetchUserProfile(userId) {
        try {
            const response = await fetch(`${API_BASE_URL}${DASHBOARD_ENDPOINTS.USER_PROFILE}/${userId}`);
            const data = await response.json();

            if (!response.ok) {
                throw new Error(data.error || 'Failed to fetch user profile');
            }

            return data;
        } catch (error) {
            throw new Error(error.message || 'Network error. Please try again.');
        }
    }
}

// Dashboard Management
class DashboardManager {
    static async initialize() {
        await this.loadUserData();
        await this.loadDashboardData();
        this.initializeEventListeners();
        this.startRealTimeUpdates();
    }

    static async loadUserData() {
        try {
            const userData = localStorage.getItem('user');
            if (!userData) {
                throw new Error('No user data found');
            }

            dashboardState.currentUser = JSON.parse(userData);
            this.updateUserInterface();
        } catch (error) {
            console.error('Error loading user data:', error);
            this.logout();
        }
    }

    static async loadDashboardData() {
        DashboardUtils.setLoading(true);

        try {
            const [usersResponse, statsResponse] = await Promise.all([
                DashboardService.fetchUsers(),
                DashboardService.fetchDashboardStats()
            ]);

            dashboardState.users = usersResponse.data.users || [];
            dashboardState.totalUsers = usersResponse.data.totalUsers || 0;

            this.updateStats(statsResponse.data);
            this.renderUsersTable();
            this.updatePagination();

        } catch (error) {
            DashboardUtils.showAlert(error.message);
        } finally {
            DashboardUtils.setLoading(false);
        }
    }

    static updateUserInterface() {
        const { currentUser } = dashboardState;

        // Update user profile
        const userFullName = document.getElementById('userFullName');
        const userRole = document.getElementById('userRole');

        if (userFullName) {
            userFullName.textContent = currentUser.fullName || currentUser.username;
        }

        if (userRole) {
            userRole.textContent = 'Premium Bidder';
        }

        // Update page title
        document.title = `BidMaster Pro - Welcome ${currentUser.username}`;
    }

    static updateStats(stats) {
        // Update stat cards
        const totalUsersElement = document.getElementById('totalUsers');
        const activeBidsElement = document.getElementById('activeBids');
        const endingSoonElement = document.getElementById('endingSoon');
        const wonAuctionsElement = document.getElementById('wonAuctions');

        if (totalUsersElement) {
            totalUsersElement.textContent = stats.totalUsers || dashboardState.totalUsers;
        }

        if (activeBidsElement) {
            activeBidsElement.textContent = stats.activeUsers || '0';
        }

        if (endingSoonElement) {
            endingSoonElement.textContent = '8'; // Mock data
        }

        if (wonAuctionsElement) {
            wonAuctionsElement.textContent = '12'; // Mock data
        }
    }

    static renderUsersTable() {
        const tableBody = document.getElementById('usersTableBody');
        if (!tableBody) return;

        const { users, currentPage, itemsPerPage, sortField, sortDirection } = dashboardState;

        // Sort users
        const sortedUsers = [...users].sort((a, b) => {
            let aValue = a[sortField];
            let bValue = b[sortField];

            if (sortField === 'createdAt') {
                aValue = new Date(aValue);
                bValue = new Date(bValue);
            }

            if (aValue < bValue) return sortDirection === 'asc' ? -1 : 1;
            if (aValue > bValue) return sortDirection === 'asc' ? 1 : -1;
            return 0;
        });

        // Paginate
        const startIndex = (currentPage - 1) * itemsPerPage;
        const paginatedUsers = sortedUsers.slice(startIndex, startIndex + itemsPerPage);

        if (paginatedUsers.length === 0) {
            tableBody.innerHTML = `
                <tr>
                    <td colspan="7" class="no-data">
                        <i class="fas fa-users-slash"></i>
                        <span>No users found</span>
                    </td>
                </tr>
            `;
            return;
        }

        tableBody.innerHTML = paginatedUsers.map(user => `
            <tr>
                <td>${user.id}</td>
                <td>
                    <div class="user-cell">
                        <span class="username">${user.username}</span>
                    </div>
                </td>
                <td>${user.email}</td>
                <td>${user.fullName || '-'}</td>
                <td>${DashboardUtils.formatPhoneNumber(user.phoneNumber)}</td>
                <td>${DashboardUtils.formatDate(user.createdAt)}</td>
                <td>
                    <div class="action-buttons">
                        <button class="btn-icon" onclick="DashboardManager.viewUser(${user.id})" title="View Profile">
                            <i class="fas fa-eye"></i>
                        </button>
                        <button class="btn-icon" onclick="DashboardManager.editUser(${user.id})" title="Edit User">
                            <i class="fas fa-edit"></i>
                        </button>
                    </div>
                </td>
            </tr>
        `).join('');

        // Update showing count
        const showingCount = document.getElementById('showingCount');
        const totalCount = document.getElementById('totalCount');

        if (showingCount) showingCount.textContent = paginatedUsers.length;
        if (totalCount) totalCount.textContent = users.length;
    }

    static updatePagination() {
        const pagination = document.getElementById('pagination');
        if (!pagination) return;

        const { users, currentPage, itemsPerPage } = dashboardState;
        const totalPages = Math.ceil(users.length / itemsPerPage);

        if (totalPages <= 1) {
            pagination.innerHTML = '';
            return;
        }

        let paginationHTML = '';

        // Previous button
        paginationHTML += `
            <button onclick="DashboardManager.changePage(${currentPage - 1})" 
                    ${currentPage === 1 ? 'disabled' : ''}>
                <i class="fas fa-chevron-left"></i>
            </button>
        `;

        // Page numbers
        for (let i = 1; i <= totalPages; i++) {
            if (i === 1 || i === totalPages || (i >= currentPage - 1 && i <= currentPage + 1)) {
                paginationHTML += `
                    <button onclick="DashboardManager.changePage(${i})" 
                            ${i === currentPage ? 'class="active"' : ''}>
                        ${i}
                    </button>
                `;
            } else if (i === currentPage - 2 || i === currentPage + 2) {
                paginationHTML += `<span>...</span>`;
            }
        }

        // Next button
        paginationHTML += `
            <button onclick="DashboardManager.changePage(${currentPage + 1})" 
                    ${currentPage === totalPages ? 'disabled' : ''}>
                <i class="fas fa-chevron-right"></i>
            </button>
        `;

        pagination.innerHTML = paginationHTML;
    }

    static changePage(page) {
        const totalPages = Math.ceil(dashboardState.users.length / dashboardState.itemsPerPage);

        if (page < 1 || page > totalPages) return;

        dashboardState.currentPage = page;
        this.renderUsersTable();
        this.updatePagination();

        // Scroll to top of table
        const tableContainer = document.querySelector('.table-container');
        if (tableContainer) {
            tableContainer.scrollIntoView({ behavior: 'smooth', block: 'start' });
        }
    }

    static sortTable(field) {
        if (dashboardState.sortField === field) {
            dashboardState.sortDirection = dashboardState.sortDirection === 'asc' ? 'desc' : 'asc';
        } else {
            dashboardState.sortField = field;
            dashboardState.sortDirection = 'asc';
        }

        this.renderUsersTable();
    }

    static viewUser(userId) {
        DashboardUtils.showAlert(`Viewing user profile for ID: ${userId}`, 'success');
        // In a real application, you would open a modal or navigate to user profile page
    }

    static editUser(userId) {
        DashboardUtils.showAlert(`Editing user with ID: ${userId}`, 'warning');
        // In a real application, you would open an edit modal
    }

    static async refreshUsers() {
        const refreshBtn = document.getElementById('refreshUsers');
        const originalText = refreshBtn.innerHTML;

        refreshBtn.innerHTML = '<i class="fas fa-sync-alt fa-spin"></i> Refreshing';
        refreshBtn.disabled = true;

        try {
            await this.loadDashboardData();
            DashboardUtils.showAlert('Users data refreshed successfully', 'success');
        } catch (error) {
            DashboardUtils.showAlert(error.message);
        } finally {
            refreshBtn.innerHTML = originalText;
            refreshBtn.disabled = false;
        }
    }

    static exportUsers() {
        const { users } = dashboardState;

        if (users.length === 0) {
            DashboardUtils.showAlert('No data to export', 'warning');
            return;
        }

        // Simple CSV export
        const headers = ['ID', 'Username', 'Email', 'Full Name', 'Phone', 'Joined Date'];
        const csvData = users.map(user => [
            user.id,
            user.username,
            user.email,
            user.fullName || '',
            user.phoneNumber || '',
            DashboardUtils.formatDate(user.createdAt)
        ]);

        const csvContent = [headers, ...csvData]
            .map(row => row.map(field => `"${field}"`).join(','))
            .join('\n');

        const blob = new Blob([csvContent], { type: 'text/csv' });
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `users-export-${new Date().toISOString().split('T')[0]}.csv`;
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        window.URL.revokeObjectURL(url);

        DashboardUtils.showAlert('Users data exported successfully', 'success');
    }

    static initializeEventListeners() {
        // Refresh users button
        const refreshBtn = document.getElementById('refreshUsers');
        if (refreshBtn) {
            refreshBtn.addEventListener('click', () => this.refreshUsers());
        }

        // Export users button
        const exportBtn = document.getElementById('exportUsers');
        if (exportBtn) {
            exportBtn.addEventListener('click', () => this.exportUsers());
        }

        // Logout button
        const logoutBtn = document.getElementById('logoutBtn');
        if (logoutBtn) {
            logoutBtn.addEventListener('click', () => this.logout());
        }

        // Sidebar toggle
        const sidebarToggle = document.getElementById('sidebarToggle');
        if (sidebarToggle) {
            sidebarToggle.addEventListener('click', () => this.toggleSidebar());
        }

        // Theme toggle
        const themeToggle = document.getElementById('themeToggle');
        if (themeToggle) {
            themeToggle.addEventListener('click', () => this.toggleTheme());
        }

        // Table sorting
        const tableHeaders = document.querySelectorAll('.table-header');
        tableHeaders.forEach(header => {
            header.addEventListener('click', () => {
                const field = header.querySelector('span').textContent
                    .toLowerCase()
                    .replace(/\s+/g, '')
                    .replace('id', 'id')
                    .replace('username', 'username')
                    .replace('email', 'email')
                    .replace('fullname', 'fullName')
                    .replace('phone', 'phoneNumber')
                    .replace('joineddate', 'createdAt');

                this.sortTable(field);
            });
        });

        // Search functionality (you can add a search input later)
        this.initializeSearch();
    }

    static initializeSearch() {
        // This would be connected to a search input
        // For now, it's a placeholder for future implementation
    }

    static toggleSidebar() {
        const sidebar = document.querySelector('.sidebar');
        sidebar.classList.toggle('open');
    }

    static toggleTheme() {
        const currentTheme = document.documentElement.getAttribute('data-theme') || 'light';
        const newTheme = currentTheme === 'light' ? 'dark' : 'light';

        document.documentElement.setAttribute('data-theme', newTheme);
        localStorage.setItem('theme', newTheme);

        const themeIcon = document.querySelector('#themeToggle i');
        if (themeIcon) {
            themeIcon.className = newTheme === 'light' ? 'fas fa-moon' : 'fas fa-sun';
        }
    }

    static logout() {
        // Clear all stored data
        localStorage.removeItem('user');
        localStorage.removeItem('isLoggedIn');
        localStorage.removeItem('loginTime');

        // Redirect to login page
        window.location.href = 'index.html';
    }

    static startRealTimeUpdates() {
        // Update current time
        this.updateCurrentTime();
        setInterval(() => this.updateCurrentTime(), 60000);

        // Refresh data periodically (every 5 minutes)
        setInterval(() => {
            this.loadDashboardData();
        }, 300000);
    }

    static updateCurrentTime() {
        const timeElement = document.getElementById('currentTime');
        if (timeElement) {
            const now = new Date();
            timeElement.textContent = now.toLocaleTimeString('en-US', {
                hour: '2-digit',
                minute: '2-digit',
                hour12: true
            });
        }
    }
}

// Initialize dashboard when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    // Check authentication first
    const isLoggedIn = localStorage.getItem('isLoggedIn');
    if (!isLoggedIn) {
        window.location.href = 'index.html';
        return;
    }

    // Initialize theme
    const savedTheme = localStorage.getItem('theme') || 'light';
    document.documentElement.setAttribute('data-theme', savedTheme);

    // Initialize dashboard
    DashboardManager.initialize();

    // Add smooth loading
    document.body.style.opacity = '0';
    setTimeout(() => {
        document.body.style.transition = 'opacity 0.3s ease';
        document.body.style.opacity = '1';
    }, 100);
});

// Error handling
window.addEventListener('error', (event) => {
    console.error('Global error:', event.error);
    DashboardUtils.showAlert('An unexpected error occurred', 'error');
});

// Export for global access
window.DashboardManager = DashboardManager;
window.DashboardUtils = DashboardUtils;