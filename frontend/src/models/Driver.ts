export interface Driver {
    id: number
    email: string
    firstName: string
    lastName: string
    phoneNumber: string
    isActive: boolean
    monthlySalary: number
    monthlyWorkingHours: number
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