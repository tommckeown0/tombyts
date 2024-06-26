import express from "express";
import connectDB from "../config/database";
import movieRoutes from "../routes/movies";
import userRoutes from "../routes/users";
import loginRoutes from "../routes/auth";
import progressRoutes from "../routes/progress";
import dotenv from "dotenv";
import cors from "cors";
dotenv.config();

const app = express();
const PORT = process.env.PORT || 3001;

app.use(cors());

connectDB();

app.use(express.json());

app.use("/movies", movieRoutes);
app.use("/users", userRoutes);
app.use(
    "/media",
    express.static("C:\\Users\\tommc\\OneDrive\\Documents\\Torrents")
);
app.use("/progress", progressRoutes);

app.get("/", (req, res) => {
    res.send("Hello from TypeScript and Express!");
});

app.use("/auth", loginRoutes);

app.listen(PORT, () => {
    console.log(`Server running on http://localhost:${PORT}`);
});
