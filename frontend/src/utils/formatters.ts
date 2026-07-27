export const formatNumber = (value: number | null, suffix = "", maximumFractionDigits = 2): string => {
    if (value === null) {
        return "Non renseigné"
    }

    return `${value.toLocaleString("fr-FR", { maximumFractionDigits })}${suffix}`
}

export const formatDistance = (value: number | null): string => {
    if (value === null) {
        return "Non renseignée";
    }

    return `${(value / 1000).toLocaleString("fr-FR", {
        minimumFractionDigits: 0,
        maximumFractionDigits: 1,
    })} km`;
}

export const formatCurrency = (value: number | null): string => {
    if (value === null) {
        return "Non renseigné"
    }

    return value.toLocaleString("fr-FR", {
        style: "currency",
        currency: "EUR",
        maximumFractionDigits: 2,
    })
}

export const formatDate = (value: string | null,): string => {
    if (!value) {
        return "Non renseignée"
    }

    const [year, month, day] = value.split("-").map(Number)

    if (!year || !month || !day) {
        return value
    }

    return new Intl.DateTimeFormat("fr-FR").format(
        new Date(year, month - 1, day),
    )
}

export const formatDateTime = (value: string | null | undefined,): string => {
    if (!value) {
        return "Non renseignée"
    }

    return new Intl.DateTimeFormat("fr-FR", {
        dateStyle: "medium",
        timeStyle: "short",
    }).format(new Date(value))
}

export const formatDuration = (value: number | null): string => {
    if (value === null) {
        return "Non renseignée";
    }

    const hours = Math.floor(value / 3600);
    const minutes = Math.round((value % 3600) / 60);

    if (hours === 0) {
        return `${minutes} min`;
    }

    if (minutes === 0) {
        return `${hours} h`;
    }

    return `${hours} h ${String(minutes).padStart(2, "0")}`;
}