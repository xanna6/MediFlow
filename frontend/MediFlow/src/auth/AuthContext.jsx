import { createContext, useContext, useEffect, useState } from "react";

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

    const login = (token) => {
        localStorage.setItem("token", token);
        const payload = JSON.parse(atob(token.split(".")[1]));
        setUser({
            username: payload.sub,
            roles: payload.roles || [],
        });
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
