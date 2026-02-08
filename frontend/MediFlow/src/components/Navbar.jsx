import { Link } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";

export default function Navbar() {
    const { user } = useAuth();

    return (
        <header className="navbar">
            <h1>MediFlow</h1>

            <nav>
                {!user && (
                    <Link to="/login" className="login-btn">
                        Zaloguj
                    </Link>
                )}

                {user && <span>{user.username}</span>}
            </nav>
        </header>
    );
}

