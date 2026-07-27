import { ref } from "vue";

import { getPlanning } from "@/api/planningApi";

import type { PlanningTransport, PlanningRequest } from "@/models/planning/planning";

export function usePlanning() {

    const loading = ref(false);
    const transports = ref<PlanningTransport[]>([]);
    const error = ref<string | null>(null);

    async function loadPlanning(request: PlanningRequest) {
        try {
            loading.value = true;
            error.value = null;
            transports.value = await getPlanning(request);
        } catch (e) {
            error.value = "Impossible de charger le planning.";
        } finally {
            loading.value = false;
        }
    }

    return { loading, transports, error, loadPlanning };
}

export function createPlanningGridStyle(dayCount: number) {
    return {
        gridTemplateColumns: `240px 120px repeat(${dayCount}, minmax(220px, 1fr))`,
        minWidth: `${240 + 120 + dayCount * 220}px`,
        width: '100%',
    }
}