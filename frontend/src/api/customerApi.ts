import api from "./axios";

import type { Customer, CustomerCreateRequest, CustomerUpdateRequest } from "@/models/Customer";

export const getCustomers = async (): Promise<Customer[]> => {
    const response = await api.get<Customer[]>("/customers");

    return response.data;
};

export const getCustomer = async (id: number): Promise<Customer> => {
    const response = await api.get<Customer>(`/customers/${id}`);

    return response.data;
};

export const createCustomer = async (customer: CustomerCreateRequest): Promise<Customer> => {
    const response = await api.post<Customer>("/customers", customer);

    return response.data;
};

export const updateCustomer = async (id: number, customer: CustomerUpdateRequest): Promise<Customer> => {
    const response = await api.put<Customer>(`/customers/${id}`, customer);

    return response.data;
};

export const deleteCustomer = async (id: number): Promise<void> => {
    await api.delete(`/customers/${id}`);
};