import api from "./axios";

export const createTransaction = (data) => api.post("/transactions", data);

export const getTransactions = (params) => api.get("/transactions", { params });

export const searchTransactions = (params) =>
  api.get("/transactions/search", { params });

export const updateTransaction = (id, data) =>
  api.put(`/transactions/${id}`, data);

export const deleteTransaction = (id) => api.delete(`/transactions/${id}`);

export const exportTransactions = (params) =>
  api.get("/transactions/export", {
    params,
    responseType: "blob",
  });
