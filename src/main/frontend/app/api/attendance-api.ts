import {ApiBase, type ApiResponse, type Page} from "~/api/commons";
import type {AttendanceStatus} from "~/api/attendance-status-api";

export interface Attendance {
    uuid: string;
    user: string;
    scheduleDayUUID: string;
    startDateTime: string;
    endDateTime: string;
    attendanceStatus: AttendanceStatus;
}

export type Attendances = Page<Attendance>;

export interface AttendancesRequest {
    pageNumber: number;
    pageSize: number;
    user?: string;
    scheduleDayUUID?: string;
    absenceExcuseUUID?: string;
    attendanceStatusUUID?: string;
}

export interface AttendanceCreationRequest {
    startDateTime: string;
    endDateTime: string;
    user: string;
    attendanceStatusUUID: string;
    scheduleDayUUID: string;
}

export interface AttendanceCreationResponse {
    attendanceUUID: string;
}

export interface AttendanceUpdateRequest {
    startDateTime?: string;
    endDateTime?: string;
    attendanceStatus?: string;
}

export interface AttendanceApi {
    getAttendance(uuid: string): Promise<ApiResponse<Attendance>>;
    getAttendances(request: AttendancesRequest): Promise<ApiResponse<Attendances>>;
    createAttendance(request: AttendanceCreationRequest): Promise<ApiResponse<AttendanceCreationResponse>>;
    updateAttendance(uuid: string, request: AttendanceUpdateRequest): Promise<Response>;
    deleteAttendance(uuid: string): Promise<Response>;
}

export class AttendanceApiV1 extends ApiBase implements AttendanceApi {
    async getAttendance(uuid: string): Promise<ApiResponse<Attendance>> {
        const response = await fetch(`${this.url}/api/attendances/${uuid}`, {
            credentials: "include"
        });
        return {raw: response, body: await response.json() as Attendance};
    }

    async getAttendances(request: AttendancesRequest): Promise<ApiResponse<Attendances>> {
        const response = await fetch(this.requestUrl(request), {
            credentials: "include"
        });
        return {raw: response, body: await response.json() as Attendances};
    }

    async createAttendance(request: AttendanceCreationRequest): Promise<ApiResponse<AttendanceCreationResponse>> {
        const response = await fetch(`${this.url}/api/attendances`, {
            method: "POST",
            credentials: "include",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(request)
        });
        return {raw: response, body: await response.json() as AttendanceCreationResponse};
    }

    async updateAttendance(uuid: string, request: AttendanceUpdateRequest): Promise<Response> {
        return await fetch(`${this.url}/api/attendances/${uuid}`, {
            method: "PATCH",
            credentials: "include",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(request)
        });
    }

    async deleteAttendance(uuid: string): Promise<Response> {
        return await fetch(`${this.url}/api/attendances/${uuid}`, {
            method: "DELETE",
            credentials: "include"
        });
    }

    private requestUrl(request: AttendancesRequest) {
        const url = new URL(`${this.url}/api/attendances`);
        if (request.absenceExcuseUUID) {
            url.searchParams.append("absenceExcuseUUID", request.absenceExcuseUUID);
        }
        if (request.attendanceStatusUUID) {
            url.searchParams.append("attendanceStatusUUID", request.attendanceStatusUUID);
        }
        if (request.user) {
            url.searchParams.append("user", request.user);
        }
        if (request.scheduleDayUUID) {
            url.searchParams.append("scheduleDayUUID", request.scheduleDayUUID);
        }
        if (request.pageNumber) {
            url.searchParams.append("pageNumber", request.pageNumber.toString());
        }
        if (request.pageSize) {
            url.searchParams.append("pageSize", request.pageSize.toString());
        }
        return url;
    }
}
