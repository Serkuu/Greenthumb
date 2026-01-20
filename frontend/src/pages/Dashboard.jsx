import { useEffect, useState } from 'react';
import Navbar from '../components/Navbar';
import api from '../services/api';
import { Link } from 'react-router-dom';

const styles = {
    container: { padding: '2rem', maxWidth: '1200px', margin: '0 auto', fontFamily: 'Arial, sans-serif' },
    header: { display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '2rem' },
    title: { color: '#1b4332' },
    createForm: { display: 'flex', gap: '10px', marginBottom: '2rem', padding: '1rem', background: '#f8fdf9', borderRadius: '8px' },
    input: { padding: '0.5rem', borderRadius: '4px', border: '1px solid #ccc', flex: 1 },
    btn: { background: '#40916c', color: 'white', border: 'none', padding: '0.5rem 1rem', borderRadius: '4px', cursor: 'pointer' },
    grid: { display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: '1.5rem' },
    card: { border: '1px solid #e0e0e0', borderRadius: '8px', padding: '1.5rem', background: 'white', transition: 'transform 0.2s', boxShadow: '0 2px 4px rgba(0,0,0,0.05)' },
    cardTitle: { marginTop: 0, color: '#2d6a4f' },
    cardLink: { display: 'inline-block', marginTop: '1rem', color: '#40916c', textDecoration: 'none', fontWeight: 'bold' },
    deleteBtn: { float: 'right', color: '#ef233c', background: 'none', border: 'none', cursor: 'pointer', fontSize: '0.8rem' }
};

import templateImg from '../images/template.png';

const PLACEHOLDER_IMG = templateImg;

export default function Dashboard() {
    const [gardens, setGardens] = useState([]);
    const [newGardenName, setNewGardenName] = useState('');

    //Pobiera listę ogrodów użytkownika z backendu
    const fetchGardens = async () => {
        try {
            const res = await api.get('/gardens');
            if (Array.isArray(res.data)) {
                setGardens(res.data);
            } else {
                console.error("Received non-array data:", res.data);
            }
        } catch (err) {
            console.error(err);
        }
    };

    useEffect(() => {
        fetchGardens();
    }, []);

    const [popularPlants, setPopularPlants] = useState([]);
    const [plantError, setPlantError] = useState(null);

    useEffect(() => {
        fetchGardens();
        fetchPopularPlants();
    }, []);

    //Pobiera listę popularnych roślin tzn monstery bo nie zrobiłem xdd
    const fetchPopularPlants = async () => {
        try {
            const res = await api.get('/plants/search?q=monstera');
            setPopularPlants(res.data.data?.slice(0, 5) || []);
        } catch (err) {
            console.error("Failed to fetch popular plants", err);
            setPlantError("Failed to fetch popular plants");
        }
    };

    //Tworzenie nowego ogrodu
    const handleCreate = async (e) => {
        e.preventDefault();
        if (!newGardenName) return;
        try {
            await api.post('/gardens', { name: newGardenName });
            setNewGardenName('');
            fetchGardens();
        } catch (err) {
            console.error('Error in creating a garden', err);
        }
    };

    //Usuwanie ogrodu
    const handleDelete = async (id) => {
        if (!confirm('Are you sure you want to delete that garden?')) return;
        try {
            await api.delete(`/gardens/${id}`);
            fetchGardens();
        } catch (err) {
            alert('Delete error');
        }
    };

    return (
        <>
            <Navbar />
            <div style={styles.container}>
                <div style={styles.header}>
                    <h1 style={{ ...styles.title, display: 'flex', alignItems: 'center', gap: '10px' }}>
                        Gardens
                        <img src="https://cdn-icons-png.flaticon.com/512/1946/1946488.png" alt="Home" style={{ width: '28px', height: '28px' }} />
                    </h1>
                </div>

                <form style={styles.createForm} onSubmit={handleCreate}>
                    <input
                        style={styles.input}
                        placeholder="Name your garden (e.g. Living room, Backyard)"
                        value={newGardenName}
                        onChange={(e) => setNewGardenName(e.target.value)}
                    />
                    <button style={styles.btn} type="submit">+ Create Garden</button>
                </form>

                <div style={styles.grid}>
                    {gardens.map(garden => (
                        <div key={garden.id} style={styles.card}>
                            <button style={styles.deleteBtn} onClick={() => handleDelete(garden.id)}>Delete</button>
                            <h3 style={styles.cardTitle}>{garden.name}</h3>
                            <p style={{ color: '#666' }}>Here will be your plants.</p>
                            <Link to={`/garden/${garden.id}`} style={styles.cardLink}>Manage {'→'}</Link>
                        </div>
                    ))}
                </div>

                {gardens.length === 0 && (
                    <p style={{ textAlign: 'center', color: '#888', marginTop: '2rem' }}>
                        You don't have any gardens yet. Create one above!
                    </p>
                )}
            </div>

            <div style={{ ...styles.container, marginTop: '2rem', borderTop: '2px dashed #ccc', paddingTop: '2rem' }}>
                <h2 style={{ color: '#2d6a4f', display: 'flex', alignItems: 'center', gap: '10px' }}>
                    Popular plants:
                </h2>
                {plantError && <p style={{ color: 'red' }}>{plantError}</p>}
                <div style={styles.grid}>
                    {popularPlants.map(plant => (
                        <div key={plant.id} style={styles.card}>
                            <img
                                src={plant.image_url || PLACEHOLDER_IMG}
                                alt={plant.common_name}
                                style={{ width: '100%', height: '150px', objectFit: 'cover', borderRadius: '4px' }}
                            />
                            <h3 style={styles.cardTitle}>{plant.common_name}</h3>
                            <p style={{ fontStyle: 'italic', color: '#666' }}>{plant.scientific_name}</p>
                        </div>
                    ))}
                </div>
            </div>
        </>
    );
}
