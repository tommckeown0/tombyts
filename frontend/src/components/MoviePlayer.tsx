import React, { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import axios from "axios";

const MoviePlayer = () => {
    const { title = "Default Title" } = useParams(); // This gets the movie id from the URL
    const [movie, setMovie] = useState<{
        title: string;
        path: string;
    } | null>(null);

    useEffect(() => {
        axios
            .get(`http://localhost:3001/movies/${encodeURIComponent(title)}`)
            .then((response) => {
                const moviePath = `http://localhost:3001/media/${response.data.path}`;
                setMovie({ ...response.data, path: moviePath });
            })
            .catch((error) => console.error("Error fetching movie", error));
    }, [title]);

    if (!movie) return <div>Loading...</div>;

    return (
        <div style={{ position: "relative" }}>
            {/* Home button */}
            <div style={{ position: "absolute", top: "10px", left: "10px" }}>
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
            <div>
                <h1>{movie.title}</h1>
                <video width="100%" controls>
                    <source src={movie.path} type="video/mp4" />
                    Your browser does not support the video tag.
                </video>
            </div>
        </div>
    );
};

export default MoviePlayer;
