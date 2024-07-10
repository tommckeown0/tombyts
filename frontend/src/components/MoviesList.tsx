import React, { useEffect, useState } from "react";
import axios from "axios";
import { Link } from "react-router-dom";
import { Movie } from "../types/Movie";
import { UserContext } from "../context/UserContext";

const MoviesList: React.FC = () => {
    const [movies, setMovies] = useState<Movie[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const { userId, username } = React.useContext(UserContext);

    useEffect(() => {
        const fetchMovies = async () => {
            try {
                const { data } = await axios.get(
                    "https://localhost:3001/movies"
                ); // Adjust the URL/port as necessary
                setMovies(data);
                setLoading(false);
            } catch (err) {
                setError("Failed to fetch movies");
                setLoading(false);
                console.error(err);
            }
        };

        fetchMovies();
    }, []);

    if (loading) return <div>Loading...</div>;
    if (error) return <div>Error: {error}</div>;

    return (
        <div>
            {userId && <span>Welcome, {userId}!</span>}{" "}
            {/* Display the user if logged in */}
            <p>Your username: {username}</p>
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
            <h1>Movies</h1>
            <ul>
                {movies.map((movie) => (
                    <li key={movie.title}>
                        <Link to={`/movie/${movie.title}`}>{movie.title}</Link>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default MoviesList;
