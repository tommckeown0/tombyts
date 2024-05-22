// File: UserContext.tsx
import React, { createContext, useState, useEffect } from "react";
import { jwtDecode } from "jwt-decode";

interface UserContextType {
    userId: string | null;
    setUserId: React.Dispatch<React.SetStateAction<string | null>>;
    logout: () => void;
}

export const UserContext = createContext<UserContextType>({
    userId: null,
    setUserId: () => {},
    logout: () => {}, // Provide a default empty function
});

interface UserProviderProps {
    children: React.ReactNode;
}

export const UserProvider: React.FC<UserProviderProps> = ({ children }) => {
    const [userId, setUserId] = useState<string | null>(null);

    useEffect(() => {
        const storedToken = localStorage.getItem("token");
        if (storedToken) {
            try {
                const decodedToken = jwtDecode(storedToken) as {
                    userId: string;
                };
                setUserId(decodedToken.userId);
            } catch (error) {
                console.error("Error decoding token:", error);
                // You might want to handle token expiration here as well
            }
        }
    }, []);

    const logout = () => {
        localStorage.removeItem("token");
        setUserId(null);
        // Optionally, redirect the user to the login page:
        window.location.href = "/";
    };

    return (
        <UserContext.Provider value={{ userId, setUserId, logout }}>
            {children}
        </UserContext.Provider>
    );
};
