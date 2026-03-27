import React, { useState } from "react";
import Navbar from "../components/common/Navbar";
import Footer from "../components/common/Footer";
import Sidebar from "../components/common/Sidebar";
import { useAuth } from "../hooks/useAuth";

const Home = () => {
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);

  const { user } = useAuth();

  const toggleSidebar = () => {
    setIsSidebarOpen((prev) => !prev);
  };

  const closeSidebar = () => {
    setIsSidebarOpen(false);
  };

  return (
    <div className="min-h-screen flex flex-col bg-linear-to-br from-gray-950 via-gray-900 to-gray-800">
      {/* Sidebar */}
      <Sidebar isOpen={isSidebarOpen} closeSidebar={closeSidebar} />

      {/* Navbar */}
      <Navbar toggleSidebar={toggleSidebar} />

      {/* Main */}
      <main className="flex-1 flex items-center justify-center px-4 text-center">
        <div className="bg-gray-900/60 backdrop-blur-lg rounded-2xl p-10 shadow-2xl border border-gray-800 max-w-xl">
          <h2 className="text-4xl font-bold mb-4 text-cyan-400">
            Welcome to PocketPulse 💸
          </h2>

          <p className="text-gray-300 text-lg">
            Smartly manage your money with powerful tracking and insights.
          </p>

          {/*  Show user state */}
          {user ? (
            <p className="mt-4 text-green-400">Logged in as {user.fullName}</p>
          ) : (
            <p className="mt-4 text-gray-400">Please login to continue</p>
          )}
        </div>
      </main>

      <Footer />
    </div>
  );
};

export default Home;
