import type { MissionDetail } from "@/models/planning/missionDetail";
import api from "./axios";


import type {
    PlanningMission,
    PlanningRequest,
} from "@/models/planning/planning";

export const getPlanning = async (request: PlanningRequest): Promise<PlanningMission[]> => {
    const response = await api.get<PlanningMission[]>(
        "/missions/planning",
        {
            params: request,
        }
    );

    return response.data;
};

export const getMissionById = async (id: number): Promise<MissionDetail> => {
    const response = await api.get<MissionDetail>(
        `/missions/${id}`
    );

    return response.data;
};