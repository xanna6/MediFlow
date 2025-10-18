import React, { useEffect, useState } from "react";
import "../styles/CollectionPoints.css";
import AppointmentModal from "./AppointmentModal.jsx";

export default function CollectionPoints() {
    const [points, setPoints] = useState([]);
    const [filteredPoints, setFilteredPoints] = useState([]);
    const [searchCity, setSearchCity] = useState("");
    const [loading, setLoading] = useState(true);
    const [selectedPoint, setSelectedPoint] = useState(null);

    useEffect(() => {
        const fetchPoints = async () => {
            try {
                const res = await fetch("/api/collection-points");
                if (!res.ok) throw new Error("Błąd pobierania punktów pobrań");
                const data = await res.json();
                setPoints(data);
                setFilteredPoints(data);
            } catch (err) {
                console.error(err);
            } finally {
                setLoading(false);
            }
        };

        fetchPoints();
    }, []);

    useEffect(() => {
        const filtered = points.filter((p) =>
            p.city.toLowerCase().includes(searchCity.toLowerCase())
        );
        setFilteredPoints(filtered);
    }, [searchCity, points]);

    const handleSelect = (point) => {
        setSelectedPoint(point);
        // otwarcie modala do wybrania terminu
        // openModal(point);
    };

    if (loading) {
        return <div className="collection-points-loading">Ładowanie punktów...</div>;
    }

    function formatTime(timeString) {
        if (!timeString) return "";
        return timeString.slice(0, 5);
    }

    return (
        <div className="collection-points-container">
            <h2>Umów wizytę</h2>

            <div className="filter-section">
                <label htmlFor="city">Filtruj po miejscowości:</label>
                <input
                    type="text"
                    id="city"
                    placeholder="np. Kraków"
                    value={searchCity}
                    onChange={(e) => setSearchCity(e.target.value)}
                />
            </div>

            {filteredPoints.length === 0 ? (
                <p>Brak punktów spełniających kryteria.</p>
            ) : (
                <div className="points-list">
                    {filteredPoints.map((point) => (
                        <div key={point.id} className="point-card">
                            <div className="point-info">
                                <h3>{point.name}</h3>
                                <p>
                                    {point.street}, {point.postalCode} {point.city}
                                </p>
                                <p>
                                    <strong>Godziny otwarcia:</strong> {formatTime(point.openedFrom)} - {formatTime(point.openedTo)}
                                </p>
                            </div>
                            <div className="point-actions">
                                <button onClick={() => handleSelect(point)}>Wybierz</button>
                            </div>
                        </div>
                    ))}
                </div>
            )}

            {selectedPoint && (
                <AppointmentModal
                    point={selectedPoint}
                    onClose={() => setSelectedPoint(null)}
                />
            )}
        </div>
    );
}
