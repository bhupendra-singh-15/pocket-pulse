import React, { useState } from "react";
import { registerUser } from "../api/authApi";
import { useNavigate } from "react-router-dom";
import { showSuccess, showError } from "../utils/toast";

const SignUp = () => {
  const [form, setForm] = useState({
    fullName: "",
    email: "",
    password: "",
  });

  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

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
    setMessage("");

    try {
      await registerUser(form);

      showSuccess("Registration successful! Check your email.");

      // optional redirect after 3 sec
      setTimeout(() => {
        navigate("/login");
      }, 3000);
    } catch (err) {
      const backendError = err.response?.data;

      if (typeof backendError === "object") {
        // convert object → string
        const messages = Object.values(backendError).join(", ");
        setError(messages);
      } else {
        setError(backendError || "Registration failed");
        showError(err);
      }
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-950">
      <form
        onSubmit={handleSubmit}
        className="bg-gray-900 p-8 rounded-xl w-96 space-y-4"
      >
        <h2 className="text-2xl text-cyan-400 font-bold">Sign Up</h2>

        {message && <p className="text-green-400">{message}</p>}
        {error && <p className="text-red-400">{error}</p>}

        <input
          type="text"
          name="fullName"
          placeholder="Full Name"
          className="w-full p-2 rounded bg-gray-800"
          onChange={handleChange}
          required
        />

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

        <button className="w-full bg-cyan-500 py-2 rounded">Register</button>
      </form>
    </div>
  );
};

export default SignUp;
