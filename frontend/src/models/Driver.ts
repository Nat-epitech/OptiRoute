export interface Driver {
    id: number
    email: string
    firstName: string
    lastName: string
    phoneNumber: string
    isActive: boolean
    createdAt: string
    updatedAt: string
}

export interface CreateDriverRequest {
    email: string
    firstName: string
    lastName: string
    phoneNumber: string
}

export interface UpdateDriverRequest {
    email: string
    firstName: string
    lastName: string
    phoneNumber: string
}