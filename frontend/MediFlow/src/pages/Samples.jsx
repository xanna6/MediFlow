import React, { useEffect, useState } from "react";
import "../styles/Samples.css";
import SampleResultsModal from "./SampleResults.jsx"

export default function Samples() {
    const [samples, setSamples] = useState([]);
    const [selectedSample, setSelectedSample] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    useEffect(() => {
        const fetchSamples = async () => {
            try {
                setLoading(true);
                setError("");
                const res = await fetch("/api/samples");
                if (!res.ok) throw new Error("Błąd podczas pobierania próbek");
                const data = await res.json();
                setSamples(data);
            } catch (err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };

        fetchSamples();
    }, []);

    const handleFillResults = async (sampleId) => {
        setLoading(true);
        try {
            const res = await fetch(`/api/samples/${sampleId}`);
            if (!res.ok) throw new Error("Nie udało się pobrać danych próbki.");
            const data = await res.json();

            setSelectedSample(data);
        } catch (err) {
            alert(err.message);
        } finally {
            setLoading(false);
        }
    };

    const handleResultsSaved = (updatedSample) => {
        setSamples((prev) =>
            prev.map((s) => (s.id === updatedSample.id ? updatedSample : s))
        );
    };

    return (
        <div className="samples-page">
            <h2>Próbki laboratoryjne</h2>

            {loading && <p>Ładowanie próbek...</p>}
            {error && <p className="error">{error}</p>}

            {!loading && !error && samples.length === 0 && (
                <p>Brak dostępnych próbek.</p>
            )}

            {samples.length > 0 && (
                <table className="samples-table">
                    <thead>
                    <tr>
                        <th>Numer próbki</th>
                        <th>Data utworzenia</th>
                        <th>Status</th>
                        <th>Akcje</th>
                    </tr>
                    </thead>
                    <tbody>
                    {samples.map((sample) => (
                        <tr key={sample.id}>
                            <td>{sample.sampleCode}</td>
                            <td>{sample.createdAt.substring(0, 16).replace("T", " ")}</td>
                            <td>{formatStatus(sample.status)}</td>
                            <td>
                                <button
                                    className="fill-btn"
                                    onClick={() => handleFillResults(sample.id)}
                                >
                                    Uzupełnij wyniki
                                </button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}
            {selectedSample && (
                <SampleResultsModal
                    sample={selectedSample}
                    onResultsSaved={handleResultsSaved}
                    onClose={() => setSelectedSample(null)}
                />
            )}
        </div>
    );
}

function formatStatus(status) {
    switch (status) {
        case "COMPLETED":
            return "Zakończona";
        case "CREATED":
            return "Nowa";
        default:
            return "W trakcie";
    }
}
