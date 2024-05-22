import express from "express";
import connectDB from "../config/database";
import movieRoutes from "../routes/movies";
import userRoutes from "../routes/users";
const cors = require("cors");

const app = express();
const PORT = process.env.PORT || 3001;

app.use(cors());

connectDB();

app.use(express.json());

app.use("/movies", movieRoutes);
app.use("/users", userRoutes);
app.use("/media", express.static("C:\\Users\\tommc\\Documents\\Torrents"));

app.get("/", (req, res) => {
    res.send("Hello from TypeScript and Express!");
});

app.listen(PORT, () => {
    console.log(`Server running on http://localhost:${PORT}`);
});
