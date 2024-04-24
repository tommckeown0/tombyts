import mongoose from "mongoose";

const connectionString: string = process.env.MONGO_URI as string;
const connectDB = async () => {
    console.log("Connecting to MongoDB...");
    try {
        await mongoose.connect(connectionString);
        console.log("MongoDB connected successfully");
    } catch (error) {
        if (error instanceof Error) {
            console.error("MongoDB connection failed:", error.message);
        } else {
            console.error("An unknown error occurred:", error);
        } // Exit process with failure
        process.exit(1);
    }
};

export default connectDB;
