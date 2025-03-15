import {validate as isValidUUID} from 'uuid';
import {ApiBase, type ApiResponse, type Page} from "~/api/commons";
import type {SimplifiedUser, SimplifiedUsers} from "~/api/user-api";

export interface Task {
    uuid: string;
    name: string;
    description?: string;
    startDateTime: Date;
    endDateTime: Date;
    color: string;
    createdBy: string;
    taskEventUUID?: string;
    isCompleted: boolean
}

export type Tasks = Page<Task>;

export interface TaskCreationRequest {
    name: string;
    description?: string;
    startDateTime: Date;
    endDateTime: Date;
    color: string;
    taskEventUUID?: string;
    isCompleted: boolean;
}

export interface TaskCreationResponse {
    taskUUID: string;
}

export interface TaskPatchRequest {
    name?: string;
    description?: string;
    startDateTime?: Date;
    endDateTime?: Date;
    color?: string;
    taskEventUUID?: string;
    isCompleted?: boolean;
}

export interface TasksRequest {
    pageNumber?: number;
    pageSize?: number;
    nameLike?: string;
    createdBy?: string;
    user?: string;
    taskEventUUID?: string;
    from?: string;
    to?: string;
}

export interface TaskUsersResponse {
    users: SimplifiedUser[]
}

export interface TaskApi {
    getTask(uuid: string): Promise<ApiResponse<Task>>;

    getTasks(request: TasksRequest): Promise<ApiResponse<Tasks>>;

    createTask(request: TaskCreationRequest): Promise<ApiResponse<TaskCreationResponse>>;

    patchTask(request: TaskPatchRequest, taskUUID: string): Promise<Response>;

    deleteTask(uuid: string): Promise<Response>;

    assignUser(userUUID: string, taskUUID: string): Promise<Response>;

    dismissUser(userUUID: string, taskUUID: string): Promise<Response>;

    getUsersByTask(uuid: string): Promise<ApiResponse<TaskUsersResponse>>;
}

export class TaskApiV1 extends ApiBase implements TaskApi {

    public async getTask(uuid: string): Promise<ApiResponse<Task>> {

        const response = await fetch(`${this.url.toString()}/api/tasks/${uuid}`, {
            credentials: "include"
        });
        return {raw: response, body: await response.json() as Task};
    }

    public async getTasks(request: TasksRequest): Promise<ApiResponse<Tasks>> {
        let getTasksURL = new URL(`${this.url}/api/tasks`);
        if (request.pageNumber) {
            getTasksURL.searchParams.append("pageNumber", request.pageNumber.toString());
        }
        if (request.pageSize) {
            getTasksURL.searchParams.append("pageSize", request.pageSize.toString());
        }
        if (request.nameLike) {
            getTasksURL.searchParams.append("nameLike", request.nameLike);
        }
        if (request.createdBy) {
            getTasksURL.searchParams.append("createdBy", request.createdBy);
        }
        if (request.user) {
            getTasksURL.searchParams.append("user", request.user);
        }
        if (request.taskEventUUID) {
            getTasksURL.searchParams.append("taskEventUUID", request.taskEventUUID);
        }
        if (request.from) {
            getTasksURL.searchParams.append("from", request.from);
        }
        if (request.to) {
            getTasksURL.searchParams.append("to", request.to);
        }
        const response = await fetch(getTasksURL, {
            credentials: "include"
        });
        return {raw: response, body: await response.json() as Tasks};
    }

    public async createTask(request: TaskCreationRequest): Promise<ApiResponse<TaskCreationResponse>> {
        const response = await fetch(`${this.url}/api/tasks`, {
            method: "POST",
            credentials: "include",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(request)
        });
        return {raw: response, body: await response.json() as TaskCreationResponse};
    }

    public async patchTask(request: TaskPatchRequest, taskUUID: string): Promise<Response> {
        if (!isValidUUID(taskUUID)) {
            throw new Error("Invalid task uuid");
        }
        return await fetch(`${this.url}/api/tasks/${taskUUID}`, {
            method: "PATCH",
            credentials: "include",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(request)
        });
    }

    async deleteTask(uuid: string): Promise<Response> {
        return await fetch(`${this.url}/api/tasks/${uuid}`, {
            method: "DELETE",
            credentials: "include"
        });
    }

    async assignUser(userUUID: string, taskUUID: string): Promise<Response> {
        const request = {userUUID: userUUID};
        return await fetch(`${this.url}/api/tasks/${taskUUID}/users`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            credentials: "include",
            body: JSON.stringify(request)
        });
    }

    async dismissUser(userUUID: string, taskUUID: string): Promise<Response> {
        const request = {userUUID: userUUID};
        return await fetch(`${this.url}/api/tasks/${taskUUID}/users`, {
            method: "DELETE",
            credentials: "include",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(request)
        });
    }

    async getUsersByTask(uuid: string): Promise<ApiResponse<TaskUsersResponse>> {
        const response = await fetch(`${this.url}/api/tasks/${uuid}/users`, {
            credentials: "include"
        });
        return {raw: response, body: await response.json() as TaskUsersResponse};
    }
}
