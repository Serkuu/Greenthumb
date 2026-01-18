import { useEffect, useState } from 'react';
import Navbar from '../components/Navbar';
import api from '../services/api';
import { useAuth } from '../context/AuthContext';

const styles = {
    container: { padding: '2rem', maxWidth: '800px', margin: '0 auto', fontFamily: 'Arial, sans-serif' },
    card: { background: 'white', border: '1px solid #e0e0e0', borderRadius: '12px', padding: '2rem', boxShadow: '0 4px 12px rgba(0,0,0,0.05)', textAlign: 'center', position: 'relative' },
    avatarSection: { position: 'relative', width: '120px', margin: '0 auto 1rem', cursor: 'pointer' },
    avatar: { width: '120px', height: '120px', borderRadius: '50%', objectFit: 'cover', border: '4px solid #d8f3dc', transition: 'filter 0.3s' },
    avatarOverlay: { position: 'absolute', top: 0, left: 0, width: '100%', height: '100%', borderRadius: '50%', background: 'rgba(0,0,0,0.5)', display: 'flex', alignItems: 'center', justifyContent: 'center', color: 'white', opacity: 0, transition: 'opacity 0.3s' },
    label: { color: '#666', fontSize: '0.9rem', marginBottom: '0.25rem', marginTop: '1.5rem', textTransform: 'uppercase', letterSpacing: '1px' },
    value: { fontSize: '1.2rem', color: '#1b4332', fontWeight: 'bold' },
    statsContainer: { display: 'flex', justifyContent: 'space-around', margin: '2rem 0', borderTop: '1px solid #eee', borderBottom: '1px solid #eee', padding: '1rem 0' },
    statItem: { textAlign: 'center' },
    statValue: { fontSize: '1.5rem', fontWeight: 'bold', color: '#2d6a4f' },
    statLabel: { fontSize: '0.9rem', color: '#888' },
    bioSection: { marginTop: '2rem', padding: '1rem', background: '#f9f9f9', borderRadius: '8px', color: '#555', position: 'relative', minHeight: '60px' },
    editButton: { position: 'absolute', top: '1rem', right: '1rem', background: 'none', border: 'none', color: '#2d6a4f', cursor: 'pointer', fontSize: '0.9rem', textDecoration: 'underline' },
    input: { width: '100%', padding: '0.5rem', marginTop: '0.5rem', borderRadius: '4px', border: '1px solid #ddd' },
    textarea: { width: '100%', padding: '0.5rem', marginTop: '0.5rem', borderRadius: '4px', border: '1px solid #ddd', minHeight: '80px' },
    saveBtn: { background: '#2d6a4f', color: 'white', border: 'none', padding: '0.5rem 1.5rem', borderRadius: '4px', cursor: 'pointer', marginTop: '1rem', fontSize: '1rem' },
    cancelBtn: { background: '#ccc', color: 'white', border: 'none', padding: '0.5rem 1.5rem', borderRadius: '4px', cursor: 'pointer', marginTop: '1rem', marginLeft: '0.5rem', fontSize: '1rem' }
};

export default function ProfilePage() {
    const { user, updateUser } = useAuth();
    const [profile, setProfile] = useState(null);
    const [loading, setLoading] = useState(true);
    const [isEditing, setIsEditing] = useState(false);
    const [formData, setFormData] = useState({ bio: '', avatarUrl: '' });
    const [hoverAvatar, setHoverAvatar] = useState(false);

    //Pobiera aktualne dane profilowe z API
    const fetchProfile = async () => {
        try {
            const res = await api.get('/profile');
            setProfile(res.data);
            setFormData({
                bio: res.data.bio || '',
                avatarUrl: res.data.avatarUrl || ''
            });
            //Updatuje avatar gdy się zmienia, żeby się odświeżyło
            if (res.data.avatarUrl) {
                updateUser({ avatarUrl: res.data.avatarUrl });
            }
        } catch (err) {
            console.error("Failed to fetch profile", err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchProfile();
    }, []);

    //Zapisuje zmienione dane
    const handleSave = async () => {
        try {
            await api.put('/profile', formData);
            if (formData.avatarUrl) {
                updateUser({ avatarUrl: formData.avatarUrl });
            }
            setIsEditing(false);
            fetchProfile();
        } catch (err) {
            alert('Failed to save changes.');
        }
    };

    if (loading) return <div>Loading...</div>;
    if (!profile) return <div>Failed to load profile.</div>;

    const avatarUrl = formData.avatarUrl || profile.avatarUrl || 'https://cdn-icons-png.flaticon.com/512/847/847969.png';

    return (
        <>
            <Navbar />
            <div style={styles.container}>
                <h1 style={{ textAlign: 'center', color: '#2d6a4f', marginBottom: '2rem' }}>Your Profile</h1>
                <div style={styles.card}>
                    {!isEditing && (
                        <button style={styles.editButton} onClick={() => setIsEditing(true)}>Edit profile</button>
                    )}

                    <div
                        style={styles.avatarSection}
                        onMouseEnter={() => setHoverAvatar(true)}
                        onMouseLeave={() => setHoverAvatar(false)}
                        onClick={() => isEditing && document.getElementById('avatarInput').focus()}
                    >
                        <img src={avatarUrl} alt="Avatar" style={styles.avatar} />
                        {isEditing && (
                            <div style={{ ...styles.avatarOverlay, opacity: hoverAvatar ? 1 : 0 }}>
                                <span>Change URL</span>
                            </div>
                        )}
                    </div>

                    {isEditing && (
                        <div style={{ marginBottom: '1rem' }}>
                            <input
                                id="avatarInput"
                                type="text"
                                placeholder="Avatar URL..."
                                style={styles.input}
                                value={formData.avatarUrl}
                                onChange={(e) => setFormData({ ...formData, avatarUrl: e.target.value })}
                            />
                        </div>
                    )}

                    <div>
                        <div style={styles.label}>USERNAME</div>
                        <div style={styles.value}>{profile.username}</div>
                    </div>

                    <div>
                        <div style={styles.label}>EMAIL</div>
                        <div style={styles.value}>{profile.email}</div>
                    </div>

                    <div style={styles.statsContainer}>
                        <div style={styles.statItem}>
                            <div style={styles.statValue}>{profile.plantCount || 0}</div>
                            <div style={styles.statLabel}>Plants</div>
                        </div>
                        <div style={styles.statItem}>
                            <div style={styles.statValue}>{profile.gardenCount || 0}</div>
                            <div style={styles.statLabel}>Gardens</div>
                        </div>
                    </div>

                    <div style={styles.bioSection}>
                        <div style={{ fontWeight: 'bold', marginBottom: '0.5rem', color: '#2d6a4f' }}>About me</div>
                        {isEditing ? (
                            <textarea
                                style={styles.textarea}
                                value={formData.bio}
                                onChange={(e) => setFormData({ ...formData, bio: e.target.value })}
                                placeholder="Write something about yourself..."
                            />
                        ) : (
                            <div style={{ fontStyle: 'italic' }}>
                                {profile.bio ? `"${profile.bio}"` : "No bio."}
                            </div>
                        )}
                    </div>

                    {isEditing && (
                        <div>
                            <button style={styles.saveBtn} onClick={handleSave}>Save</button>
                            <button style={styles.cancelBtn} onClick={() => setIsEditing(false)}>Cancel</button>
                        </div>
                    )}
                </div>
            </div>
        </>
    );
}
