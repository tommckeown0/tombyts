import express, { Request, Response } from "express";
import Movie from "../models/movie";
import { authenticateUser } from "./auth";

const router = express.Router();

// Get all movies
router.get("/", authenticateUser, async (req: Request, res: Response) => {
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
router.post("/", authenticateUser, async (req: Request, res: Response) => {
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

// GET movie by title
router.get("/:title", authenticateUser, async (req, res) => {
    try {
        // Decode the title to handle spaces and special characters
        const title = decodeURIComponent(req.params.title);

        // Find the movie by title
        const movie = await Movie.findOne({ title: title });

        if (movie) {
            res.json(movie);
        } else {
            res.status(404).send("Movie not found");
        }
    } catch (err) {
        console.error(err);
        res.status(500).send("Server error");
    }
});

export default router;
