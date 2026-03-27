import React, { useState } from "react";
import { loginUser } from "../api/authApi";
import { useAuth } from "../hooks/useAuth";
import { useNavigate } from "react-router-dom";
import { showSuccess, showError } from "../utils/toast";
const Login = () => {
  const [form, setForm] = useState({
    email: "",
    password: "",
  });

  const [error, setError] = useState("");
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleChange = (e) => {
    setForm({
      ...form,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    try {
      const res = await loginUser(form);

      login(res.data);
      showSuccess("Login successful");

      // Redirect to dashboard
      navigate("/dashboard");
    } catch (err) {
      showError(err);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-950">
      <form
        onSubmit={handleSubmit}
        className="bg-gray-900 p-8 rounded-xl w-96 space-y-4"
      >
        <h2 className="text-2xl text-cyan-400 font-bold">Login</h2>

        {error && <p className="text-red-400">{error}</p>}

        <input
          type="email"
          name="email"
          placeholder="Email"
          className="w-full p-2 rounded bg-gray-800"
          onChange={handleChange}
          required
        />

        <input
          type="password"
          name="password"
          placeholder="Password"
          className="w-full p-2 rounded bg-gray-800"
          onChange={handleChange}
          required
        />

        <button className="w-full bg-cyan-500 py-2 rounded">Login</button>
      </form>
    </div>
  );
};

export default Login;
