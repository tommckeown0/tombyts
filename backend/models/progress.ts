import mongoose from "mongoose";

const progressSchema = new mongoose.Schema({
    userId: {
        type: mongoose.Schema.Types.ObjectId,
        ref: "User",
        required: true,
    },
    movieId: { type: String, required: true }, // Assuming you're using movie title as a unique identifier
    progress: { type: Number, default: 0 }, // Percentage watched (0-100)
    updatedAt: { type: Date, default: Date.now },
});

const Progress = mongoose.model("Progress", progressSchema);

export default Progress;
