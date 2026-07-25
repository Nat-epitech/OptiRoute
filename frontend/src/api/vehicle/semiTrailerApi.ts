import api from "../axios";

import type {
    SemiTrailer,
    SemiTrailerDetails,
    CreateSemiTrailerRequest,
    UpdateSemiTrailerRequest,
} from "@/models/vehicle/SemiTrailer";

export const getSemiTrailers = async (): Promise<SemiTrailer[]> => {
    const response = await api.get<SemiTrailer[]>("/semiTrailer");
    return response.data;
};

export const getSemiTrailer = async (
    id: number
): Promise<SemiTrailerDetails> => {
    const response = await api.get<SemiTrailerDetails>(`/semiTrailer/${id}`);
    return response.data;
};

export const createSemiTrailer = async (
    semiTrailer: CreateSemiTrailerRequest
): Promise<SemiTrailer> => {
    const response = await api.post<SemiTrailer>(
        "/semiTrailer",
        semiTrailer
    );
    return response.data;
};

export const updateSemiTrailer = async (
    id: number,
    semiTrailer: UpdateSemiTrailerRequest
): Promise<SemiTrailer> => {
    const response = await api.put<SemiTrailer>(
        `/semiTrailer/${id}`,
        semiTrailer
    );
    return response.data;
};

export const deleteSemiTrailer = async (id: number): Promise<void> => {
    await api.delete(`/semiTrailer/${id}`);
};