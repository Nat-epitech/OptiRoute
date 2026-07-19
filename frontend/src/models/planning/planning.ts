export interface PlanningTransport {
    id: number;
    name: string;

    driverId: number;
    driverName: string;

    plannedStart: string;
    plannedEnd: string;

    originName: string;
    destinationName: string;

    estimatedTotalCost: number | null;
}

export interface PlanningRequest {
    startDate: string;
    endDate: string;
}

export interface PlanningDriver {
    id: number;
    name: string;
    totalCost: number;
    days: Record<string, PlanningTransport[]>;
}

export interface PlanningDay {
    key: string;
    label: string;
    date: Date;
}