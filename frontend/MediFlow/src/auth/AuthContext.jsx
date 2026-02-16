import { createContext, useContext, useEffect, useState } from "react";
import {useNavigate} from "react-router-dom";
import {getDefaultRouteForRole} from "./AuthRedirect.js";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
    const [user, setUser] = useState(null);

    useEffect(() => {
        const token = localStorage.getItem("token");
        if (!token) return;

        const payload = JSON.parse(atob(token.split(".")[1]));

        setUser({
            username: payload.sub,
            roles: payload.roles || [],
        });
    }, []);

    const hasRole = (role) => {
        return user?.roles?.includes(role);
    };

    const navigate = useNavigate();

    const login = (token) => {
        localStorage.setItem("token", token);
        const payload = JSON.parse(atob(token.split(".")[1]));
        const newUser = {
            username: payload.sub,
            roles: payload.roles || [],
        };

        setUser(newUser);
        const redirectPath = getDefaultRouteForRole(newUser);
        navigate(redirectPath, { replace: true });
    };

    const logout = () => {
        localStorage.removeItem("token");
        setUser(null);
    };

    return (
        <AuthContext.Provider value={{ user, login, logout, hasRole }}>
            {children}
        </AuthContext.Provider>
    );
}

export const useAuth = () => useContext(AuthContext);
