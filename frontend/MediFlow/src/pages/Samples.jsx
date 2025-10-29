import React, { useEffect, useState } from "react";
import "../styles/Samples.css";

export default function Samples() {
    const [samples, setSamples] = useState([]);
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

    const handleFillResults = (sampleId) => {
        // TODO: otwórz modal lub przejdź do formularza
        alert(`Otwieram formularz wyników dla próbki #${sampleId}`);
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
