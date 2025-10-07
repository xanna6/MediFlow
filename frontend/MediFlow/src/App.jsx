import { Routes, Route } from "react-router-dom";
import Layout from "./components/Layout";
import MainPage from "./pages/MainPage.jsx";
import Referrals from "./pages/Referrals";

function App() {
    return (
        <Layout>
            <Routes>
                <Route path="/" element={<MainPage />} />
                <Route path="/referrals" element={<Referrals />} />
            </Routes>
        </Layout>
    );
}

export default App;
