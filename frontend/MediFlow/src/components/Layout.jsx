import Navbar from "./Navbar";
import SubNavbar from "./SubNavbar.jsx";
import "../styles/App.css";

export default function Layout({ children }) {
    return (
        <div className="layout">
            <Navbar />
            <SubNavbar />
            <main className="main-content">
                {children}
            </main>
        </div>
    );
}
