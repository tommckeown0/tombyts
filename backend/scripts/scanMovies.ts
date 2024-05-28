import fs from "fs/promises";
import path from "path";
import connectDB from "../config/database";
import Movie from "../models/movie";

const supportedExtensions = [".mp4", ".mkv", ".avi"]; // Add more if needed
const torrentsDir = "C:\\Users\\tommc\\OneDrive\\Documents\\Torrents";

type ScanMoviesFunction = (directoryPath?: string) => Promise<void>;

const scanMovies: ScanMoviesFunction = async (directoryPath = torrentsDir) => {
    try {
        await connectDB();

        const files = await fs.readdir(directoryPath);

        for (const file of files) {
            const filePath = path.join(directoryPath, file);
            const stats = await fs.stat(filePath);

            if (
                stats.isFile() &&
                supportedExtensions.includes(path.extname(file).toLowerCase())
            ) {
                const title = path.basename(file, path.extname(file));

                // Correctly calculate relative path from the root torrentsDir
                const relativePath = path.relative(torrentsDir, filePath);

                // Check if the movie already exists in the database
                const existingMovie = await Movie.findOne({ title });
                if (!existingMovie) {
                    await Movie.create({ title, path: relativePath });
                } else {
                    console.log(
                        `Movie "${title}" already exists in the database.`
                    );
                }
            } else if (stats.isDirectory()) {
                await scanMovies(filePath); // Recursively scan subdirectories
            }
        }
    } catch (error) {
        console.error("Error scanning movies:", error);
    }
};

scanMovies();