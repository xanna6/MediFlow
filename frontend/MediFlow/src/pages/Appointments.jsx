import React, { useEffect, useState } from "react";
import "../styles/Appointments.css";

export default function Appointments() {
    const [appointments, setAppointments] = useState([]);
    const [selectedDate, setSelectedDate] = useState(
        new Date().toISOString().split("T")[0]
    );
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");
    const [successMessage, setSuccessMessage] = useState("");

    const fetchAppointments = async (date) => {
        try {
            setLoading(true);
            setError("");
            const res = await fetch(`/api/appointments/day?date=${date}`);
            if (!res.ok) throw new Error("Błąd podczas pobierania wizyt");
            const data = await res.json();
            setAppointments(data);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchAppointments(selectedDate);
    }, [selectedDate]);

    const handleConfirm = async (appointmentId) => {
        try {
            const res = await fetch("/api/samples", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ appointmentId }),
            });
            if (!res.ok) throw new Error("Nie udało się potwierdzić wizyty");

            setSuccessMessage("Wizyta potwierdzona — próbka została utworzona.");

            fetchAppointments(selectedDate);
        } catch (err) {
            setError(err.message);
        }
    };

    return (
        <div className="appointments-page">
            <h2>Wizyty — recepcja</h2>

            <div className="appointments-controls">
                <label htmlFor="date">Data: </label>
                <input
                    id="date"
                    type="date"
                    value={selectedDate}
                    onChange={(e) => setSelectedDate(e.target.value)}
                />
            </div>

            {loading && <p>Ładowanie wizyt...</p>}
            {error && <p className="error">{error}</p>}
            {successMessage && <p className="success">{successMessage}</p>}

            {!loading && !error && appointments.length === 0 && (
                <p>Brak wizyt na ten dzień.</p>
            )}

            <table className="appointments-table">
                <thead>
                <tr>
                    <th>Godzina</th>
                    <th>Pacjent</th>
                    <th>PESEL</th>
                    <th>Numer skierowania</th>
                    <th>Akcje</th>
                </tr>
                </thead>
                <tbody>
                {appointments.map((a) => (
                    <tr key={a.id}>
                        <td>{a.date.substring(11, 16)}</td>
                        <td>{a.patientName}</td>
                        <td>{a.pesel}</td>
                        <td>{a.referralNumber}</td>
                        <td>
                            {a.status === "SCHEDULED" ? (
                                <button
                                    className="confirm-btn"
                                    onClick={() => handleConfirm(a.id)}
                                >
                                    Potwierdź wizytę
                                </button>
                            ) : (
                                <span className="confirmed">Potwierdzona</span>
                            )}
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}
