import axios from "axios";
import { clearSession, getToken } from "./auth";

export const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL ?? ""
});

api.interceptors.request.use((config) => {
  const token = getToken();
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401 && !error.config?.url?.includes("/auth/")) {
      clearSession();
    }
    return Promise.reject(error);
  }
);

function cleanParams(params) {
  return Object.fromEntries(
    Object.entries(params).filter(([, value]) => value !== "" && value !== undefined && value !== null)
  );
}

export async function login(payload) {
  const { data } = await api.post("/api/v1/auth/login", payload);
  return data;
}

export async function register(payload) {
  const { data } = await api.post("/api/v1/auth/register", payload);
  return data;
}

export async function fetchProfile() {
  const { data } = await api.get("/api/v1/users/me");
  return data;
}

export async function updateProfile(payload) {
  const { data } = await api.put("/api/v1/users/me", payload);
  return data;
}

export async function fetchStreams(filters) {
  const { data } = await api.get("/api/v1/streams", { params: cleanParams(filters) });
  return data;
}

export async function fetchServices(filters) {
  const { data } = await api.get("/api/v1/services", { params: cleanParams(filters) });
  return data;
}

export async function createBooking(serviceId, payload) {
  const { data } = await api.post(`/api/v1/services/${serviceId}/bookings`, payload);
  return data;
}

export async function fetchProducts(filters) {
  const { data } = await api.get("/api/v1/products", { params: cleanParams(filters) });
  return data;
}

export async function createOrder(payload) {
  const { data } = await api.post("/api/v1/orders", payload);
  return data;
}

export async function fetchOrders() {
  const { data } = await api.get("/api/v1/orders/me");
  return data;
}

export async function fetchConversations() {
  const { data } = await api.get("/api/v1/chat/conversations");
  return data;
}

export async function createConversation(payload) {
  const { data } = await api.post("/api/v1/chat/conversations", payload);
  return data;
}

export async function fetchMessages(conversationId) {
  const { data } = await api.get(`/api/v1/chat/conversations/${conversationId}/messages`);
  return data;
}

export async function sendMessage(conversationId, payload) {
  const { data } = await api.post(`/api/v1/chat/conversations/${conversationId}/messages`, payload);
  return data;
}
