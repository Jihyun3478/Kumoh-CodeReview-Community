import axios from "axios";

const axiosInstance = axios.create({
  baseURL: "http://localhost:8080",
  withCredentials: true,
});

axios.defaults.withCredentials = true;
export default axiosInstance;