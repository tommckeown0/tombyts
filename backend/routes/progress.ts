// File: backend/routes/progress.ts
import express, { Request, Response } from "express";
import { authenticateUser } from "./auth";
import Progress from "../models/progress";

const router = express.Router();

// Test progress route
router.get("/", authenticateUser, (req: Request, res: Response) => {
    res.send("Progress route");
});

// Update/create progress for a movie
router.post("/:movieId", authenticateUser, async (req, res) => {
    const movieId = req.params.movieId;
    const userId = req.user?.userId;
    const { progress } = req.body;

    try {
        // Find or create progress entry
        const updatedProgress = await Progress.findOneAndUpdate(
            { userId, movieId },
            { progress, updatedAt: new Date() },
            { upsert: true, new: true } // Create if doesn't exist, return updated doc
        );

        res.status(200).json(updatedProgress);
    } catch (error) {
        res.status(500).json({ error: "Failed to update/create progress" });
    }
});

// Get progress for a movie (for initial loading)
router.get("/:movieId", authenticateUser, async (req, res) => {
    const movieId = req.params.movieId;
    const userId = req.user?.userId;

    try {
        const progress = await Progress.findOne({ userId, movieId });
        res.status(200).json(progress);
    } catch (error) {
        res.status(500).json({ error: "Failed to fetch progress" });
    }
});

export default router;
