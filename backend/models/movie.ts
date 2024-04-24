import mongoose, { Document, Schema } from "mongoose";

interface IMovie extends Document {
    title: string;
    path: string;
}

const movieSchema: Schema = new Schema({
    title: { type: String, required: true },
    path: { type: String, required: true },
});

const Movie = mongoose.model<IMovie>("Movie", movieSchema);

export default Movie;
