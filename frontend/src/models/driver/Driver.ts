export interface DriverSummary {
    id: number
    firstName: string
    lastName: string
}

export interface DriverDetails extends DriverSummary {
    login: string
    phoneNumber: string

    monthlyCost: number
    monthlyWorkingHours: number
}

export interface CreateDriverRequest {
    firstName: string | null
    lastName: string | null
    phoneNumber: string | null
    monthlyCost: number | null
    monthlyWorkingHours: number | null
}

export interface UpdateDriverRequest {
    firstName: string | null
    lastName: string | null
    phoneNumber: string | null
    monthlyCost: number | null
    monthlyWorkingHours: number | null
}

export interface DriverFormData {
    firstName: string | null
    lastName: string | null
    phoneNumber: string | null
    monthlyCost: number | null
    monthlyWorkingHours: number | null
}