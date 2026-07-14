export interface Vehicle {
    id: number

    externalId: string | null
    externalSource: string

    registration: string

    brand: string | null
    model: string | null

    fuelType: string | null
    averageConsumption: number | null
    tankCapacity: number | null

    metadata: Record<string, unknown> | null
}

export interface CreateVehicleRequest {
    externalId: string | null
    externalSource: string

    registration: string

    brand: string | null
    model: string | null

    fuelType: string | null
    averageConsumption: number | null
    tankCapacity: number | null

    metadata: Record<string, unknown> | null
}

export interface UpdateVehicleRequest {
    registration: string
    brand: string | null
    model: string | null

    fuelType: string | null
    averageConsumption: number | null
    tankCapacity: number | null
}