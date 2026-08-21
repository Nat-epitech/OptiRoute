import type { CostParameterFormData } from "@/models/cost/CostParameter"

export const createEmptyCostParameterForm = (): CostParameterFormData => ({
    category: "VEHICLE",
    label: "",
    value: 0,
    unit: "EUR_PER_TRIP",
    active: true,
    rule: null,
})