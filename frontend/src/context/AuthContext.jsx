import { createContext, useState, useEffect, useContext } from 'react';
import api from '../services/api';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
    //Wczytywanie stanu użytkownika z localStorage
    const [user, setUser] = useState(() => {
        const storedToken = localStorage.getItem('token');
        const storedUser = localStorage.getItem('username');
        const storedAvatar = localStorage.getItem('avatarUrl');

        if (storedToken && storedUser) {
            return {
                username: storedUser,
                avatarUrl: storedAvatar
            };
        }
        return null;
    });

    const [token, setToken] = useState(localStorage.getItem('token'));

    //Synchronizacja tokenu po wylogowaniu
    useEffect(() => {
        if (token) {
            localStorage.setItem('token', token);
        } else {
            localStorage.removeItem('token');
            if (user) setUser(null);
        }
    }, [token]);

    //Aktualizacja użytkownika do tego przycisku Edit Profile
    const updateUser = (data) => {
        setUser(prev => ({ ...prev, ...data }));
        if (data.username) localStorage.setItem('username', data.username);
        if (data.avatarUrl) localStorage.setItem('avatarUrl', data.avatarUrl);
    };

    //Loguje, zapisuje token i dane użytkownika
    const login = async (username, password) => {
        const response = await api.post('/auth/login', { username, password });
        localStorage.setItem('username', username);
        if (response.data.avatarUrl) {
            localStorage.setItem('avatarUrl', response.data.avatarUrl);
        }
        localStorage.setItem('token', response.data.token);

        setToken(response.data.token);
        setUser({
            username,
            avatarUrl: response.data.avatarUrl
        });
    };

    //Rejestracja nowego użytkownika
    const register = async (username, email, password) => {
        const response = await api.post('/auth/register', { username, email, password });
        localStorage.setItem('username', username);
        if (response.data.avatarUrl) {
            localStorage.setItem('avatarUrl', response.data.avatarUrl);
        }
        localStorage.setItem('token', response.data.token);

        setToken(response.data.token);
        setUser({
            username,
            avatarUrl: response.data.avatarUrl
        });
    };

    //Wylogowuje i usuwa token użytkownika
    const logout = () => {
        setToken(null);
        localStorage.removeItem('token');
        localStorage.removeItem('username');
        localStorage.removeItem('avatarUrl');
    };

    return (
        <AuthContext.Provider value={{ user, login, register, logout, updateUser, isAuthenticated: !!user }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);
