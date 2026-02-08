import { Routes, Route } from "react-router-dom";
import Layout from "./components/Layout";
import MainPage from "./pages/MainPage.jsx";
import Referrals from "./pages/Referrals";
import CollectionPoints from "./pages/CollectionPoints.jsx";
import Appointments from "./pages/Appointments.jsx";
import Samples from "./pages/Samples.jsx"
import Results from "./pages/Results.jsx"
import Login from "./pages/Login.jsx"

function App() {
    return (
        <Layout>
            <Routes>
                <Route path="/" element={<MainPage />} />
                <Route path="/referrals" element={<Referrals />} />
                <Route path="/make-appointment" element={<CollectionPoints />} />
                <Route path="/appointments" element={<Appointments />} />
                <Route path="/samples" element={<Samples />} />
                <Route path="/results" element={<Results />} />
                <Route path="/login" element={<Login />} />
            </Routes>
        </Layout>
    );
}

export default App;
