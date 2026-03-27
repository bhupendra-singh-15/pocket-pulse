import { useEffect, useState, useRef } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";
import api from "../api/axios";

const Activation = () => {
  const [status, setStatus] = useState("Activating your account...");
  const [success, setSuccess] = useState(false);

  const [params] = useSearchParams();
  const navigate = useNavigate();

  const called = useRef(false);

  const token = params.get("token");

  useEffect(() => {
    if (!token || called.current) return;

    called.current = true;

    const activate = async () => {
      try {
        await api.get(`/profile/activate?token=${token}`);

        setSuccess(true);
        setStatus("✅ Account activated successfully!");
      } catch (err) {
        setSuccess(false);
        setStatus("❌ Invalid or expired activation link.");
      }
    };

    activate();
  }, [token]);

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-950 text-white px-4">
      <div className="bg-gray-900 p-8 rounded-xl shadow-lg text-center max-w-md w-full space-y-4">
        <h2 className="text-2xl font-bold text-cyan-400">Account Activation</h2>

        <p className="text-lg">{status}</p>

        {/* Show login button only on success */}
        {success && (
          <button
            onClick={() => navigate("/login")}
            className="mt-4 w-full bg-cyan-500 hover:bg-cyan-600 py-2 rounded-lg font-semibold transition"
          >
            Go to Login
          </button>
        )}
      </div>
    </div>
  );
};

export default Activation;
