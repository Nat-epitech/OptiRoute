export type Waypoint = {
    id: string
    type: 'from' | 'to' | 'via'
    label: string
    query: string
    results: HereItem[]
    position: Position | null
}

export type HereItem = {
    id: string
    title: string
    position?: {
        lat: number
        lng: number
    }
}

export type Position = {
    lat: number
    lng: number
}