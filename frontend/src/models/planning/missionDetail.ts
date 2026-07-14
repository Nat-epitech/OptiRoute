export type MissionStatus =
    | "PLANNED"
    | "IN_PROGRESS"
    | "COMPLETED"
    | "CANCELLED";

export interface MissionDetail {
    id: number;
    name: string;
    status: MissionStatus;

    plannedStart: string;
    plannedEnd: string;

    actualStart: string | null;
    actualEnd: string | null;

    driverId: number | null;
    driverName: string | null;
    driverEmail: string | null;

    vehicleId: number | null;
    vehicleRegistration: string | null;
    vehicleBrand: string | null;
    vehicleModel: string | null;

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
    trafficDurationSeconds: number | null;

    polyline: string | null;

    estimatedFuelCost: number | null;
    estimatedTollCost: number | null;
    estimatedDriverCost: number | null;
    estimatedTotalCost: number | null;
}