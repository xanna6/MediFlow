export const getDefaultRouteForRole = (user) => {
    if (!user || user.roles.length === 0) return "/";

    if (user.roles.includes("ROLE_DOCTOR"))
        return "/referrals";

    if (user.roles.includes("ROLE_REGISTRATION"))
        return "/appointments";

    if (user.roles.includes("ROLE_LAB"))
        return "/samples";

    return "/";
};