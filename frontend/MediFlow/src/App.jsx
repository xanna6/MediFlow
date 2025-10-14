import { Routes, Route } from "react-router-dom";
import Layout from "./components/Layout";
import MainPage from "./pages/MainPage.jsx";
import Referrals from "./pages/Referrals";
import CollectionPoints from "./pages/CollectionPoints.jsx";

function App() {
    return (
        <Layout>
            <Routes>
                <Route path="/" element={<MainPage />} />
                <Route path="/referrals" element={<Referrals />} />
                <Route path="/appointments" element={<CollectionPoints />} />
            </Routes>
        </Layout>
    );
}

export default App;
