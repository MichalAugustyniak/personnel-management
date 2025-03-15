import {ApiBase, type ApiResponse, type Page} from "~/api/commons";

export type Sex = "MALE" | "FEMALE";

export interface UserRequest {
    userUUID: string;
}

export interface User {
    uuid: string;
    isActive: boolean;
    lastLoginAt: string;
    createdAt: string;
    role: string;
    sex: Sex;
    birthday: string;
    username: string;
    firstName: string;
    middleName: string | undefined;
    lastName: string;
    addressUUID: string;
    additionalAddressUUID: string | undefined;
    email: string | undefined;
    phone: string | undefined;
}

export interface SimplifiedUser {
    uuid: string;
    username: string;
    lastLoginAt: string;
    firstName: string;
    middleName?: string;
    lastName: string;
}

export interface UsersRequest {
    role?: string | undefined;
    addressUUID?: string | undefined;
    sex?: Sex | undefined;
    isActive?: boolean | undefined;
    like?: string | undefined;
    pageNumber?: number | undefined;
    pageSize?: number | undefined;
}

//export type Users = Page<User>;

export interface Users {
    totalElements: number;
    totalPages: number;
    number: number;
    numberOfElements: number;
    size: number;
    users: User[];
}

//export type SimplifiedUsers = Page<SimplifiedUser>;

export interface SimplifiedUsers {
    totalElements: number;
    totalPages: number;
    number: number;
    numberOfElements: number;
    size: number;
    users: SimplifiedUser[];
}

export interface UserEditionRequest {
    isActive?: boolean | undefined;
    role?: string | undefined;
    sex?: Sex | undefined;
    birthday?: string | undefined;
    username?: string | undefined;
    password?: string | undefined;
    firstName?: string | undefined;
    middleName?: string | undefined;
    lastName?: string | undefined;
    addressUUID?: string | undefined;
    additionalAddressUUID?: string | undefined;
    email?: string | undefined;
    phone?: string | undefined;
}

export interface UserCreationRequest {
    firstName: string;
    middleName: string | undefined;
    lastName: string;
    role: string;
    birthday: string;
    username: string | undefined;
    password: string;
    isActive: boolean;
    sex: Sex;
    addressUUID: string;
    additionalAddressUUID: string | undefined;
    email: string | undefined;
    phone: string | undefined;
}

export interface UserCreationResponse {
    userUUID: string;
}

export interface UserApi {
    getUser(request: UserRequest): Promise<ApiResponse<User>>;
    getSimplifiedUser(request: UserRequest): Promise<ApiResponse<SimplifiedUser>>;
    getUsers(request: UsersRequest): Promise<ApiResponse<Users>>;
    getSimplifiedUsers(request: UsersRequest): Promise<ApiResponse<SimplifiedUsers>>;
    createUser(request: UserCreationRequest): Promise<ApiResponse<UserCreationResponse>>;
    updateUser(uuid: string, request: UserEditionRequest): Promise<Response>;
}

export class UserApiV1 extends ApiBase implements UserApi {
    public async getUser(request: UserRequest): Promise<ApiResponse<User>> {
        let response = await fetch(`${this.url}/api/users/${request.userUUID}?type=full`, {
            credentials: "include",
        });
        return {raw: response, body: await response.json() as User};
    }

    public async getSimplifiedUser(request: UserRequest): Promise<ApiResponse<SimplifiedUser>> {
        let response = await fetch(`${this.url}/api/users/${request.userUUID}`, {
            credentials: "include",
        });
        return { raw: response, body: await response.json() as SimplifiedUser };
    }

    public async getUsers(request: UsersRequest): Promise<ApiResponse<Users>> {
        let requestUrl = this.usersUrl(request);
        requestUrl.searchParams.append("type", "full");
        let response = await fetch(requestUrl, {
            credentials: "include",
        });
        return { raw: response, body: await response.json() as Users };
    }

    public async getSimplifiedUsers(request: UsersRequest): Promise<ApiResponse<SimplifiedUsers>> {
        let requestUrl = this.usersUrl(request);
        requestUrl.searchParams.append("type", "simplified");
        let response = await fetch(requestUrl, {
            credentials: "include",
        });
        return { raw: response, body: await response.json() as SimplifiedUsers };
    }

    public async createUser(request: UserCreationRequest): Promise<ApiResponse<UserCreationResponse>> {
        console.log("Creating an user...")
        let response = await fetch(`${this.url}/api/users`, {
            method: "POST",
            credentials: "include",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(request)
        });
        return { raw: response, body: await response.json() as UserCreationResponse };
    }

    public async updateUser(uuid: string, request: UserEditionRequest): Promise<Response> {
        return await fetch(`${this.url}/api/users/${uuid}`, {
            method: "PATCH",
            credentials: "include",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(request)
        });
    }

    private usersUrl(request: UsersRequest): URL {
        let requestUrl = new URL(`${this.url}/api/users`);
        if (request.addressUUID !== undefined) {
            requestUrl.searchParams.append("addressUUID", request.addressUUID);
        }
        if (request.like !== undefined) {
            requestUrl.searchParams.append("like", request.like);
        }
        if (request.sex !== undefined) {
            requestUrl.searchParams.append("sex", request.sex.toString());
        }
        if (request.isActive !== undefined) {
            requestUrl.searchParams.append("isActive", request.isActive ? "1" : "0");
        }
        if (request.role !== undefined) {
            requestUrl.searchParams.append("role", request.role);
        }
        if (request.pageNumber !== undefined) {
            requestUrl.searchParams.append("pageNumber", request.pageNumber.toString());
        }
        if (request.pageSize !== undefined) {
            requestUrl.searchParams.append("pageSize", request.pageSize.toString());
        }
        return requestUrl;
    }
}
