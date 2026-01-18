import { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { useNavigate, Link } from 'react-router-dom';

const styles = {
    container: {
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        height: '100vh',
        backgroundColor: '#f0f4f1',
        fontFamily: 'Arial, sans-serif'
    },
    card: {
        background: 'white',
        padding: '2rem',
        borderRadius: '8px',
        boxShadow: '0 4px 6px rgba(0,0,0,0.1)',
        width: '100%',
        maxWidth: '400px',
    },
    title: {
        color: '#2d6a4f',
        textAlign: 'center',
        marginBottom: '1.5rem',
    },
    inputGroup: {
        marginBottom: '1rem',
    },
    input: {
        width: '100%',
        padding: '0.75rem',
        borderRadius: '4px',
        border: '1px solid #ddd',
        fontSize: '1rem',
        boxSizing: 'border-box'
    },
    button: {
        width: '100%',
        padding: '0.75rem',
        backgroundColor: '#40916c',
        color: 'white',
        border: 'none',
        borderRadius: '4px',
        fontSize: '1rem',
        cursor: 'pointer',
        marginTop: '1rem',
    },
    link: {
        display: 'block',
        textAlign: 'center',
        marginTop: '1rem',
        color: '#40916c',
        textDecoration: 'none'
    }
};

export default function LoginPage() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const { login } = useAuth();
    const navigate = useNavigate();
    const [error, setError] = useState('');

    //Formularz logowania za pomocą useAuth
    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await login(username, password);
            navigate('/');
        } catch (err) {
            setError('Login failed. Please check your details.');
        }
    };

    return (
        <div style={styles.container}>
            <div style={styles.card}>
                <h1 style={{ ...styles.title, display: 'flex', justifyContent: 'center', alignItems: 'center', gap: '10px' }}>
                    GreenThumb
                    <img src="https://cdn-icons-png.flaticon.com/512/628/628283.png" alt="Leaf" style={{ width: '32px', height: '32px' }} />
                </h1>
                <h2 style={{ textAlign: 'center', marginBottom: '1rem', color: '#555' }}>Login</h2>
                {error && <p style={{ color: 'red', textAlign: 'center' }}>{error}</p>}
                <form onSubmit={handleSubmit}>
                    <div style={styles.inputGroup}>
                        <input
                            style={styles.input}
                            type="text"
                            placeholder="Username"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                        />
                    </div>
                    <div style={styles.inputGroup}>
                        <input
                            style={styles.input}
                            type="password"
                            placeholder="Password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                        />
                    </div>
                    <button style={styles.button} type="submit">Login</button>
                </form>
                <Link to="/register" style={styles.link}>Don't have an account? Sign up</Link>
            </div>
        </div>
    );
}
