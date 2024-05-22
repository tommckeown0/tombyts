import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import MoviesList from "./components/MoviesList";
import MoviePlayer from "./components/MoviePlayer";
import Login from "./components/Login";
import ProtectedRoute from "./components/ProtectedRoute";
import Home from "./components/Home";
import { UserProvider } from "./context/UserContext";

function App() {
    return (
        <Router>
            <UserProvider>
                {" "}
                {/* Wrap Routes with UserProvider */}
                <div className="App">
                    <Routes>
                        {/* Use Routes to wrap Route */}
                        <Route path="/login" element={<Login />} />
                        <Route path="/" element={<Home />} />
                        <Route path="/" element={<ProtectedRoute />}>
                            <Route
                                path="/movieslist"
                                element={<MoviesList />}
                            />
                            <Route
                                path="/movie/:title"
                                element={<MoviePlayer />}
                            />
                            {/* Add more protected routes here */}
                        </Route>
                    </Routes>
                </div>
            </UserProvider>
        </Router>
    );
}

export default App;
