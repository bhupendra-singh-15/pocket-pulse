import React, { useEffect, useState } from "react";
import { getDashboardData } from "../api/dashboardApi";
import DashboardChart from "../components/dashboard/DashboardChart";

const Dashboard = () => {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchDashboard = async () => {
      try {
        const res = await getDashboardData();
        setData(res.data);
      } catch (err) {
        console.error("Dashboard error:", err);
      } finally {
        setLoading(false);
      }
    };

    fetchDashboard();
  }, []);

  const getInsight = () => {
    if (!data) return "";

    if (data.totalExpense > data.totalIncome) {
      return "⚠️ You are spending more than you earn!";
    }

    if (data.balance > 0) {
      return "✅ Good job! You are saving money.";
    }

    return "⚖️ Your finances are balanced.";
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center text-white bg-gray-950">
        Loading dashboard...
      </div>
    );
  }

  if (!data) {
    return (
      <div className="min-h-screen flex items-center justify-center text-red-400 bg-gray-950">
        Failed to load dashboard
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-950 text-white p-6 space-y-6">
      <h1 className="text-3xl font-bold text-cyan-400">Dashboard</h1>

      {/* SUMMARY */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="bg-gray-900 p-6 rounded-xl">
          <p className="text-gray-400">Total Income</p>
          <h2 className="text-green-400 text-2xl font-bold">
            ₹ {data.totalIncome}
          </h2>
        </div>

        <div className="bg-gray-900 p-6 rounded-xl">
          <p className="text-gray-400">Total Expense</p>
          <h2 className="text-red-400 text-2xl font-bold">
            ₹ {data.totalExpense}
          </h2>
        </div>

        <div className="bg-gray-900 p-6 rounded-xl">
          <p className="text-gray-400">Balance</p>
          <h2 className="text-cyan-400 text-2xl font-bold">₹ {data.balance}</h2>
        </div>
      </div>

      {/* INSIGHT */}
      <div className="bg-gray-900 p-4 rounded-xl text-center text-lg">
        {getInsight()}
      </div>

      {/* CHART */}
      <DashboardChart data={data} />

      {/* RECENT TRANSACTIONS */}
      <div className="bg-gray-900 p-6 rounded-xl">
        <h2 className="text-xl font-semibold mb-4">Recent Transactions</h2>

        {data.recentTransactions.length === 0 ? (
          <p className="text-gray-400">No transactions yet</p>
        ) : (
          <ul className="space-y-2">
            {data.recentTransactions.map((tx) => (
              <li
                key={tx.id}
                className="flex justify-between border-b border-gray-800 pb-2"
              >
                <span>{tx.name}</span>
                <span
                  className={
                    tx.type === "INCOME" ? "text-green-400" : "text-red-400"
                  }
                >
                  ₹ {tx.amount}
                </span>
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
};

export default Dashboard;
