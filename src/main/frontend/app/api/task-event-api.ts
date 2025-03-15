import {ApiBase, type ApiResponse, type Page} from "~/api/commons";

export interface TaskEvent {
    uuid: string;
    name: string;
    description?: string;
    startDateTime: string;
    endDateTime: string;
    createdBy: string;
    createdAt: string;
};

export type TaskEvents = Page<TaskEvent>;

export interface TaskEventsRequest {
    like?: string;
    from?: string;
    to?: string;
    user?: string;
    createdBy?: string;
    pageNumber: number;
    pageSize: number;
}

export interface TaskEventCreationRequest {
    name: string;
    description?: string;
    startDateTime: string;
    endDateTime: string;
}

export interface TaskEventCreationResponse {
    taskEventUUID: string;
}

export interface TaskEventUpdateRequest {
    name?: string;
    description?: string;
    startDateTime?: string;
    endDateTime?: string;
}

export interface TaskEventApi {
    getTaskEvent(uuid: string): Promise<ApiResponse<TaskEvent>>;
    getTaskEvents(request: TaskEventsRequest): Promise<ApiResponse<TaskEvents>>;
    createTaskEvent(request: TaskEventCreationRequest): Promise<ApiResponse<string>>;
    updateTaskEvent(uuid: string, request: TaskEventUpdateRequest): Promise<Response>;
    deleteTaskEvent(uuid: string): Promise<Response>;
}


export class TaskEventApiV1 extends ApiBase implements TaskEventApi {
    public async getTaskEvent(uuid: string): Promise<ApiResponse<TaskEvent>> {
        const response = await fetch(`${this.url}/api/task-events/${uuid}`, {
            credentials: "include",
        });
        return { raw: response, body: await response.json() as TaskEvent };
    }

    public async getTaskEvents(request: TaskEventsRequest): Promise<ApiResponse<TaskEvents>> {
        const response = await fetch(this.requestUrl(request), {
            credentials: "include",
        });
        return { raw: response, body: await response.json() as TaskEvents };
    }

    public async createTaskEvent(request: TaskEventCreationRequest): Promise<ApiResponse<string>> {
        const response = await fetch(`${this.url}/api/task-events`, {
            method: "POST",
            credentials: "include",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(request),
        });
        return { raw: response, body: await  response.json() as string };
    }

    public async updateTaskEvent(uuid: string, request: TaskEventUpdateRequest): Promise<Response> {
        return await fetch(`${this.url}/api/task-events/${uuid}`, {
            method: "PATCH",
            credentials: "include",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(request),
        });
    }

    public async deleteTaskEvent(uuid: string): Promise<Response> {
        return await fetch(`${this.url}/api/task-events/${uuid}`, {
            method: "DELETE",
            credentials: "include",
        });
    }

    private requestUrl(request: TaskEventsRequest) {
        const requestUrl = new URL(`${this.url}/api/task-events`);
        if (request.user) {
            requestUrl.searchParams.append("user", request.user);
        }
        if (request.createdBy) {
            requestUrl.searchParams.append("createdBy", request.createdBy);
        }
        if (request.from) {
            requestUrl.searchParams.append("from", request.from);
        }
        if (request.to) {
            requestUrl.searchParams.append("to", request.to);
        }
        if (request.like) {
            requestUrl.searchParams.append("like", request.like);
        }
        if (request.pageNumber) {
            requestUrl.searchParams.append("pageNumber", request.pageNumber.toString());
        }
        if (request.pageSize) {
            requestUrl.searchParams.append("pageSize", request.pageSize.toString());
        }
        return requestUrl;
    }
}
