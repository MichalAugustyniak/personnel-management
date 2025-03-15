import {ApiBase, type ApiResponse, type Page} from "~/api/commons";

export interface WorkBreakCreationRequest {
    startDateTime: string;
    endDateTime: string;
    isPaid: boolean;
}

export interface ScheduleDayCreationRequest {
    startDateTime: string;
    endDateTime: string;
    shiftTypeUUID: string;
    workBreaks: WorkBreakCreationRequest[];
}

export interface ScheduleCreationRequest {
    name: string;
    description?: string;
    maxWorkingHoursPerDay: number;
    enableHolidayAssignments: boolean;
    enableWorkingSaturdays: boolean;
    enableWorkingSundays: boolean;
    users: string[];
    scheduleDays: ScheduleDayCreationRequest[];
}

export interface ScheduleCreationResponse {
    scheduleUUID: string;
}

export interface SchedulesRequest {
    user?: string;
    isActive?: boolean;
    pageSize?: number;
    pageNumber?: number;
}

export interface AttachUsersToScheduleRequest {
    users: string[];
}

export interface DetachUsersFromScheduleRequest {
    users: string[];
}

export type Schedules = Page<Schedule>;

export interface WorkBreak {
    uuid: string;
    startDateTime: string;
    endDateTime: string;
    scheduleDayUUID: string;
    isPaid: boolean;
}

export interface ScheduleDay {
    uuid: string;
    scheduleUUID: string;
    startDateTime: string;
    endDateTime: string;
    shiftTypeUUID: string;
    workBreaks: WorkBreak[];
}

export interface Schedule {
    uuid: string;
    name: string;
    description?: string;
    maxWorkingHoursPerDay: number;
    enableHolidayAssignments: boolean;
    enableWorkingSaturdays: boolean;
    enableWorkingSundays: boolean;
    days: ScheduleDay[];
}

export interface ScheduleUpdateRequest {
    name?: string;
    description?: string;
    maxWorkingHoursPerDay?: number;
    enableHolidayAssignments?: boolean;
    enableWorkingSaturdays?: boolean;
    enableWorkingSundays?: boolean;
}

export interface ScheduleApi {
    getSchedule(uuid: string): Promise<ApiResponse<Schedule>>;
    getSchedules(request: SchedulesRequest): Promise<ApiResponse<Schedules>>;
    createSchedule(request: ScheduleCreationRequest): Promise<ApiResponse<ScheduleCreationResponse>>;
    updateSchedule(uuid: string, request: ScheduleUpdateRequest): Promise<Response>;
    attachUsersToSchedule(uuid: string, request: AttachUsersToScheduleRequest): Promise<Response>;
    detachUsersFromSchedule(uuid: string, request: AttachUsersToScheduleRequest): Promise<Response>;
    deleteSchedule(uuid: string): Promise<Response>;
}

export class ScheduleApiV1 extends ApiBase implements ScheduleApi {
    async getSchedule(uuid: string): Promise<ApiResponse<Schedule>> {
        const response = await fetch(`${this.url}/api/schedules/${uuid}`, {
            credentials: "include"
        });
        return {raw: response, body: await response.json() as Schedule};
    }

    async getSchedules(request: SchedulesRequest): Promise<ApiResponse<Schedules>> {
        const response = await fetch(this.requestUrl(request), {
            credentials: "include"
        });
        return {raw: response, body: await response.json() as Schedules};
    }

    async createSchedule(request: ScheduleCreationRequest): Promise<ApiResponse<ScheduleCreationResponse>> {
        const response = await fetch(`${this.url}/api/schedules`, {
            method: "POST",
            credentials: "include",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(request)
        });
        return {raw: response, body: await response.json() as ScheduleCreationResponse};
    }

    async updateSchedule(uuid: string, request: ScheduleUpdateRequest): Promise<Response> {
        return  await fetch(`${this.url}/api/schedules/${uuid}`, {
            method: "PATCH",
            credentials: "include",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(request)
        });
    }

    async attachUsersToSchedule(uuid: string, request: AttachUsersToScheduleRequest): Promise<Response> {
        return await fetch(`${this.url}/api/schedules/${uuid}/users`, {
            method: "POST",
            credentials: "include",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(request)
        });
    }

    async detachUsersFromSchedule(uuid: string, request: AttachUsersToScheduleRequest): Promise<Response> {
        return await fetch(`${this.url}/api/schedules/${uuid}/users`, {
            method: "DELETE",
            credentials: "include",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(request)
        });
    }

    async deleteSchedule(uuid: string): Promise<Response> {
        return await fetch(`${this.url}/api/schedules/${uuid}`, {
            method: "DELETE",
            credentials: "include"
        });
    }

    private requestUrl(request: SchedulesRequest) {
        const url = new URL(`${this.url}/api/schedules`);
        if (request.isActive) {
            url.searchParams.append("isActive", request.isActive ? "true" : "false");
        }
        if (request.user) {
            url.searchParams.append("user", request.user);
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
