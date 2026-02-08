import { NavLink } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";

export default function SubNavbar() {
    const { user, hasRole } = useAuth();

        if (!user) {
        return (
            <aside className="subnavbar">
                <nav>
                    <ul>
                        <li>
                            <NavLink to="/" end className={({ isActive }) => isActive ? "active" : ""}>
                                Strona główna
                            </NavLink>
                        </li>
                        <li>
                            <NavLink to="/make-appointment"
                                     className={({ isActive }) => isActive ? "active" : ""}>
                                Umów wizytę
                            </NavLink>
                        </li>
                        <li>
                            <NavLink to="/results"
                                     className={({ isActive }) => isActive ? "active" : ""}>
                                Wyniki
                            </NavLink>
                        </li>
                    </ul>
                </nav>
            </aside>
        );
    }

    return (
        <aside className="subnavbar">
            <nav>
                <ul>
                    {hasRole("ROLE_DOCTOR") && (
                        <li>
                            <NavLink to="/referrals"
                                     className={({ isActive }) => isActive ? "active" : ""}>
                                Skierowania
                            </NavLink>
                        </li>
                    )}

                    {hasRole("ROLE_LAB") && (
                        <>
                            <li>
                                <NavLink to="/appointments"
                                         className={({ isActive }) => isActive ? "active" : ""}>
                                    Wizyty
                                </NavLink>
                            </li>
                            <li>
                                <NavLink to="/samples"
                                         className={({ isActive }) => isActive ? "active" : ""}>
                                    Próbki
                                </NavLink>
                            </li>
                        </>
                    )}
                </ul>
            </nav>
        </aside>
    );
}
