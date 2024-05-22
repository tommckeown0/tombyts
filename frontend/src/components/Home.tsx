import React, { useContext } from "react";
import { Link } from "react-router-dom";
import { UserContext } from "../context/UserContext";

const Home: React.FC = () => {
    const { userId, username, logout } = useContext(UserContext);

    return (
        <div>
            <h1>Welcome to My Media App</h1>

            {userId ? (
                <>
                    <p>Your User ID: {userId}</p>
                    <p>Your username: {username}</p>
                    <button onClick={logout}>Logout</button>
                </>
            ) : (
                <Link to="/login">Login</Link>
            )}

            <nav>
                <ul>
                    <li>
                        <Link to="/">Home</Link>
                    </li>
                    <li>
                        <Link to="/movieslist">Movies</Link>
                    </li>
                </ul>
            </nav>
        </div>
    );
};

export default Home;
