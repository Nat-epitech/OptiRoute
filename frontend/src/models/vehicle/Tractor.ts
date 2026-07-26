import type { FuelType } from "@/types/vehicleType"

export interface Tractor {
    id: number

    registration: string

    brand: string | null
    model: string | null
}

export interface TractorDetails extends Tractor {
    maxSpeed: number | null

    fuelType: FuelType | null
    averageConsumption: number | null

    emptyWeightKg: number | null
    grossCombinationWeightKg: number | null

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

export interface CreateTractorRequest {
    externalId: string | null
    externalSource: string

    registration: string

    brand: string | null
    model: string | null

    maxSpeed: number | null

    fuelType: FuelType | null
    averageConsumption: number | null

    emptyWeightKg: number | null
    grossCombinationWeightKg: number | null

    heightCm: number | null
    widthCm: number | null
    lengthCm: number | null

    axleCount: number | null

    purchaseCost: number | null

    depreciationStartDate: string | null
    depreciationEndDate: string | null

    active: boolean
}

export interface UpdateTractorRequest {
    registration: string

    brand: string | null
    model: string | null

    maxSpeed: number | null

    fuelType: FuelType | null
    averageConsumption: number | null

    emptyWeightKg: number | null
    grossCombinationWeightKg: number | null

    heightCm: number | null
    widthCm: number | null
    lengthCm: number | null

    axleCount: number | null

    purchaseCost: number | null

    depreciationStartDate: string | null
    depreciationEndDate: string | null

    active: boolean
}

export interface TractorFormData {
    registration: string
    brand: string | null
    model: string | null

    maxSpeed: number | null

    fuelType: FuelType | null
    averageConsumption: number | null

    emptyWeightKg: number | null
    grossCombinationWeightKg: number | null

    heightCm: number | null
    widthCm: number | null
    lengthCm: number | null

    axleCount: number | null

    purchaseCost: number | null
    depreciationStartDate: string | null
    depreciationEndDate: string | null

    active: boolean
}