import React from "react";
import { LayoutDashboard, ArrowLeftRight, Tags, User, X } from "lucide-react";
import { useNavigate, useLocation } from "react-router-dom";

const Sidebar = ({ isOpen, closeSidebar }) => {
  const navigate = useNavigate();
  const location = useLocation();

  const isActive = (path) => location.pathname === path;

  const menuItems = [
    {
      name: "Dashboard",
      icon: <LayoutDashboard size={20} />,
      path: "/dashboard",
    },
    {
      name: "Transactions",
      icon: <ArrowLeftRight size={20} />,
      path: "/transactions",
    },
    {
      name: "Categories",
      icon: <Tags size={20} />,
      path: "/categories",
    },
    {
      name: "Profile",
      icon: <User size={20} />,
      path: "/profile",
    },
  ];

  return (
    <>
      {/* Overlay */}
      {isOpen && (
        <div
          onClick={closeSidebar}
          className="fixed inset-0 bg-black/50 z-30"
        />
      )}

      {/* Sidebar */}
      <div
        className={`fixed top-0 left-0 h-full w-64 bg-gray-950 text-gray-200 border-r border-gray-800 transform transition-transform duration-300 z-40
        ${isOpen ? "translate-x-0" : "-translate-x-full"}`}
      >
        {/* Header */}
        <div className="p-6 flex justify-between items-center border-b border-gray-800">
          <span className="text-xl font-bold text-cyan-400">PocketPulse</span>

          <button onClick={closeSidebar}>
            <X size={22} />
          </button>
        </div>

        {/* Menu */}
        <ul className="p-4 space-y-2">
          {menuItems.map((item) => (
            <li
              key={item.name}
              onClick={() => {
                navigate(item.path);
                closeSidebar();
              }}
              className={`flex items-center gap-3 p-2 rounded-lg cursor-pointer transition
                ${
                  isActive(item.path)
                    ? "bg-cyan-500 text-white"
                    : "hover:bg-gray-800 hover:text-cyan-400"
                }`}
            >
              {item.icon}
              {item.name}
            </li>
          ))}
        </ul>
      </div>
    </>
  );
};

export default Sidebar;
