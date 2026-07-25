export interface Driver {
    id: number
    email: string
    firstName: string
    lastName: string
    phoneNumber: string
    isActive: boolean

    //TMP
    monthlySalary: number
    monthlyWorkingHours: number
}

export interface DriverDetails extends Driver {
    createdAt: string
    updatedAt: string
}

export interface CreateDriverRequest {
    email: string
    firstName: string
    lastName: string
    phoneNumber: string
    monthlySalary: number
    monthlyWorkingHours: number
}

export interface UpdateDriverRequest {
    email: string
    firstName: string
    lastName: string
    phoneNumber: string
    monthlySalary: number
    monthlyWorkingHours: number
}