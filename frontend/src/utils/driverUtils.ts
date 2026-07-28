import type { DriverFormData } from "@/models/driver/Driver"
import type { DriverDetails } from "@/models/driver/Driver"

export const createEmptyDriverForm = (): DriverFormData => ({
    firstName: null,
    lastName: null,

    phoneNumber: null,

    monthlyCost: null,
    monthlyWorkingHours: null,
})

export const getDriverName = (driver: DriverDetails | null): string => {
    if (!driver) {
        return "Détail du chauffeur"
    }

    const fullName = [driver.firstName, driver.lastName].filter(Boolean).join(" ")

    return fullName || "Chauffeur sans nom"
}

export const getHourlyCost = (driver: DriverDetails | null): number => {
    if (driver == null || driver.monthlyCost == null || driver.monthlyWorkingHours == null || driver.monthlyWorkingHours <= 0) {
        return -1
    }

    return (driver.monthlyCost / driver.monthlyWorkingHours)
}