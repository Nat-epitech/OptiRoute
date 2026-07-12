import { ref } from "vue";

import planningService from "@/api/planning.service";

import type {
    PlanningMission,
    PlanningRequest
} from "@/models/planning/planning";

export function usePlanning() {

    const loading = ref(false);
    const missions = ref<PlanningMission[]>([]);
    const error = ref<string | null>(null);

    async function loadPlanning(request: PlanningRequest) {
        try {
            loading.value = true;
            error.value = null;
            missions.value = await planningService.getPlanning(request);
        } catch (e) {
            console.error(e);
            error.value = "Impossible de charger le planning.";
        } finally {
            loading.value = false;
        }
    }

    return { loading, missions, error, loadPlanning };
}