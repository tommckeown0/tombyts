import React, { useContext } from "react";
import { Link as RouterLink } from "react-router-dom";
import { UserContext } from "../context/UserContext";
import { AppBar, Toolbar, Typography, Button } from "@mui/material";

const Header: React.FC = () => {
    const { userId, logout } = useContext(UserContext);

    return (
        <AppBar position="static">
            <Toolbar>
                <Typography
                    variant="h6"
                    component={RouterLink}
                    to="/"
                    sx={{
                        flexGrow: 1,
                        textDecoration: "none",
                        color: "inherit",
                    }}
                >
                    Tombyts
                </Typography>
                {userId ? (
                    <Button color="inherit" onClick={logout}>
                        Logout
                    </Button>
                ) : (
                    <Button color="inherit" component={RouterLink} to="/login">
                        Login
                    </Button>
                )}
            </Toolbar>
        </AppBar>
    );
};

export default Header;
