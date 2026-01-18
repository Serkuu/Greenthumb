import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const styles = {
    nav: {
        background: '#2d6a4f',
        padding: '1rem 2rem',
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        color: 'white',
        boxShadow: '0 2px 4px rgba(0,0,0,0.1)'
    },
    logo: {
        fontSize: '1.5rem',
        fontWeight: 'bold',
        textDecoration: 'none',
        color: 'white'
    },
    links: {
        display: 'flex',
        gap: '1.5rem',
        alignItems: 'center'
    },
    link: {
        color: '#d8f3dc',
        textDecoration: 'none',
        fontSize: '1rem',
        transition: 'color 0.2s'
    },
    logoutBtn: {
        background: 'rgba(255,255,255,0.2)',
        border: 'none',
        color: 'white',
        padding: '0.5rem 1rem',
        borderRadius: '4px',
        cursor: 'pointer',
        fontSize: '0.9rem'
    }
};

export default function Navbar() {
    const { logout, user } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    return (
        <nav style={styles.nav}>
            <Link to="/" style={{ ...styles.logo, display: 'flex', alignItems: 'center', gap: '8px' }}>
                GreenThumb
                <img src="https://cdn-icons-png.flaticon.com/512/628/628283.png" alt="Leaf" style={{ width: '24px', height: '24px' }} />
            </Link>
            <div style={styles.links}>
                <div style={{ display: 'flex', alignItems: 'center', gap: '15px' }}>
                    <Link to="/profile" style={{ display: 'flex', alignItems: 'center', textDecoration: 'none', color: 'white' }} title="Twój Profil">
                        <img
                            src={user?.avatarUrl || "https://cdn-icons-png.flaticon.com/512/847/847969.png"}
                            alt="Profile"
                            style={{ width: '32px', height: '32px', borderRadius: '50%', border: '2px solid #d8f3dc', objectFit: 'cover' }}
                        />
                    </Link>
                    <button style={styles.logoutBtn} onClick={handleLogout}>Logout</button>
                </div>
            </div>
        </nav>
    );
}
