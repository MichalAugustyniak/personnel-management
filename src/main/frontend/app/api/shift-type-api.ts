import {ApiBase, type ApiResponse, type Page} from "~/api/commons";

export interface ShiftType {
    uuid: string;
    name: string;
    description?: string;
    startTime: string;
    endTime: string;
}

export type ShiftTypes = Page<ShiftType>;

interface ShiftTypesRequest {
    pageNumber: number;
    pageSize: number;
}

interface ShiftTypeCreationRequest {
    name: string;
    description?: string;
    startTime: string;
    endTime: string;
}

interface ShiftTypeCreationResponse {
    shiftTypeUUID: string;
}


interface ShiftTypeUpdateRequest {
    name?: string;
    description?: string;
    startTime?: string;
    endTime?: string;
}

export interface ShiftTypeApi {
    getShiftType(uuid: string): Promise<ApiResponse<ShiftType>>;
    getShiftTypes(request: ShiftTypesRequest): Promise<ApiResponse<ShiftTypes>>;
    createShiftType(request: ShiftTypeCreationRequest): Promise<ApiResponse<ShiftTypeCreationResponse>>;
    updateShiftType(uuid: string, request: ShiftTypeUpdateRequest): Promise<Response>;
    deleteShiftType(uuid: string): Promise<Response>;
}

export class ShiftTypeApiV1 extends ApiBase implements ShiftTypeApi {
    async getShiftType(uuid: string): Promise<ApiResponse<ShiftType>> {
        const response = await fetch(`${this.url}/api/shift-types/${uuid}`, {
            credentials: "include",
            headers: {
                "Content-Type": "application/json",
            },
        });
        return { raw: response, body: await response.json() as ShiftType };
    }

    async getShiftTypes(request: ShiftTypesRequest): Promise<ApiResponse<ShiftTypes>> {
        const requestUrl = new URL(`${this.url}/api/shift-types`);
        requestUrl.searchParams.append("pageNumber", request.pageNumber.toString());
        requestUrl.searchParams.append("pageSize", request.pageSize.toString())
        const response = await fetch(requestUrl, {
            credentials: "include",
            headers: {
                "Content-Type": "application/json",
            },
        });
        return { raw: response, body: await response.json() as ShiftTypes };
    }

    async createShiftType(request: ShiftTypeCreationRequest): Promise<ApiResponse<ShiftTypeCreationResponse>> {
        const response = await fetch(`${this.url}/api/shift-types`, {
            method: "POST",
            credentials: "include",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(request),
        });
        return { raw: response, body: await response.json() as ShiftTypeCreationResponse };
    }
    async updateShiftType(uuid: string, request: ShiftTypeUpdateRequest): Promise<Response> {
        return await fetch(`${this.url}/api/shift-types/${uuid}`, {
            method: "PATCH",
            credentials: "include",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(request),
        });
    }

    async deleteShiftType(uuid: string): Promise<Response> {
        return await fetch(`${this.url}/api/shift-types/${uuid}`, {
            method: "DELETE",
            credentials: "include",
        });
    }
}