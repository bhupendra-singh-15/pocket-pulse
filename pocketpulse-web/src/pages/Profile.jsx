import React from "react";
import { useAuth } from "../hooks/useAuth";
import { useNavigate } from "react-router-dom";
import { showSuccess } from "../utils/toast";

const Profile = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    showSuccess("Logged out successfully");
    navigate("/");
  };

  if (!user) {
    return (
      <div className="min-h-screen flex items-center justify-center text-gray-400">
        Loading profile...
      </div>
    );
  }

  return (
    <div className="max-w-4xl mx-auto space-y-6">
      {/* HEADER */}
      <div>
        <h1 className="text-3xl font-bold text-cyan-400">Profile</h1>
        <p className="text-gray-400 text-sm">Your account information</p>
      </div>

      {/* PROFILE CARD */}
      <div className="bg-gray-900 rounded-xl p-6 shadow-lg border border-gray-800">
        <div className="flex items-center gap-6">
          {/* Avatar */}
          <div className="w-20 h-20 rounded-full bg-cyan-500 flex items-center justify-center text-3xl font-bold overflow-hidden">
            {user.profileImageUrl ? (
              <img
                src={user.profileImageUrl}
                alt="profile"
                className="w-full h-full object-cover"
              />
            ) : (
              user.fullName?.charAt(0).toUpperCase()
            )}
          </div>

          {/* Basic Info */}
          <div>
            <h2 className="text-xl font-semibold">{user.fullName}</h2>
            <p className="text-gray-400">{user.email}</p>

            <span
              className={`inline-block mt-2 px-3 py-1 text-xs rounded-full ${
                user.isActive
                  ? "bg-green-500/20 text-green-400"
                  : "bg-red-500/20 text-red-400"
              }`}
            >
              {user.isActive ? "Active" : "Inactive"}
            </span>
          </div>
        </div>

        {/* DETAILS */}
        <div className="grid md:grid-cols-2 gap-6 mt-6">
          <div>
            <p className="text-gray-400 text-sm">User ID</p>
            <p className="font-semibold">{user.id}</p>
          </div>

          <div>
            <p className="text-gray-400 text-sm">Email</p>
            <p className="font-semibold">{user.email}</p>
          </div>

          <div>
            <p className="text-gray-400 text-sm">Joined On</p>
            <p className="font-semibold">
              {new Date(user.createdAt).toLocaleDateString()}
            </p>
          </div>

          <div>
            <p className="text-gray-400 text-sm">Last Updated</p>
            <p className="font-semibold">
              {new Date(user.updatedAt).toLocaleDateString()}
            </p>
          </div>
        </div>
      </div>

      {/* ACTIONS */}
      <div className="flex justify-between items-center">
        <button
          onClick={() => navigate("/dashboard")}
          className="bg-gray-800 hover:bg-gray-700 px-4 py-2 rounded-lg transition"
        >
          ← Back to Dashboard
        </button>

        <button
          onClick={handleLogout}
          className="bg-red-500 hover:bg-red-600 px-4 py-2 rounded-lg transition"
        >
          Logout
        </button>
      </div>
    </div>
  );
};

export default Profile;
