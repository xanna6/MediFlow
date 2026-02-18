import { useNavigate } from "react-router-dom";
import "../styles/MainPage.css";

export default function MainPage() {
    const navigate = useNavigate();

    return (
        <div className="mainpage-container">

            <h2 className="mainpage-title">
                Witamy w MediFlow
            </h2>

            <p className="mainpage-subtitle">
                Zarządzaj wizytami i odbieraj wyniki badań w prosty sposób
            </p>

            <div className="mainpage-cards">

                <div className="mainpage-card">

                    <div className="card-image">
                        <img
                            src="/appointment.jpg"
                            alt="Umów wizytę"
                        />
                    </div>

                    <div className="card-content">

                        <h3>Umów wizytę</h3>

                        <p>
                            Wybierz dogodny termin i punkt pobrań.
                            Proces zajmie mniej niż minutę.
                        </p>

                        <button
                            className="mainpage-btn"
                            onClick={() => navigate("/make-appointment")}
                        >
                            Umów wizytę
                        </button>

                    </div>
                </div>


                <div className="mainpage-card">

                    <div className="card-image">
                        <img
                            src="/results.jpg"
                            alt="Odbierz wyniki"
                        />
                    </div>

                    <div className="card-content">

                        <h3>Sprawdź wyniki</h3>

                        <p>
                            Odbierz wyniki swoich badań online,
                            bez konieczności wizyty w punkcie.
                        </p>

                        <button
                            className="mainpage-btn"
                            onClick={() => navigate("/results")}
                        >
                            Zobacz wyniki
                        </button>

                    </div>
                </div>

            </div>
        </div>
    );
}
