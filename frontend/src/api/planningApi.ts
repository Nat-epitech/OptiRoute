import type { TransportDetail } from "@/models/transport/TransportDetail";
import api from "./axios";


import type { PlanningTransport, PlanningRequest } from "@/models/planning/planning";

export const getPlanning = async (request: PlanningRequest): Promise<PlanningTransport[]> => {
    const response = await api.get<PlanningTransport[]>("/transports/planning",
        {
            params: request,
        }
    );

    return response.data;
};

export const getTransportById = async (id: number): Promise<TransportDetail> => {
    const response = await api.get<TransportDetail>(`/transports/${id}`);

    return response.data;
};