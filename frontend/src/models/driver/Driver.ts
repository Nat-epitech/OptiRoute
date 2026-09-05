export interface DriverSummary {
    id: number
    firstName: string
    lastName: string
}

export interface DriverDetails extends DriverSummary {
    login: string
    phoneNumber: string

    annualSalary: number
    monthlyWorkingHours: number

    tractorId: number | null
    tractorRegistration: string | null
    semiTrailerId: number | null
    semiTrailerRegistration: string | null
    costType: DriverCostType
    driverType: DriverType
}

export type DriverCostType = "HOURLY" | "FIXED"
export type DriverType = "LONG_HAUL" | "SHORT_DISTANCE" | "REGIONAL"

export interface CreateDriverRequest {
    firstName: string | null
    lastName: string | null
    phoneNumber: string | null
    annualSalary: number | null
    monthlyWorkingHours: number | null
    tractorId: number | null
    semiTrailerId: number | null
    costType: DriverCostType | null
    driverType: DriverType | null
}

export interface UpdateDriverRequest {
    firstName: string | null
    lastName: string | null
    phoneNumber: string | null
    annualSalary: number | null
    monthlyWorkingHours: number | null
    tractorId: number | null
    semiTrailerId: number | null
    costType: DriverCostType | null
    driverType: DriverType | null
}

export interface DriverFormData {
    firstName: string | null
    lastName: string | null
    phoneNumber: string | null
    annualSalary: number | null
    monthlyWorkingHours: number | null
    tractorId: number | null
    semiTrailerId: number | null
    costType: DriverCostType | null
    driverType: DriverType | null
}