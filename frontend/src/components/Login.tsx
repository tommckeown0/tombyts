import React, { useState } from "react";
import axios from "axios";
import { Link } from "react-router-dom";

const Login = () => {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        try {
            const res = await axios.post("http://localhost:3001/auth/login", {
                username,
                password,
            });

            if (res.status === 200) {
                // Extract the token from the response
                const token = res.data.token;

                // Store the token in localStorage
                localStorage.setItem("token", token); // Or use sessionStorage

                // Redirect to the main page
                window.location.href = "/movieslist"; // Replace "/" with the actual path of your main page
            } else {
                // Handle unsuccessful login (e.g., show an error message)
                console.error("Login failed:", res.data.message);
            }
        } catch (err) {
            console.error("Login error:", err);
        }
    };

    return (
        <div>
            <div style={{ position: "relative", top: "10px", left: "10px" }}>
                <Link
                    to="/"
                    style={{
                        textDecoration: "none",
                        color: "white",
                        background: "blue",
                        padding: "8px 15px",
                        borderRadius: "5px",
                    }}
                >
                    Home
                </Link>
            </div>
            <h1>Login</h1>
            <form onSubmit={handleSubmit}>
                <label>
                    Username:
                    <input
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />
                </label>
                <label>
                    Password:
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                </label>
                <input type="submit" value="Login" />
            </form>
        </div>
    );
};

export default Login;
