import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import MoviesList from "./components/MoviesList";
import MoviePlayer from "./components/MoviePlayer";

function App() {
    return (
        <Router>
            <div className="App">
                <Routes>
                    {" "}
                    {/* Use Routes to wrap Route */}
                    <Route path="/" element={<MoviesList />} />
                    <Route path="/movie/:title" element={<MoviePlayer />} />
                </Routes>
            </div>
        </Router>
    );
}

export default App;
