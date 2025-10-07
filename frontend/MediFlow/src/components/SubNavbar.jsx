import { NavLink } from "react-router-dom";

export default function SubNavbar() {
    return (
        <aside className="subnavbar">
            <nav>
                <ul>
                    <li><NavLink to="/" end className={({ isActive }) => isActive ? "active" : ""}>Strona główna</NavLink></li>
                    <li><NavLink to="/referrals" className={({ isActive }) => isActive ? "active" : ""}>Skierowania</NavLink></li>
                    <li><NavLink to="/appointments" className={({ isActive }) => isActive ? "active" : ""}>Wizyty</NavLink></li>
                    <li><NavLink to="/results" className={({ isActive }) => isActive ? "active" : ""}>Wyniki</NavLink></li>
                </ul>
            </nav>
        </aside>
    );
}
