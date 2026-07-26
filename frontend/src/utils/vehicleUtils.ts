import type { FuelType, TrailerType } from "@/types/vehicleType"

const FUEL_TYPE_LABELS: Record<FuelType, string> = {
    DIESEL: "Diesel",
    B100: "B100",
    HVO100: "HVO100",
    ELECTRIC: "Électrique",
    LNG: "GNL",
    CNG: "GNC",
    HYBRID: "Hybride",
    OTHER: "Autre",
}

const TRAILER_TYPE_LABELS: Record<TrailerType, string> = {
    CURTAINSIDER: "Tautliner",
    BOX: "Fourgon",
    REFRIGERATED: "Frigorifique",
    FLATBED: "Plateau",
    TIPPER: "Benne",
    TANKER: "Citerne",
    CONTAINER_CHASSIS: "Porte-conteneur",
    OTHER: "Autre",
}

export const formatFuelType = (fuelType: FuelType | null | undefined,): string => {
    if (!fuelType) {
        return "Non renseigné"
    }

    return FUEL_TYPE_LABELS[fuelType]
}

export const formatTrailerType = (trailerType: TrailerType | null | undefined,): string => {
    if (!trailerType) {
        return "Non renseigné"
    }

    return TRAILER_TYPE_LABELS[trailerType]
}

export const formatVehicleLabel = (brand: string | null | undefined, model: string | null | undefined, emptyLabel = "Marque et modèle non renseignés",): string => {
    const parts = [brand, model].filter(
        (value): value is string => Boolean(value),
    )

    return parts.length > 0 ? parts.join(" ") : emptyLabel
}

export const formatConsumption = (value: number | null): string => {
    if (value === null) {
        return "Non renseignée"
    }

    return `${value.toLocaleString("fr-FR", { maximumFractionDigits: 2 })} L / 100 km`
}