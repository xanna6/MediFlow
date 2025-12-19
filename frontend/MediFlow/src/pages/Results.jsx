import React, { useState } from "react";
import "../styles/Results.css";

export default function ResultsDownloadPage() {
    const [pesel, setPesel] = useState("");
    const [sampleCode, setSampleCode] = useState("");
    const [sample, setSample] = useState(null);
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError("");
        setLoading(true);
        setSample(null);

        try {
            const res = await fetch(
                `/api/samples/by-code?pesel=${pesel}&sampleCode=${sampleCode}`
            );

            if (!res.ok) {
                throw new Error("Nie znaleziono wyników dla podanych danych.");
            }

            const data = await res.json();
            setSample(data);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    const handleDownloadPdf = async () => {
        const res = await fetch(`/api/samples/${sample.id}/results/pdf`);
        if (!res.ok) {
            alert("Nie udało się pobrać pliku PDF.");
            return;
        }

        const blob = await res.blob();
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement("a");
        a.href = url;
        a.download = `wyniki-${sample.sampleCode}.pdf`;
        document.body.appendChild(a);
        a.click();
        a.remove();
    };

    return (
        <div className="results-download-container">
            <h2>Pobierz wyniki badań</h2>
            <form onSubmit={handleSubmit} className="results-form">
                <label>
                    PESEL:
                    <input
                        type="text"
                        value={pesel}
                        onChange={(e) => setPesel(e.target.value)}
                        required
                    />
                </label>
                <label>
                    Numer próbki:
                    <input
                        type="text"
                        value={sampleCode}
                        onChange={(e) => setSampleCode(e.target.value)}
                        required
                    />
                </label>
                <button type="submit" disabled={loading}>
                    {loading ? "Szukam wyników..." : "Szukaj"}
                </button>
            </form>

            {error && <p className="error">{error}</p>}

            {sample && (
                <div className="results-section">
                    <h3>Próbka: {sample.sampleCode}</h3>
                    <p>Data utworzenia: {new Date(sample.createdAt).toLocaleString()}</p>

                    <table className="results-table">
                        <thead>
                        <tr>
                            <th>Badanie</th>
                            <th>Wynik</th>
                            <th>Jednostka</th>
                            <th>Norma</th>
                        </tr>
                        </thead>
                        <tbody>
                        {sample.sampleTestDtos.map((t) => (
                            <tr key={t.id}>
                                <td>{t.name}</td>
                                <td>{t.result || "-"}</td>
                                <td>{t.unit}</td>
                                <td>{t.standard}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>

                    <button className="download-btn" onClick={handleDownloadPdf}>
                        Pobierz PDF
                    </button>

                </div>
            )}
        </div>
    );
}
