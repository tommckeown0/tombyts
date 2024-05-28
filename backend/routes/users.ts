import express, { Request, Response } from "express";
import bcrypt from "bcrypt";
import User, { IUser } from "../models/user";

const router = express.Router();

// Test user route
router.get("/", async (req: Request, res: Response) => {
    res.send("User route");
});

// Create a new user
router.post("/", async (req: Request, res: Response) => {
    const saltRounds = 10;
    const hashedPassword = await bcrypt.hash(req.body.password, saltRounds);

    const user: IUser = new User({
        username: req.body.username,
        email: req.body.email,
        passwordHash: hashedPassword,
    });

    try {
        const newUser = await user.save();
        res.status(201).json(newUser);
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
