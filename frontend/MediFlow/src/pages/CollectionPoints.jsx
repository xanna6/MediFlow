import React, { useEffect, useRef, useState } from "react";
import "../styles/CollectionPoints.css";
import AppointmentModal from "./AppointmentModal.jsx";
import CollectionPointsMap from "./CollectionPointsMap.jsx";

export default function CollectionPoints() {
    const [points, setPoints] = useState([]);
    const [filteredPoints, setFilteredPoints] = useState([]);
    const [searchCity, setSearchCity] = useState("");
    const [loading, setLoading] = useState(true);
    const [selectedPoint, setSelectedPoint] = useState(null);
    const [appointmentPoint, setAppointmentPoint] = useState(null);

    const mapRef = useRef(null);
    const listItemRefs = useRef({});

    useEffect(() => {
        const fetchPoints = async () => {
            const res = await fetch("/api/collection-points");
            const data = await res.json();
            setPoints(data);
            setFilteredPoints(data);
            setLoading(false);
        };
        fetchPoints();
    }, []);

    useEffect(() => {
        setFilteredPoints(
            points.filter(p =>
                p.city.toLowerCase().includes(searchCity.toLowerCase())
            )
        );
    }, [searchCity, points]);

    const handleFocusPoint = (point) => {
        setSelectedPoint(point);

        // fokus mapy
        mapRef.current?.focusOnPoint(point);

        // przewinięcie listy
        listItemRefs.current[point.id]?.scrollIntoView({
            behavior: "smooth",
            block: "center"
        });
    };

    const handleSelectForAppointment = (point) => {
        setAppointmentPoint(point);
    };

    if (loading) {
        return <div>Ładowanie punktów...</div>;
    }

    function formatTime(timeString) {
        if (!timeString) return "";
        return timeString.slice(0, 5);
    }

    return (
        <div className="collection-points-container">
            <h2>Umów wizytę</h2>

            <div className="filter-section">
                <input
                    placeholder="Filtruj po miejscowości"
                    value={searchCity}
                    onChange={(e) => setSearchCity(e.target.value)}
                />
            </div>

            <div className="points-layout">
                <div className="points-list">
                    {filteredPoints.map(point => (
                        <div
                            key={point.id}
                            ref={(el) => (listItemRefs.current[point.id] = el)}
                            className={`point-card ${
                                selectedPoint?.id === point.id ? "selected" : ""
                            }`}
                        >
                            <div className="point-card-left">
                                <h3 className="point-name">{point.name}</h3>

                                <p className="point-address">
                                    {point.street}, {point.postalCode} {point.city}
                                </p>

                                <p className="point-hours">
                                    <strong>Godziny otwarcia:</strong>{" "}
                                    {formatTime(point.openedFrom)} – {formatTime(point.openedTo)}
                                </p>
                            </div>

                            <div className="point-card-right">
                                <button
                                    className="map-btn"
                                    onClick={() => handleFocusPoint(point)}
                                >
                                    Pokaż na mapie
                                </button>

                                <button
                                    className="select-btn"
                                    onClick={() => handleSelectForAppointment(point)}
                                >
                                    Wybierz
                                </button>
                            </div>
                        </div>
                    ))}
                </div>

                <CollectionPointsMap
                    ref={mapRef}
                    points={filteredPoints}
                    selectedPoint={selectedPoint}
                    onMarkerClick={handleFocusPoint}
                />
            </div>

            {appointmentPoint && (
                <AppointmentModal
                    point={appointmentPoint}
                    onClose={() => setAppointmentPoint(null)}
                />
            )}
        </div>
    );
}
