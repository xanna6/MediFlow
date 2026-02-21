import React, { useState } from "react";
import "../styles/SampleResults.css";

export default function SampleResultsModal({ sample, onClose, onResultsSaved }) {
    const [results, setResults] = useState(sample.sampleTestDtos);
    const [changedResults, setChangedResults] = useState([]);

    const token = localStorage.getItem("token");

    const handleChange = (id, value) => {
        setResults((prev) =>
            prev.map((r) => (r.id === id ? { ...r, result: value } : r))
        );

        setChangedResults((prev) => {
            const existing = prev.find((r) => r.id === id);

            if (existing) {
                return prev.map((r) =>
                    r.id === id ? { ...r, result: value } : r
                );
            } else {
                return [...prev, { id, result: value }];
            }
        });
    };


    const handleSave = async () => {
        const payload = {
            sampleId: sample.id,
            tests: changedResults,
        };

        try {
            const res = await fetch(`/api/samples/${sample.id}/results`, {
                method: "POST",
                headers: { "Content-Type": "application/json" , Authorization: "Bearer " + token},
                body: JSON.stringify(payload),
            });

            if (!res.ok) {
                const errData = await res.json();
                throw new Error(errData.message || "Błąd podczas zapisywania wyników.");
            }

            const updatedSample = await res.json();
            onResultsSaved(updatedSample);
            onClose();
        } catch (err) {
            console.log(err.message);
        }
    };

    return (
        <div className="modal-overlay">
            <div className="modal-content">
                <button className="close-btn" onClick={onClose}>×</button>
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
                    {results.map((t) => (
                        <tr key={t.id}>
                            <td>{t.name}</td>
                            <td>
                                <input
                                    type="text"
                                    value={t.result || ""}
                                    onChange={(e) => handleChange(t.id, e.target.value)}
                                />
                            </td>
                            <td>{t.unit}</td>
                            <td>{t.standard}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>

                <div className="modal-actions">
                    <button onClick={handleSave} disabled={changedResults.length === 0}>Zapisz wyniki</button>
                    <button onClick={onClose}>Anuluj</button>
                </div>
            </div>
        </div>
    );
}
