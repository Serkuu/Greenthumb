const API_URL = '/api';

const api = {
    //Główna funkcja do zapytań API
    request: async (endpoint, options = {}) => {
        const token = localStorage.getItem('token');
        const isAuthRequest = endpoint.startsWith('/auth/');

        const headers = {
            'Content-Type': 'application/json',
            ...(token && !isAuthRequest && { 'Authorization': `Bearer ${token}` }),
            ...options.headers
        };

        const config = {
            ...options,
            headers
        };

        try {
            const response = await fetch(`${API_URL}${endpoint}`, config);

            //Automatycznego wylogowania przy błędzie autoryzacji
            if ((response.status === 401 || response.status === 403) && !isAuthRequest) {
                localStorage.removeItem('token');
                localStorage.removeItem('username');
                window.location.href = '/login';
                throw new Error('Unauthorized');
            }

            //Reszta błędów
            if (!response.ok) {
                let errorData;
                try {
                    errorData = await response.json();
                } catch (e) {
                    errorData = { message: response.statusText };
                }

                const error = new Error(errorData.message || 'Request failed');
                error.response = {
                    status: response.status,
                    data: errorData
                };
                throw error;
            }

            const contentType = response.headers.get("content-type");
            let data = null;
            if (contentType && contentType.indexOf("application/json") !== -1) {
                data = await response.json();
            }
            return { data };

        } catch (error) {
            throw error;
        }
    },

    get: async (endpoint) => {
        return api.request(endpoint, { method: 'GET' });
    },

    post: async (endpoint, body) => {
        return api.request(endpoint, {
            method: 'POST',
            body: JSON.stringify(body)
        });
    },

    put: async (endpoint, body) => {
        return api.request(endpoint, {
            method: 'PUT',
            body: JSON.stringify(body)
        });
    },

    delete: async (endpoint) => {
        return api.request(endpoint, { method: 'DELETE' });
    }
};

export default api;
