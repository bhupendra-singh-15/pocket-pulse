import api from "./axios";

export const loginUser = (data) => api.post("/auth/login", data);
export const registerUser = (data) => api.post("/profile/register", data);
export const getProfile = () => api.get("/profile/me");
