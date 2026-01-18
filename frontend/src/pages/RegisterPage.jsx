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

export default function RegisterPage() {
    const [formData, setFormData] = useState({ username: '', email: '', password: '' });
    const { register } = useAuth();
    const navigate = useNavigate();
    const [error, setError] = useState('');

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    //Rejestracja za pomocą useAuth
    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await register(formData.username, formData.email, formData.password);
            navigate('/');
        } catch (err) {
            console.error("Registration failed", err);
            if (err.response && err.response.data && err.response.data.message) {
                setError('Registration failed: ' + err.response.data.message);
            } else {
                setError('Registration failed');
            }
        }
    };

    return (
        <div style={styles.container}>
            <div style={styles.card}>
                <h1 style={{ ...styles.title, display: 'flex', justifyContent: 'center', alignItems: 'center', gap: '10px' }}>
                    GreenThumb
                    <img src="https://cdn-icons-png.flaticon.com/512/628/628283.png" alt="Leaf" style={{ width: '32px', height: '32px' }} />
                </h1>
                <h2 style={{ textAlign: 'center', marginBottom: '1rem', color: '#555' }}>Register</h2>
                {error && <p style={{ color: 'red', textAlign: 'center' }}>{error}</p>}
                <form onSubmit={handleSubmit}>
                    <div style={styles.inputGroup}>
                        <input
                            style={styles.input}
                            name="username"
                            type="text"
                            placeholder="Username"
                            onChange={handleChange}
                        />
                    </div>
                    <div style={styles.inputGroup}>
                        <input
                            style={styles.input}
                            name="email"
                            type="email"
                            placeholder="Email"
                            onChange={handleChange}
                        />
                    </div>
                    <div style={styles.inputGroup}>
                        <input
                            style={styles.input}
                            name="password"
                            type="password"
                            placeholder="Password"
                            onChange={handleChange}
                        />
                    </div>
                    <button style={styles.button} type="submit">Create account</button>
                </form>
                <Link to="/login" style={styles.link}>Already have an account? Log in</Link>
            </div>
        </div>
    );
}
