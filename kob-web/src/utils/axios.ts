import type { AxiosInstance, AxiosResponse, InternalAxiosRequestConfig } from 'axios';
import axios from 'axios';
import { tokenStorage } from './token-storage';

const AXIOS_TIMEOUT = 15000;

export interface ApiResponse<T = any> {
  code: number
  data: T
  msg: string
}

const api: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_BASE_API_URL as string,
  timeout: AXIOS_TIMEOUT,
});

api.interceptors.request.use(
  (config: InternalAxiosRequestConfig): InternalAxiosRequestConfig => {
    if (tokenStorage.get()) {
      config.headers = config.headers || {};
      config.headers.Authorization = `Bearer ${tokenStorage.get()}`;
    }
    return config;
  },
  (error: any) => Promise.reject(error),
);

api.interceptors.response.use(
  (response: AxiosResponse): AxiosResponse<ApiResponse> => {
    const apiResponse: ApiResponse = {
      code: response.data.code,
      data: response.data.data,
      msg: response.data.msg,
    };
    return { ...response, data: apiResponse };
  },
  (error: any) => Promise.reject(error),
);

export default api;
