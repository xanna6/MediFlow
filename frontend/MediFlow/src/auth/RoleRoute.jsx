import { Navigate } from "react-router-dom";
import { useAuth } from "./AuthContext";
import {getDefaultRouteForRole} from "./AuthRedirect.js";

export default function RoleRoute({ roles, children }) {
    const { user } = useAuth();

    if (!user) {
        return <Navigate to="/login" replace />;
    }

    const hasRequiredRole = roles.some(role =>
        user.roles.includes(role)
    );

    if (!hasRequiredRole) {
        const redirectPath = getDefaultRouteForRole(user);
        return <Navigate to={redirectPath} replace />;
    }

    return children;
}
