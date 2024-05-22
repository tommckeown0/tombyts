import { Navigate, Outlet } from "react-router-dom";
import { useEffect, useState } from "react";

const ProtectedRoute = () => {
    const token = localStorage.getItem("token");
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        // Verify token validity on the backend (async function)
        // If invalid, clear localStorage and setIsLoading(false)

        setIsLoading(false);
    }, []);

    if (isLoading) {
        return <div>Loading...</div>; // Or a loading spinner
    }

    return token ? <Outlet /> : <Navigate to="/login" />;
};

export default ProtectedRoute;
