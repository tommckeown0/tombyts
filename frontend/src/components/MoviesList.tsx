import React, { useEffect, useState } from "react";
import axios from "axios";
import { Link } from "react-router-dom";
import { Movie } from "../types/Movie";

const MoviesList: React.FC = () => {
    const [movies, setMovies] = useState<Movie[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        const fetchMovies = async () => {
            try {
                const { data } = await axios.get(
                    "http://localhost:3001/movies"
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
