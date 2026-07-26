import type { TrailerType } from "@/types/vehicleType"

export interface SemiTrailerSummary {
    id: number

    registration: string

    brand: string | null
    model: string | null
}

export interface SemiTrailerDetails extends SemiTrailerSummary {
    trailerType: TrailerType | null

    maxSpeed: number | null

    emptyWeightKg: number | null
    grossVehicleWeightKg: number | null

    heightCm: number | null
    widthCm: number | null
    lengthCm: number | null

    axleCount: number | null

    purchaseCost: number | null

    depreciationStartDate: string | null
    depreciationEndDate: string | null

    active: boolean

    externalId: string | null
    externalSource: string

    createdAt: string
    updatedAt: string
}

export interface CreateSemiTrailerRequest {
    externalId: string | null
    externalSource: string

    registration: string

    brand: string | null
    model: string | null

    trailerType: TrailerType | null

    maxSpeed: number | null

    emptyWeightKg: number | null
    grossVehicleWeightKg: number | null

    heightCm: number | null
    widthCm: number | null
    lengthCm: number | null

    axleCount: number | null

    purchaseCost: number | null

    depreciationStartDate: string | null
    depreciationEndDate: string | null

    active: boolean
}

export interface UpdateSemiTrailerRequest {
    registration: string

    brand: string | null
    model: string | null

    trailerType: TrailerType | null

    maxSpeed: number | null

    emptyWeightKg: number | null
    grossVehicleWeightKg: number | null

    heightCm: number | null
    widthCm: number | null
    lengthCm: number | null

    axleCount: number | null

    purchaseCost: number | null

    depreciationStartDate: string | null
    depreciationEndDate: string | null

    active: boolean
}

export interface SemiTrailerFormData {
    registration: string
    brand: string | null
    model: string | null

    maxSpeed: number | null

    trailerType: TrailerType | null

    emptyWeightKg: number | null
    grossVehicleWeightKg: number | null

    heightCm: number | null
    widthCm: number | null
    lengthCm: number | null

    axleCount: number | null

    purchaseCost: number | null
    depreciationStartDate: string | null
    depreciationEndDate: string | null

    active: boolean
}