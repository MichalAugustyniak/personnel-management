export interface LoginRequest {
    username: string;
    password: string;
}

export interface SessionResponse {
    username: string;
    roles: string[];
}

export interface LoginApi {
    login(request: LoginRequest): Promise<Response>;
    logout(): Promise<Response>;
    session(): Promise<Response>;
}

export class LoginApiV1 implements LoginApi {
    private readonly url: string;

    public constructor(address: string, isSecure: boolean) {
        this.url = (isSecure ? "https" : "http") + `://${address}`;
    }

    public async login(request: LoginRequest): Promise<Response> {
        console.log(`username=${request.username}\tpassword=${request.password}`);
        console.log(this.url.toString());
        return await fetch(`${this.url}/login`, {
            method: "POST",
            credentials: "include",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({username: request.username, password: request.password})
        });
    }

    public async logout(): Promise<Response> {
        return await fetch(`${this.url}/logout`, {
            credentials: "include"
        });
    }

    public async session(): Promise<Response> {
        return await fetch(`${this.url}/session`, {
            credentials: "include"
        });
    }
}
