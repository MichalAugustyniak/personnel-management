import {ApiBase, type ApiResponse} from "~/api/commons";

export interface Logo {
    propertyName: string;
    propertyValue: string;
}

export interface LogoApi {
    getLogo(): Promise<ApiResponse<Logo>>;
    uploadLogo(file: File): Promise<Response>;
    getHost(): string;
}

export class LogoApiV1 extends ApiBase implements LogoApi {
    async getLogo(): Promise<ApiResponse<Logo>> {
        const response = await fetch(`${this.url}/api/config/logo`);
        return {raw: response, body: await response.json() as Logo};
    }

    async uploadLogo(file: File): Promise<Response> {
        const formData = new FormData();
        formData.append("file", file);
        return await fetch(`${this.url}/api/config/upload-logo`, {
            method: "POST",
            credentials: "include",
            body: formData
        });
    }

    getHost(): string {
        return this.url;
    }
}