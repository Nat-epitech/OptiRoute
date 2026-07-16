export interface CreateMissionRequest {
    name: string
    driverId: number
    vehicleId?: number
    customerId?: number

    plannedStart: string
    plannedEnd: string

    originName: string
    originAddress: string
    originLat: number
    originLng: number

    destinationName: string
    destinationAddress: string
    destinationLat: number
    destinationLng: number
}