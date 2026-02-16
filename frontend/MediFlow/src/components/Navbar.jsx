import { useNavigate } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";
import {getDefaultRouteForRole} from "../auth/AuthRedirect.js";

export default function Navbar() {
    const { user, logout } = useAuth();
    const navigate = useNavigate();
    const redirectPath = getDefaultRouteForRole(user);

    return (
        <header className="navbar">
            <h1 onClick={() => navigate(redirectPath, { replace: true })}>MediFlow</h1>

            <nav>
                {!user && (
                    <button
                        className="login-btn"
                        onClick={() => navigate("/login")}
                    >
                        Zaloguj
                    </button>
                )}

                {user && (
                    <>
                    <span style={{marginRight: "20px" }}>
                        {user.username}
                    </span>
                        <button className="login-btn" onClick={logout}>Wyloguj</button>
                    </>
                )}
            </nav>
        </header>
    );
}

