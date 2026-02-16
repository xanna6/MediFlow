import { Routes, Route } from "react-router-dom";
import Layout from "./components/Layout";
import MainPage from "./pages/MainPage.jsx";
import Referrals from "./pages/Referrals";
import CollectionPoints from "./pages/CollectionPoints.jsx";
import Appointments from "./pages/Appointments.jsx";
import Samples from "./pages/Samples.jsx"
import Results from "./pages/Results.jsx"
import Login from "./pages/Login.jsx"
import RoleRoute from "./auth/RoleRoute";


function App() {
    return (
        <Layout>
            <Routes>
                <Route path="/" element={<MainPage />} />
                <Route path="/referrals" element={<RoleRoute roles={["ROLE_DOCTOR"]}>
                    <Referrals />
                </RoleRoute>} />
                <Route path="/make-appointment" element={<CollectionPoints />} />
                <Route path="/appointments" element={<RoleRoute roles={["ROLE_LAB"]}>
                    <Appointments />
                </RoleRoute>} />
                <Route path="/samples" element={ <RoleRoute roles={["ROLE_LAB"]}>
                    <Samples />
                </RoleRoute>} />
                <Route path="/results" element={<Results />} />
                <Route path="/login" element={<Login />} />
            </Routes>
        </Layout>
    );
}

export default App;
