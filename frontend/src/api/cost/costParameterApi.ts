import api from '../axios'
import type { CostParameter, CostParameterLight, CreateCostParameterRequest, UpdateCostParameterRequest } from '@/models/cost/CostParameter'

export const getCostParameters = async (): Promise<CostParameterLight[]> => {
    const response = await api.get<CostParameterLight[]>("/cost-parameters")
    return response.data
}

export const getCostParameter = (id: number) =>
api.get<CostParameter>(`/cost-parameters/${id}`)

export const createCostParameter = (request: CreateCostParameterRequest) =>
    api.post<CostParameter>("/cost-parameters", request)

export const updateCostParameter = (id: number, request: UpdateCostParameterRequest) =>
    api.put<CostParameter>(`/cost-parameters/${id}`, request)

export const deleteCostParameter = (id: number) =>
    api.delete(`/cost-parameters/${id}`)

export const setCostParameterActive = (id: number, active: boolean) =>
    api.patch<CostParameter>(`/cost-parameters/${id}/active`, null,
        {
            params: { active }
        }
    )