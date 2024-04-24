import express from "express";
import connectDB from "../config/database";
import movieRoutes from "../routes/movies";

const app = express();
const PORT = process.env.PORT || 3001;

connectDB();

app.use(express.json());

app.use("/movies", movieRoutes);

app.get("/", (req, res) => {
    res.send("Hello from TypeScript and Express!");
});

app.listen(PORT, () => {
    console.log(`Server running on http://localhost:${PORT}`);
});
