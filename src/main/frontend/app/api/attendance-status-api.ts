import {ApiBase, type ApiResponse, type Page} from "~/api/commons";

export interface AttendanceStatus {
    uuid: string;
    name: string;
    description: string;
    isExcusable: boolean;
}

export type AttendanceStatuses = Page<AttendanceStatus>;

export interface AttendanceStatusCreationRequest {
    name: string;
    description?: string;
    isExcusable: "true" | "false";
}

export interface AttendanceStatusCreationResponse {
    attendanceStatusUUID: string;
}

export interface AttendanceStatusUpdateRequest {
    name?: string;
    description?: string;
}

export interface AttendanceStatusesRequest {
    pageNumber: number;
    pageSize: number;
}

export interface AttendanceStatusApi {
    getAttendanceStatus(uuid: string): Promise<ApiResponse<AttendanceStatus>>;
    getAttendanceStatuses(request: AttendanceStatusesRequest): Promise<ApiResponse<AttendanceStatuses>>;
    createAttendanceStatus(request: AttendanceStatusCreationRequest): Promise<ApiResponse<AttendanceStatusCreationResponse>>;
    updateAttendanceStatus(uuid: string, request: AttendanceStatusUpdateRequest): Promise<Response>;
    deleteAttendanceStatus(uuid: string): Promise<Response>;
}

export class AttendanceStatusApiV1 extends ApiBase implements AttendanceStatusApi {
    async getAttendanceStatus(uuid: string): Promise<ApiResponse<AttendanceStatus>> {
        const response = await fetch(`${this.url}/api/attendance-statuses/${uuid}`, {
            credentials: "include"
        });
        return {raw: response, body: await response.json() as AttendanceStatus};
    }

    async getAttendanceStatuses(request: AttendanceStatusesRequest): Promise<ApiResponse<AttendanceStatuses>> {
        const response = await fetch(this.requestUrl(request), {
            credentials: "include"
        });
        return {raw: response, body: await response.json() as AttendanceStatuses};
    }

    async createAttendanceStatus(request: AttendanceStatusCreationRequest): Promise<ApiResponse<AttendanceStatusCreationResponse>> {
        const response = await fetch(`${this.url}/api/attendance-statuses`, {
            method: "POST",
            credentials: "include",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(request)
        });
        return {raw: response, body: await response.json() as AttendanceStatusCreationResponse};
    }

    async updateAttendanceStatus(uuid: string, request: AttendanceStatusUpdateRequest): Promise<Response> {
        return await fetch(`${this.url}/api/attendance-statuses/${uuid}`, {
            method: "PATCH",
            credentials: "include",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(request)
        });
    }
    async deleteAttendanceStatus(uuid: string): Promise<Response> {
        return await fetch(`${this.url}/api/attendance-statuses/${uuid}`, {
            method: "DELETE",
            credentials: "include"
        });
    }

    private requestUrl(request: AttendanceStatusesRequest) {
        const url = new URL(`${this.url}/api/attendance-statuses`);
        url.searchParams.append("pageNumber", request.pageNumber.toString());
        url.searchParams.append("pageSize", request.pageSize.toString());
        return url;
    }
}