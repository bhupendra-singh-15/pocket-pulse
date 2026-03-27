import { createContext, useState, useEffect } from "react";
import api from "../api/axios";

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(localStorage.getItem("token"));

  const loadUser = async () => {
    try {
      const res = await api.get("/profile/me");
      setUser(res.data);
    } catch {
      logout();
    }
  };

  useEffect(() => {
    if (token) {
      localStorage.setItem("token", token);
      loadUser();
    } else {
      localStorage.removeItem("token");
    }
  }, [token]);

  const login = (data) => {
    localStorage.setItem("token", data.token); // 🔥 fix
    setToken(data.token);
    setUser(data.user);
  };

  const logout = () => {
    localStorage.removeItem("token"); // 🔥 fix
    setToken(null);
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, token, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
