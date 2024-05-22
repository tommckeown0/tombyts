import express from "express";
import User from "../models/user";
import bcrypt from "bcrypt";
import jwt from "jsonwebtoken";

const router = express.Router();

router.post("/login", async (req, res) => {
    const { username, password } = req.body;

    try {
        const user = await User.findOne({ username });
        if (!user) {
            return res.status(404).json({ message: "User not found" });
        }

        const passwordMatch = await bcrypt.compare(password, user.passwordHash);
        if (!passwordMatch) {
            return res.status(401).json({ message: "Incorrect password" });
        }

        // Successful login - create and send JWT token
        const token = jwt.sign(
            { userId: user._id, username: user.username }, // Payload (user information)
            process.env.JWT_SECRET!, // Secret key (store securely!)
            { expiresIn: "1h" } // Options (e.g., expiration time)
        );

        // Successful login - handle session or token creation
        res.status(200).json({ message: "Login successful", token });
    } catch (error: unknown) {
        if (error instanceof Error) {
            res.status(500).json({ message: error.message });
        } else {
            res.status(500).json({ message: "An unknown error occurred" });
        }
    }
});

export default router;
