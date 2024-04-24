import express, { Request, Response } from "express";
import Movie from "../models/movie";

const router = express.Router();

// Get all movies
router.get("/", async (req: Request, res: Response) => {
    try {
        const movies = await Movie.find();
        res.json(movies);
    } catch (error: unknown) {
        // Type guard to check if error is an instance of Error
        if (error instanceof Error) {
            res.status(500).json({ message: error.message });
        } else {
            res.status(500).json({ message: "An unknown error occurred" });
        }
    }
});

// Add a new movie
router.post("/", async (req: Request, res: Response) => {
    const movie = new Movie({
        title: req.body.title,
        path: req.body.path,
    });

    try {
        const newMovie = await movie.save();
        res.status(201).json(newMovie);
    } catch (error: unknown) {
        // Type guard to check if error is an instance of Error
        if (error instanceof Error) {
            res.status(400).json({ message: error.message });
        } else {
            res.status(400).json({ message: "An unknown error occurred" });
        }
    }
});

export default router;
