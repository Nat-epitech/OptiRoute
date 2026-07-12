export type VehicleFuelType =
    | "DIESEL"
    | "PETROL"
    | "ELECTRIC"
    | "HYBRID"
    | "LNG"
    | "CNG"
    | "OTHER";

export interface Vehicle {
    id: number;

    externalId: string | null;
    externalSource: string;

    registration: string;

    brand: string | null;
    model: string | null;

    fuelType: VehicleFuelType | null;

    averageConsumption: number | null;
    tankCapacity: number | null;

    metadata: Record<string, unknown> | null;

    createdAt: string;
    updatedAt: string;
}