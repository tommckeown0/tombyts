import React, { useContext } from "react";
import { Link as RouterLink } from "react-router-dom";
import { UserContext } from "../context/UserContext";
import { Container, Typography, Box, Link } from "@mui/material";
import Header from "./Header"; // Adjust the import path as necessary

const Home: React.FC = () => {
    const { userId, username } = useContext(UserContext);

    return (
        <Container>
            <Header />
            <Box sx={{ my: 4 }}>
                <Typography variant="h4" component="h1" gutterBottom>
                    Welcome to Tombyts
                </Typography>
                {userId ? (
                    <Box>
                        <Typography variant="body1">
                            Your User ID: {userId}
                        </Typography>
                        <Typography variant="body1">
                            Your username: {username}
                        </Typography>
                    </Box>
                ) : (
                    <Typography variant="body1">
                        Please log in to access more features.
                    </Typography>
                )}

                <Box sx={{ mt: 2 }}>
                    <nav>
                        {userId && (
                            <Link component={RouterLink} to="/movieslist">
                                Movies
                            </Link>
                        )}
                    </nav>
                </Box>
            </Box>
        </Container>
    );
};

export default Home;
