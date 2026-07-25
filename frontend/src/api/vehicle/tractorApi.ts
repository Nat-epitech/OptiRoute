import api from "../axios";

import type {
    Tractor,
    TractorDetails,
    CreateTractorRequest,
    UpdateTractorRequest,
} from "@/models/vehicle/Tractor";

export const getTractors = async (): Promise<Tractor[]> => {
    const response = await api.get<Tractor[]>("/tractors");
    return response.data;
};

export const getTractor = async (id: number): Promise<TractorDetails> => {
    const response = await api.get<TractorDetails>(`/tractors/${id}`);
    return response.data;
};

export const createTractor = async (
    tractor: CreateTractorRequest
): Promise<Tractor> => {
    const response = await api.post<Tractor>("/tractors", tractor);
    return response.data;
};

export const updateTractor = async (
    id: number,
    tractor: UpdateTractorRequest
): Promise<Tractor> => {
    const response = await api.put<Tractor>(`/tractors/${id}`, tractor);
    return response.data;
};

export const deleteTractor = async (id: number): Promise<void> => {
    await api.delete(`/tractors/${id}`);
};