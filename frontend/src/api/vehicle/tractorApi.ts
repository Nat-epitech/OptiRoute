import api from "../axios";

import type {
    TractorSummary,
    TractorDetails,
    CreateTractorRequest,
    UpdateTractorRequest,
} from "@/models/vehicle/Tractor";

export const getTractors = async (): Promise<TractorSummary[]> => {
    const response = await api.get<TractorSummary[]>("/tractors");
    return response.data;
};

export const getTractor = async (id: number): Promise<TractorDetails> => {
    const response = await api.get<TractorDetails>(`/tractors/${id}`);
    return response.data;
};

export const createTractor = async (tractor: CreateTractorRequest): Promise<TractorSummary> => {
    const response = await api.post<TractorSummary>("/tractors", tractor);
    return response.data;
};

export const updateTractor = async (id: number, tractor: UpdateTractorRequest): Promise<TractorSummary> => {
    const response = await api.put<TractorSummary>(`/tractors/${id}`, tractor);
    return response.data;
};

export const deleteTractor = async (id: number): Promise<void> => {
    await api.delete(`/tractors/${id}`);
};