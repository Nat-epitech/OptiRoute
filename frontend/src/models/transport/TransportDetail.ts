import type { TransportStatus } from "@/types/planningType"

export interface TransportDetail {
    id: number;
    name: string;
    status: TransportStatus;

    plannedStart: string;
    plannedEnd: string;

    actualStart: string | null;
    actualEnd: string | null;

    driverId: number | null;
    driverName: string | null;
    driverEmail: string | null;

    tractorId: number | null;
    tractorRegistration: string | null;
    tractorBrand: string | null;
    tractorModel: string | null;

    semiTrailerId: number | null;
    semiTrailerRegistration: string | null;
    semiTrailerBrand: string | null;
    semiTrailerModel: string | null;

    customerId: number | null;
    customerName: string | null;
    customerAddress: string | null;
    customerCity: string | null;

    originName: string;
    originAddress: string | null;
    originLat: number;
    originLng: number;

    destinationName: string;
    destinationAddress: string | null;
    destinationLat: number;
    destinationLng: number;

    distanceMeters: number | null;
    durationSeconds: number | null;

    polyline: string | null;

    estimatedFuelCost: number | null;
    estimatedTollCost: number | null;
    estimatedDriverCost: number | null;
    estimatedTotalCost: number | null;
}