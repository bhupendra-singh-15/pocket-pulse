import React from "react";
import { Menu } from "lucide-react";
import { useAuth } from "../../hooks/useAuth";
import { useNavigate } from "react-router-dom";

const Navbar = ({ toggleSidebar }) => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  return (
    <nav className="bg-gray-900/80 backdrop-blur-md text-gray-100 px-6 py-4 flex justify-between items-center border-b border-gray-800">
      {/* Left */}
      <div className="flex items-center gap-4">
        <button onClick={toggleSidebar}>
          <Menu size={26} />
        </button>

        <h1
          className="text-2xl font-bold text-cyan-400 cursor-pointer"
          onClick={() => navigate("/")}
        >
          PocketPulse
        </h1>
      </div>

      {/* Right */}
      <div className="flex gap-4 items-center">
        {!user ? (
          <>
            <button
              onClick={() => navigate("/login")}
              className="px-4 py-2 bg-cyan-500 rounded-lg"
            >
              Login
            </button>

            <button
              onClick={() => navigate("/signup")}
              className="px-4 py-2 border border-cyan-400 rounded-lg text-cyan-400"
            >
              Register
            </button>
          </>
        ) : (
          <>
            <span className="text-sm text-gray-300">Hi, {user.fullName}</span>

            <button
              onClick={logout}
              className="px-4 py-2 bg-red-500 rounded-lg"
            >
              Logout
            </button>
          </>
        )}
      </div>
    </nav>
  );
};

export default Navbar;
