export interface User {
    id: number
    email: string
    password: string
    firstName: string
    lastName: string
    createdAt: string
}

export interface UpdateUserRequest {
    email: string
    firstName: string
    lastName: string
    password?: string
}