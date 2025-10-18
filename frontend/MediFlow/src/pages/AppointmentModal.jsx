import React, { useEffect, useState } from "react";
import "../styles/AppointmentModal.css";

export default function AppointmentModal({ point, onClose }) {
    const [selectedDate, setSelectedDate] = useState(null);
    const [availableTimes, setAvailableTimes] = useState([]);
    const [selectedTime, setSelectedTime] = useState(null);
    const [pesel, setPesel] = useState("");
    const [referralNumber, setReferralNumber] = useState("");
    const [message, setMessage] = useState("");
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        document.body.style.overflow = "hidden";
        return () => {
            document.body.style.overflow = "auto";
        };
    }, []);

    const next7Days = Array.from({ length: 7 }, (_, i) => {
        const date = new Date();
        date.setDate(date.getDate() + i);
        return date;
    });

    useEffect(() => {
        if (!selectedDate) return;
        setLoading(true);
        // fetch(`/api/appointments/available?pointId=${point.id}&date=${yyyy-MM-dd}`)
        setTimeout(() => {
            const hours = generateHours("07:00", "15:00", 15); // co 15 min
            setAvailableTimes(
                hours.map((h, idx) => ({
                    time: h,
                    available: idx % 5 !== 0, // co piąty termin zajęty
                }))
            );
            setLoading(false);
        }, 500);
    }, [selectedDate, point]);

    const generateHours = (start, end, interval) => {
        const result = [];
        let [h, m] = start.split(":").map(Number);
        const [endH, endM] = end.split(":").map(Number);
        while (h < endH || (h === endH && m <= endM)) {
            result.push(
                `${h.toString().padStart(2, "0")}:${m.toString().padStart(2, "0")}`
            );
            m += interval;
            if (m >= 60) {
                h += 1;
                m -= 60;
            }
        }
        return result;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!selectedDate || !selectedTime) {
            setMessage("Wybierz termin wizyty");
            return;
        }

        const appointment = {
            collectionPointId: point.id,
            date: selectedDate.toISOString()[0],
            pesel,
            referralNumber,
        };

        try {
            const res = await fetch("/api/appointments", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(appointment),
            });

            if (res.ok) {
                setMessage("Wizyta została umówiona pomyślnie");
            } else if (res.status === 409) {
                setMessage("Wybrany termin jest już zajęty");
            } else {
                setMessage("Nie udało się umówić wizyty");
            }
        } catch (err) {
            console.error(err);
            setMessage("Błąd połączenia z serwerem");
        }
    };

    return (
        <div className="modal-overlay">
            <div className="modal-content">
                <button className="close-btn" onClick={onClose}>
                    ✕
                </button>
                <h3>Umów wizytę w punkcie</h3>
                <p className="point-name">
                    <strong>{point.name}</strong> ({point.city})
                </p>

                <div className="date-selector">
                    <p>Wybierz dzień:</p>
                    <div className="days-row">
                        {next7Days.map((d) => {
                            const formatted = d.toLocaleDateString("pl-PL", {
                                weekday: "short",
                                day: "numeric",
                                month: "short",
                            });
                            const isSelected =
                                selectedDate && d.toDateString() === selectedDate.toDateString();
                            return (
                                <button
                                    key={d.toISOString()}
                                    className={`day-btn ${isSelected ? "selected" : ""}`}
                                    onClick={() => setSelectedDate(d)}
                                >
                                    {formatted}
                                </button>
                            );
                        })}
                    </div>
                </div>

                {selectedDate && (
                    <div className="times-section">
                        <p>Dostępne godziny:</p>
                        {loading ? (
                            <p>Ładowanie godzin...</p>
                        ) : (
                            <div className="times-grid">
                                {availableTimes.map((slot) => (
                                    <button
                                        key={slot.time}
                                        className={`time-btn ${
                                            !slot.available ? "disabled" : ""
                                        } ${selectedTime === slot.time ? "selected" : ""}`}
                                        onClick={() =>
                                            slot.available && setSelectedTime(slot.time)
                                        }
                                        disabled={!slot.available}
                                    >
                                        {slot.time}
                                    </button>
                                ))}
                            </div>
                        )}
                    </div>
                )}

                <form onSubmit={handleSubmit} className="appointment-form">
                    <label>Numer skierowania:</label>
                    <input
                        type="text"
                        value={referralNumber}
                        onChange={(e) => setReferralNumber(e.target.value)}
                        required
                    />

                    <label>PESEL:</label>
                    <input
                        type="text"
                        maxLength={11}
                        value={pesel}
                        onChange={(e) => setPesel(e.target.value)}
                        required
                    />

                    <button type="submit" className="submit-btn">
                        Umów wizytę
                    </button>
                </form>

                {message && <p className="message">{message}</p>}
            </div>
        </div>
    );
}
