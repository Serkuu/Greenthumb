import { useEffect, useState } from 'react';
import Navbar from '../components/Navbar';
import api from '../services/api';
import { useParams, Link } from 'react-router-dom';

const styles = {
    container: { padding: '2rem', maxWidth: '1200px', margin: '0 auto', fontFamily: 'Arial, sans-serif' },
    backLink: { color: '#666', textDecoration: 'none', marginBottom: '1rem', display: 'inline-block' },
    section: { marginBottom: '3rem' },
    subtitle: { borderBottom: '2px solid #d8f3dc', paddingBottom: '0.5rem', marginBottom: '1.5rem', color: '#1b4332' },

    searchBox: { display: 'flex', gap: '10px', marginBottom: '1rem' },
    searchInput: { padding: '0.8rem', flex: 1, borderRadius: '4px', border: '1px solid #ccc' },
    searchBtn: { padding: '0.8rem 1.5rem', background: '#2d6a4f', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer' },
    resultsGrid: { display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(200px, 1fr))', gap: '1rem', marginTop: '1rem', alignItems: 'start' },
    resultCard: { border: '1px solid #eee', borderRadius: '8px', padding: '1rem', textAlign: 'center', background: '#fff' },
    plantImg: { width: '100%', height: '150px', objectFit: 'cover', borderRadius: '4px', marginBottom: '0.5rem' },
    addBtn: { width: '100%', padding: '0.5rem', marginTop: '0.5rem', background: '#52b788', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer' },

    plantList: { display: 'flex', flexWrap: 'wrap', gap: '1.5rem' },
    myPlantCard: { width: '250px', border: '1px solid #cce3de', borderRadius: '12px', overflow: 'hidden', boxShadow: '0 2px 8px rgba(0,0,0,0.05)', background: 'white' },
    myPlantImg: { width: '100%', height: '180px', objectFit: 'cover' },
    myPlantInfo: { padding: '1rem' },
};

import templateImg from '../images/template.png';

const PLACEHOLDER_IMG = templateImg;

// Komponent przycisku usuwania z obsługą hover
const RemoveButton = ({ onClick }) => {
    const [isHovered, setIsHovered] = useState(false);

    const btnStyle = {
        width: '100%',
        padding: '0.5rem',
        marginTop: '1rem',
        borderRadius: '4px',
        cursor: 'pointer',
        border: '1px solid #e56b6f',
        background: isHovered ? '#e56b6f' : 'transparent',
        color: isHovered ? 'white' : '#e56b6f',
        transition: 'all 0.2s ease-in-out'
    };

    return (
        <button
            style={btnStyle}
            onClick={onClick}
            onMouseEnter={() => setIsHovered(true)}
            onMouseLeave={() => setIsHovered(false)}
        >
            Remove
        </button>
    );
};

export default function GardenDetails() {
    const { id } = useParams();
    const [garden, setGarden] = useState(null);
    const [plants, setPlants] = useState([]);
    const [searchQuery, setSearchQuery] = useState('');
    const [searchResults, setSearchResults] = useState([]);
    const [loading, setLoading] = useState(false);

    //Pobiera szczegóły ogrodu
    const fetchGardenDetails = async () => {
        try {
            const res = await api.get(`/gardens/${id}`);
            setGarden(res.data);
        } catch (err) {
            console.error("Failed to fetch garden", err);
        }
    };

    useEffect(() => {
        fetchGardenDetails();
    }, [id]);

    //Wyszukiwanie roślin w API

    const handleSearch = async (e) => {
        e.preventDefault();
        setLoading(true);
        try {
            const res = await api.get(`/plants/search?q=${searchQuery}`);
            setSearchResults(res.data.data);
        } catch (err) {
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    //Dodawanie rośliny do ogrodu
    const handleAddPlant = async (plant) => {
        try {
            await api.post('/plants', {
                gardenId: id,
                trefleId: plant.id,
                nickname: plant.common_name,
                imageUrl: plant.bestImageUrl
            });
            alert('Plant added!');
            fetchGardenDetails();
        } catch (err) {
            alert('Error adding plant');
        }
    };

    //Usuwanie rośliny z ogrodu
    const handleRemovePlant = async (plantId) => {
        if (!window.confirm('Are you sure you want to remove this plant?')) return;
        try {
            await api.delete(`/plants/${plantId}`);
            fetchGardenDetails();
        } catch (err) {
            alert('Error removing plant');
        }
    };

    return (
        <>
            <Navbar />
            <div style={styles.container}>
                <Link to="/" style={{ ...styles.backLink, display: 'inline-flex', alignItems: 'center', gap: '5px' }}>
                    <img src="https://cdn-icons-png.flaticon.com/512/271/271220.png" alt="Back" style={{ width: '16px', height: '16px' }} />
                    Return to the gardens
                </Link>
                <h1>Manage Gardens</h1>

                <div style={styles.section}>
                    <h2 style={styles.subtitle}>Add a new plant</h2>
                    <form style={styles.searchBox} onSubmit={handleSearch}>
                        <input
                            style={styles.searchInput}
                            placeholder="Enter the name of the plant (e.g. Monstera, Tomato)..."
                            value={searchQuery}
                            onChange={(e) => setSearchQuery(e.target.value)}
                        />
                        <button style={styles.searchBtn} type="submit">{loading ? 'Searching...' : 'Search'}</button>
                    </form>

                    <div style={styles.resultsGrid}>
                        {searchResults.map(plant => (
                            <div key={plant.id} style={styles.resultCard}>
                                <img
                                    src={plant.image_url || PLACEHOLDER_IMG}
                                    alt={plant.common_name}
                                    style={styles.plantImg}
                                />
                                <strong>{plant.common_name}</strong>
                                <br />
                                <small style={{ color: '#777' }}>{plant.scientific_name}</small>
                                <button style={styles.addBtn} onClick={() => handleAddPlant({
                                    ...plant,
                                    bestImageUrl: plant.image_url || PLACEHOLDER_IMG
                                })}>
                                    + Add to the garden
                                </button>
                            </div>
                        ))}
                    </div>
                </div>

                <div style={styles.section}>
                    <h2 style={styles.subtitle}>Your plants in {garden?.name}:</h2>
                    {garden?.plants && garden.plants.length > 0 ? (
                        <div style={styles.resultsGrid}>
                            {garden.plants.map(plant => (
                                <div key={plant.id} style={styles.resultCard}>
                                    <img
                                        src={plant.imageUrl || PLACEHOLDER_IMG}
                                        alt={plant.nickname}
                                        style={styles.plantImg}
                                    />
                                    <strong>{plant.nickname}</strong>
                                    {plant.scientificName && (
                                        <div style={{ fontSize: '0.9rem', fontStyle: 'italic', color: '#555', marginBottom: '0.5rem' }}>
                                            {plant.scientificName}
                                        </div>
                                    )}
                                    <details style={{ textAlign: 'left', fontSize: '0.85rem', marginTop: '0.5rem' }}>
                                        <summary style={{ cursor: 'pointer', color: '#2d6a4f' }}>Details</summary>
                                        <div style={{ marginTop: '5px', paddingLeft: '10px', borderLeft: '2px solid #eee' }}>
                                            {plant.family && <div><strong>Family:</strong> {plant.family}</div>}
                                            {plant.familyCommonName && <div><strong>Family common name:</strong> {plant.familyCommonName}</div>}
                                            {plant.year && <div><strong>Year:</strong> {plant.year}</div>}
                                            {plant.edible !== null && plant.edible !== undefined && (
                                                <div><strong>Edible:</strong> {plant.edible ? 'Yes' : 'No'}</div>
                                            )}
                                        </div>
                                    </details>
                                    <RemoveButton onClick={() => handleRemovePlant(plant.id)} />
                                </div>
                            ))}
                        </div>
                    ) : (
                        <p>Your garden is empty. Add something above!</p>
                    )}
                </div>
            </div >
        </>
    );
}
