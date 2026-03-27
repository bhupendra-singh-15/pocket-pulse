import toast from "react-hot-toast";

export const showSuccess = (message) => {
  toast.success(message || "Success");
};

export const showError = (error) => {
  if (typeof error === "string") {
    toast.error(error);
  } else if (error?.response?.data) {
    const msg =
      typeof error.response.data === "string"
        ? error.response.data
        : Object.values(error.response.data).join(", ");

    toast.error(msg);
  } else {
    toast.error("Something went wrong");
  }
};
