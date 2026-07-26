import api from "../axios";

import type {
    SemiTrailerSummary,
    SemiTrailerDetails,
    CreateSemiTrailerRequest,
    UpdateSemiTrailerRequest,
} from "@/models/vehicle/SemiTrailer";

export const getSemiTrailers = async (): Promise<SemiTrailerSummary[]> => {
    const response = await api.get<SemiTrailerSummary[]>("/semiTrailers");

    return response.data;
};

export const getSemiTrailer = async (id: number): Promise<SemiTrailerDetails> => {
    const response = await api.get<SemiTrailerDetails>(`/semiTrailers/${id}`);

    return response.data;
};

export const createSemiTrailer = async (semiTrailer: CreateSemiTrailerRequest): Promise<SemiTrailerSummary> => {
    const response = await api.post<SemiTrailerSummary>("/semiTrailers", semiTrailer);

    return response.data;
};

export const updateSemiTrailer = async (id: number, semiTrailer: UpdateSemiTrailerRequest): Promise<SemiTrailerSummary> => {
    const response = await api.put<SemiTrailerSummary>(`/semiTrailers/${id}`, semiTrailer);

    return response.data;
};

export const deleteSemiTrailer = async (id: number): Promise<void> => {
    await api.delete(`/semiTrailers/${id}`);
};