import React, { useEffect, useState, useContext } from "react";
import axios from "axios";
import { Link as RouterLink } from "react-router-dom";
import { Movie } from "../types/Movie";
import { UserContext } from "../context/UserContext";
import {
    Container,
    Typography,
    Box,
    Link,
    CircularProgress,
    Alert,
    List,
    ListItem,
    ListItemText,
} from "@mui/material";
import Header from "./Header";

const MoviesList: React.FC = () => {
    const [movies, setMovies] = useState<Movie[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const { userId, username, logout } = useContext(UserContext);

    useEffect(() => {
        const fetchMovies = async () => {
            try {
                const token = localStorage.getItem("token");
                const { data } = await axios.get(
                    "https://localhost:3001/movies",
                    {
                        headers: {
                            Authorization: `Bearer ${token}`,
                        },
                    }
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

    return (
        <Container>
            <Header />
            <Box sx={{ my: 4 }}>
                <Typography variant="h4" component="h1" gutterBottom>
                    Movies
                </Typography>
                {userId && (
                    <Box>
                        <Typography variant="body1">
                            Your User ID {userId}!
                        </Typography>
                        <Typography variant="body1">
                            Your username: {username}
                        </Typography>
                    </Box>
                )}
                {loading ? (
                    <Box
                        sx={{
                            display: "flex",
                            justifyContent: "center",
                            mt: 4,
                        }}
                    >
                        <CircularProgress />
                    </Box>
                ) : error ? (
                    <Alert severity="error">{error}</Alert>
                ) : (
                    <List>
                        {movies.map((movie) => (
                            <ListItem key={movie.title}>
                                <ListItemText>
                                    <Link
                                        component={RouterLink}
                                        to={`/movie/${movie.title}`}
                                    >
                                        {movie.title}
                                    </Link>
                                </ListItemText>
                            </ListItem>
                        ))}
                    </List>
                )}
            </Box>
        </Container>
    );
};

export default MoviesList;
