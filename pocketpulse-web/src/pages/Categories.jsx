import React, { useEffect, useState, useRef } from "react";
import EmojiPicker from "emoji-picker-react";
import { showSuccess, showError } from "../utils/toast";

import {
  createCategory,
  getCategories,
  getCategoriesByType,
  deleteCategory,
  updateCategory,
} from "../api/categoryApi";

const Categories = () => {
  const [categories, setCategories] = useState([]);
  const [filter, setFilter] = useState("ALL");

  const [showForm, setShowForm] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [showEmojiPicker, setShowEmojiPicker] = useState(false);

  const pickerRef = useRef();

  const [form, setForm] = useState({
    name: "",
    type: "EXPENSE",
    icon: "",
  });

  // Default icon
  const getDefaultIcon = (type) => {
    return type === "INCOME" ? "💰" : "💸";
  };

  //  Fetch categories
  const fetchCategories = async () => {
    try {
      let res;

      if (filter === "ALL") {
        res = await getCategories();
      } else {
        res = await getCategoriesByType(filter);
      }

      setCategories(res.data);
    } catch (err) {
      showError(err);
    }
  };

  useEffect(() => {
    fetchCategories();
  }, [filter]);

  // Close emoji picker on outside click
  useEffect(() => {
    const handleClickOutside = (e) => {
      if (pickerRef.current && !pickerRef.current.contains(e.target)) {
        setShowEmojiPicker(false);
      }
    };

    if (showEmojiPicker) {
      document.addEventListener("mousedown", handleClickOutside);
    }

    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [showEmojiPicker]);

  //  Form change
  const handleChange = (e) => {
    setForm({
      ...form,
      [e.target.name]: e.target.value,
    });
  };

  // Emoji select
  const handleEmojiClick = (emojiData) => {
    setForm((prev) => ({
      ...prev,
      icon: emojiData.emoji,
    }));
    setShowEmojiPicker(false);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const finalData = {
      ...form,
      icon: form.icon || getDefaultIcon(form.type),
    };

    try {
      if (editingId) {
        await updateCategory(editingId, finalData);
        showSuccess("Category updated successfully");
      } else {
        await createCategory(finalData);
        showSuccess("Category created successfully");
      }

      setForm({ name: "", type: "EXPENSE", icon: "" });
      setShowForm(false);
      setEditingId(null);

      fetchCategories();
    } catch (err) {
      showError(err);
    }
  };

  // Delete
  const handleDelete = async (id) => {
    try {
      await deleteCategory(id);
      showSuccess("Category deleted successfully");
      fetchCategories();
    } catch (err) {
      showError(err);
    }
  };

  //  Edit
  const handleEdit = (cat) => {
    setForm({
      name: cat.name,
      type: cat.type,
      icon: cat.icon || "",
    });
    setEditingId(cat.id);
    setShowForm(true);
  };

  return (
    <div className="space-y-6">
      <h1 className="text-3xl font-bold text-cyan-400">Categories</h1>

      {/* FILTER */}
      <div className="flex gap-3">
        {["ALL", "INCOME", "EXPENSE"].map((t) => (
          <button
            key={t}
            onClick={() => setFilter(t)}
            className={`px-4 py-2 rounded ${
              filter === t ? "bg-cyan-500" : "bg-gray-800 hover:bg-gray-700"
            }`}
          >
            {t}
          </button>
        ))}
      </div>

      {/* ADD BUTTON */}
      <button
        onClick={() => {
          setShowForm(true);
          setEditingId(null);
          setForm({ name: "", type: "EXPENSE", icon: "" });
        }}
        className="bg-cyan-500 px-4 py-2 rounded"
      >
        + Add Category
      </button>

      {/* FORM */}
      {showForm && (
        <form
          onSubmit={handleSubmit}
          className="bg-gray-900 p-6 rounded-xl max-w-md space-y-4 relative"
        >
          <h2 className="text-xl font-semibold">
            {editingId ? "Update Category" : "Create Category"}
          </h2>

          <input
            type="text"
            name="name"
            placeholder="Category Name"
            value={form.name}
            onChange={handleChange}
            className="w-full p-2 rounded bg-gray-800 text-blue-50"
            required
          />

          <select
            name="type"
            value={form.type}
            onChange={handleChange}
            className="w-full p-2 rounded bg-gray-800 text-blue-50"
          >
            <option value="EXPENSE">Expense</option>
            <option value="INCOME">Income</option>
          </select>

          {/*  ICON + PICKER */}
          <div className="space-y-2">
            <label className="text-sm text-gray-400">Category Icon</label>

            <div className="flex items-center gap-3">
              <div className="text-2xl">
                {form.icon || getDefaultIcon(form.type)}
              </div>

              <button
                type="button"
                onClick={(e) => {
                  e.stopPropagation();
                  setShowEmojiPicker(!showEmojiPicker);
                }}
                className="bg-gray-800 px-3 py-1 rounded"
              >
                Pick Emoji
              </button>
            </div>
          </div>

          {/* EMOJI PICKER FIXED */}
          {showEmojiPicker && (
            <div ref={pickerRef} className="fixed bottom-10 right-10 z-50">
              <div className="max-h-400px overflow-y-auto shadow-lg">
                <EmojiPicker onEmojiClick={handleEmojiClick} />
              </div>
            </div>
          )}

          <div className="flex gap-3">
            <button className="bg-cyan-500 px-4 py-2 rounded">
              {editingId ? "Update" : "Create"}
            </button>

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
      <div className="grid md:grid-cols-3 gap-4">
        {categories.map((cat) => (
          <div key={cat.id} className="bg-gray-900 p-4 rounded-xl space-y-2">
            <div className="flex justify-between items-center">
              <p className="font-semibold">
                {cat.icon || getDefaultIcon(cat.type)} {cat.name}
              </p>
              <span className="text-sm text-gray-400">{cat.type}</span>
            </div>

            <div className="flex gap-2">
              <button
                onClick={() => handleEdit(cat)}
                className="bg-yellow-500 px-2 py-1 rounded text-sm"
              >
                Edit
              </button>

              <button
                onClick={() => handleDelete(cat.id)}
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

export default Categories;
