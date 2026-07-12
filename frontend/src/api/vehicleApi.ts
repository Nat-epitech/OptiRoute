import api from "./axios";

import type { Vehicle } from "@/models/Vehicle";

export const getVehicles = async (): Promise<Vehicle[]> => {
    const response = await api.get<Vehicle[]>("/vehicles");
    return response.data;
};

export const getVehicle = async (id: number): Promise<Vehicle> => {
    const response = await api.get<Vehicle>(`/vehicles/${id}`);
    return response.data;
};

export const createVehicle = async (
    vehicle: Partial<Vehicle>
): Promise<Vehicle> => {
    const response = await api.post<Vehicle>("/vehicles", vehicle);
    return response.data;
};

export const updateVehicle = async (
    id: number,
    vehicle: Partial<Vehicle>
): Promise<Vehicle> => {
    const response = await api.put<Vehicle>(`/vehicles/${id}`, vehicle);
    return response.data;
};

export const deleteVehicle = async (id: number): Promise<void> => {
    await api.delete(`/vehicles/${id}`);
};