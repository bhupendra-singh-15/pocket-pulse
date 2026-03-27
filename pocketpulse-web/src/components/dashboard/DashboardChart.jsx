import React from "react";
import {
  PieChart,
  Pie,
  Cell,
  Tooltip,
  BarChart,
  Bar,
  XAxis,
  YAxis,
  ResponsiveContainer,
  CartesianGrid,
} from "recharts";

const COLORS = ["#22c55e", "#ef4444"];

const DashboardChart = ({ data }) => {
  const pieData = [
    { name: "Income", value: Number(data.totalIncome) },
    { name: "Expense", value: Number(data.totalExpense) },
  ];

  // CATEGORY EXPENSE
  const categoryMap = {};

  data.recentTransactions.forEach((tx) => {
    if (tx.type !== "EXPENSE") return;

    if (!categoryMap[tx.categoryName]) {
      categoryMap[tx.categoryName] = 0;
    }

    categoryMap[tx.categoryName] += Number(tx.amount);
  });

  const barData = Object.entries(categoryMap)
    .map(([name, value]) => ({ name, value }))
    .sort((a, b) => b.value - a.value)
    .slice(0, 5); //

  const savingsRate =
    data.totalIncome > 0
      ? ((data.balance / data.totalIncome) * 100).toFixed(1)
      : 0;

  const topCategory = barData[0]?.name || "N/A";

  return (
    <div className="space-y-6">
      {/* INSIGHT CARDS */}
      <div className="grid md:grid-cols-3 gap-4">
        <div className="bg-gray-900 p-4 rounded-xl text-center">
          <p className="text-gray-400 text-sm">Savings Rate</p>
          <h2 className="text-green-400 text-xl font-bold">{savingsRate}%</h2>
        </div>

        <div className="bg-gray-900 p-4 rounded-xl text-center">
          <p className="text-gray-400 text-sm">Top Expense</p>
          <h2 className="text-red-400 text-xl font-bold">{topCategory}</h2>
        </div>

        <div className="bg-gray-900 p-4 rounded-xl text-center">
          <p className="text-gray-400 text-sm">Transactions</p>
          <h2 className="text-cyan-400 text-xl font-bold">
            {data.recentTransactions.length}
          </h2>
        </div>
      </div>

      {/* CHARTS */}
      <div className="grid md:grid-cols-2 gap-6">
        {/* PIE */}
        <div className="bg-gray-900 p-6 rounded-xl">
          <h2 className="text-lg font-semibold mb-4">Income vs Expense</h2>

          <ResponsiveContainer width="100%" height={280}>
            <PieChart>
              <Pie data={pieData} dataKey="value" outerRadius={100} label>
                {pieData.map((_, i) => (
                  <Cell key={i} fill={COLORS[i]} />
                ))}
              </Pie>

              <Tooltip />
            </PieChart>
          </ResponsiveContainer>
        </div>

        {/* BAR */}
        <div className="bg-gray-900 p-6 rounded-xl">
          <h2 className="text-lg font-semibold mb-4">Top Expenses</h2>

          <ResponsiveContainer width="100%" height={280}>
            <BarChart data={barData}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="name" />
              <YAxis />
              <Tooltip />

              <Bar dataKey="value" fill="#ef4444" radius={[6, 6, 0, 0]} />
            </BarChart>
          </ResponsiveContainer>
        </div>
      </div>
    </div>
  );
};

export default DashboardChart;
