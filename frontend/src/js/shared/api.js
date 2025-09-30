const API_BASE_URL = '/api';

export class ApiService {
    static async request(endpoint, options = {}) {
        const config = {
            headers: {
                'Content-Type': 'application/json',
                ...options.headers
            },
            ...options
        };

        try {
            const response = await fetch(`${API_BASE_URL}${endpoint}`, config);
            const data = await response.json();

            if (!response.ok) {
                throw new Error(data.message || data.error || 'Request failed');
            }

            return data;
        } catch (error) {
            console.error('API Error:', error);
            throw error;
        }
    }

    static async get(endpoint) {
        return this.request(endpoint, { method: 'GET' });
    }

    static async post(endpoint, body) {
        return this.request(endpoint, {
            method: 'POST',
            body: JSON.stringify(body)
        });
    }

    static async put(endpoint, body) {
        return this.request(endpoint, {
            method: 'PUT',
            body: JSON.stringify(body)
        });
    }

    static async delete(endpoint) {
        return this.request(endpoint, { method: 'DELETE' });
    }
}

export class AuthApi {
    static async login(email, password) {
        return ApiService.post('/auth/login', { email, password });
    }

    static async register(data) {
        return ApiService.post('/auth/register', {
            email: data.email,
            password: data.password,
            fullName: data.fullName,
            roles: ['BIDDER']
        });
    }
}

export class AuctionApi {
    static async getAllAuctions() {
        return ApiService.get('/auctions');
    }

    static async getAuction(id) {
        return ApiService.get(`/auctions/${id}`);
    }

    static async createAuction(data) {
        return ApiService.post('/auctions', data);
    }
}

export class BidApi {
    static async placeBid(auctionId, bidderEmail, amount) {
        return ApiService.post(`/bids/auction/${auctionId}`, {
            bidderEmail,
            amount
        });
    }

    static async getAuctionBids(auctionId) {
        return ApiService.get(`/bids/auction/${auctionId}`);
    }
}