import axios from "axios";

const API_URL =
  process.env.NEXT_PUBLIC_API_URL || "http://localhost:8081/api/v1";

export const apiClient = axios.create({
  baseURL: API_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

export interface Library {
  id: number;
  name: string;
  source: string;
  version: string;
  healthScore: number;
  license: string;
  description: string;
}

export interface LibrariesResponse {
  libraries: Library[];
  totalElements: number;
  currentPage: number;
  totalPages: number;
}

export const libraryApi = {
  getAll: (page = 0, size = 10) =>
    apiClient.get<LibrariesResponse>(`/libraries?page=${page}&size=${size}`),

  search: (query: string, source?: string, page = 0, size = 10) => {
    let url = `/libraries/search?query=${query}&page=${page}&size=${size}`;
    if (source) url += `&source=${source}`;
    return apiClient.get<LibrariesResponse>(url);
  },

  getById: (id: number) => apiClient.get<Library>(`/libraries/${id}`),
};
