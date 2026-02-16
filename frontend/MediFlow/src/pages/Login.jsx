import React, { useState } from "react";
import { useAuth } from "../auth/AuthContext";
import "../styles/AppointmentModal.css";

export default function LoginPage() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [message, setMessage] = useState("");
    const [loading, setLoading] = useState(false);

    const { login } = useAuth();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setMessage("");

        try {
            const res = await fetch("/api/auth/login", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ username, password }),
            });

            if (!res.ok) {
                setMessage("Nieprawidłowy login lub hasło");
                return;
            }

            const { token } = await res.json();
            localStorage.setItem("token", token);

            login(token);

        } catch (err) {
            console.error(err);
            setMessage("Błąd połączenia z serwerem");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div style={{ display: "flex", justifyContent: "center", marginTop: "60px" }}>
            <div className="modal-content" style={{ maxWidth: "420px" }}>
                <h3>Zaloguj się</h3>

                <form onSubmit={handleSubmit} className="appointment-form">
                    <label>Login / email</label>
                    <input
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />

                    <label>Hasło</label>
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />

                    <button className="submit-btn" type="submit" disabled={loading}>
                        {loading ? "Logowanie..." : "Zaloguj"}
                    </button>
                </form>

                {message && <p className="message">{message}</p>}
            </div>
        </div>
    );
}
