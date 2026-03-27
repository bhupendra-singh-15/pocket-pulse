import React, { useEffect, useState } from "react";

import {
  createTransaction,
  getTransactions,
  deleteTransaction,
  updateTransaction,
  searchTransactions,
  exportTransactions,
} from "../api/transactionApi";

import { getCategories } from "../api/categoryApi";
import { showSuccess, showError } from "../utils/toast";

const Transactions = () => {
  // DATA
  const [transactions, setTransactions] = useState([]);
  const [categories, setCategories] = useState([]);

  // FILTERS (independent)
  const [filterType, setFilterType] = useState("");
  const [search, setSearch] = useState("");
  const [month, setMonth] = useState("");
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");

  // FORM
  const [showForm, setShowForm] = useState(false);
  const [editingId, setEditingId] = useState(null);

  const [form, setForm] = useState({
    amount: "",
    name: "",
    description: "",
    date: "",
    categoryId: "",
  });

  // FETCH TRANSACTIONS
  const fetchTransactions = async () => {
    try {
      const params = {
        type: filterType || undefined,
        keyword: search || undefined,
        month: month || undefined,
        startDate: startDate || undefined,
        endDate: endDate || undefined,
      };

      let res;

      if (search) {
        res = await searchTransactions(params);
      } else {
        res = await getTransactions(params);
      }

      setTransactions(res.data);
    } catch (err) {
      showError(err);
    }
  };

  // FETCH CATEGORIES
  const fetchCategories = async () => {
    try {
      const res = await getCategories();
      setCategories(res.data);
    } catch (err) {
      showError(err);
    }
  };

  useEffect(() => {
    fetchTransactions();
    fetchCategories();
  }, [filterType, search, month, startDate, endDate]);

  // FORM CHANGE
  const handleChange = (e) => {
    setForm({
      ...form,
      [e.target.name]: e.target.value,
    });
  };

  //  SUBMIT
  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      if (editingId) {
        await updateTransaction(editingId, form);
        showSuccess("Transaction updated");
      } else {
        await createTransaction(form);
        showSuccess("Transaction created");
      }

      setForm({
        amount: "",
        name: "",
        description: "",
        date: "",
        categoryId: "",
      });

      setShowForm(false);
      setEditingId(null);

      fetchTransactions();
    } catch (err) {
      showError(err);
    }
  };

  // DELETE
  const handleDelete = async (id) => {
    try {
      await deleteTransaction(id);
      showSuccess("Transaction deleted");
      fetchTransactions();
    } catch (err) {
      showError(err);
    }
  };

  //  EDIT
  const handleEdit = (tx) => {
    setForm({
      amount: tx.amount,
      name: tx.name,
      description: tx.description || "",
      date: tx.date,
      categoryId: tx.categoryId,
    });

    setEditingId(tx.id);
    setShowForm(true);
  };

  // EXPORT
  const handleExport = async () => {
    try {
      const res = await exportTransactions({
        type: filterType || undefined,
        keyword: search || undefined,
        month: month || undefined,
        startDate: startDate || undefined,
        endDate: endDate || undefined,
      });

      const blob = new Blob([res.data], {
        type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
      });

      const url = window.URL.createObjectURL(blob);

      const link = document.createElement("a");
      link.href = url;
      link.download = "transactions.xlsx";
      link.click();

      window.URL.revokeObjectURL(url);

      showSuccess("Excel downloaded");
    } catch (err) {
      showError(err);
    }
  };

  // CLEAR FILTERS
  const clearFilters = () => {
    setFilterType("");
    setSearch("");
    setMonth("");
    setStartDate("");
    setEndDate("");
  };

  return (
    <div className="space-y-6">
      <h1 className="text-3xl font-bold text-cyan-400">Transactions</h1>

      {/* FILTER BAR */}
      <div className="flex flex-wrap gap-3 items-center bg-gray-900 p-4 rounded-xl">
        {/* TYPE */}
        <select
          value={filterType}
          onChange={(e) => setFilterType(e.target.value)}
          className="bg-gray-800 p-2 rounded"
        >
          <option value="">All</option>
          <option value="INCOME">Income</option>
          <option value="EXPENSE">Expense</option>
        </select>

        {/* SEARCH */}
        <input
          type="text"
          placeholder="Search..."
          className="bg-gray-800 p-2 rounded"
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />

        {/* MONTH */}
        <input
          type="month"
          value={month}
          onChange={(e) => setMonth(e.target.value)}
          className="bg-gray-800 p-2 rounded"
        />

        {/* START DATE */}
        <input
          type="date"
          value={startDate}
          onChange={(e) => setStartDate(e.target.value)}
          className="bg-gray-800 p-2 rounded"
        />

        {/* END DATE */}
        <input
          type="date"
          value={endDate}
          onChange={(e) => setEndDate(e.target.value)}
          className="bg-gray-800 p-2 rounded"
        />

        {/* CLEAR */}
        <button
          onClick={clearFilters}
          className="bg-gray-700 px-3 py-2 rounded"
        >
          Clear
        </button>

        {/* EXPORT */}
        <button
          onClick={handleExport}
          className="bg-green-500 px-4 py-2 rounded"
        >
          Export
        </button>

        {/* ADD */}
        <button
          onClick={() => {
            setShowForm(true);
            setEditingId(null);
          }}
          className="bg-cyan-500 px-4 py-2 rounded"
        >
          + Add
        </button>
      </div>

      {/*  FORM */}
      {showForm && (
        <form
          onSubmit={handleSubmit}
          className="bg-gray-900 p-6 rounded-xl max-w-md space-y-4"
        >
          <h2 className="text-xl font-semibold">
            {editingId ? "Update" : "Create"} Transaction
          </h2>

          <input
            type="number"
            name="amount"
            placeholder="Amount"
            value={form.amount}
            onChange={handleChange}
            className="w-full p-2 rounded bg-gray-800"
            required
          />

          <input
            type="text"
            name="name"
            placeholder="Title"
            value={form.name}
            onChange={handleChange}
            className="w-full p-2 rounded bg-gray-800"
            required
          />

          <input
            type="text"
            name="description"
            placeholder="Description"
            value={form.description}
            onChange={handleChange}
            className="w-full p-2 rounded bg-gray-800"
          />

          <input
            type="date"
            name="date"
            value={form.date}
            onChange={handleChange}
            className="w-full p-2 rounded bg-gray-800"
            required
          />

          <select
            name="categoryId"
            value={form.categoryId}
            onChange={handleChange}
            className="w-full p-2 rounded bg-gray-800"
            required
          >
            <option value="">Select Category</option>
            {categories.map((cat) => (
              <option key={cat.id} value={cat.id}>
                {cat.icon} {cat.name}
              </option>
            ))}
          </select>

          <div className="flex gap-3">
            <button className="bg-cyan-500 px-4 py-2 rounded">Save</button>

            <button
              type="button"
              onClick={() => setShowForm(false)}
              className="bg-gray-700 px-4 py-2 rounded"
            >
              Cancel
            </button>
          </div>
        </form>
      )}

      {/* LIST */}
      <div className="grid md:grid-cols-2 gap-4">
        {transactions.map((tx) => (
          <div key={tx.id} className="bg-gray-900 p-4 rounded-xl">
            <div className="flex justify-between">
              <p className="font-semibold">{tx.name}</p>
              <span
                className={
                  tx.type === "INCOME" ? "text-green-400" : "text-red-400"
                }
              >
                ₹ {tx.amount}
              </span>
            </div>

            <p className="text-sm text-gray-400">
              {tx.categoryName} • {tx.date}
            </p>

            {tx.description && (
              <p className="text-sm text-gray-300">{tx.description}</p>
            )}

            <div className="flex gap-2 mt-2">
              <button
                onClick={() => handleEdit(tx)}
                className="bg-yellow-500 px-2 py-1 rounded text-sm"
              >
                Edit
              </button>

              <button
                onClick={() => handleDelete(tx.id)}
                className="bg-red-500 px-2 py-1 rounded text-sm"
              >
                Delete
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Transactions;
