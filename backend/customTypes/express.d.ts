//File: backend/types/express.d.ts
declare namespace Express {
    interface Request {
        user?: { userId: string; username: string };
    }
}
