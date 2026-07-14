export interface CustomerMetadata {
    contactName: string;
    deliveryInstructions: string;

    [key: string]: unknown;
}

export interface Customer {
    id: number;

    externalId: string | null;
    externalSource: string;

    name: string;
    code: string | null;

    address: string | null;
    city: string | null;
    country: string | null;

    metadata: CustomerMetadata | null;

    createdAt: string;
    updatedAt: string;
}

export interface CustomerCreateRequest {
    externalId: string | null;
    externalSource: string;

    name: string;
    code: string | null;

    address: string | null;
    city: string | null;
    country: string | null;

    metadata: CustomerMetadata | null;
}

export interface CustomerUpdateRequest {
    name: string;
    code: string | null;

    address: string | null;
    city: string | null;
    country: string | null;

    metadata: CustomerMetadata | null;
}