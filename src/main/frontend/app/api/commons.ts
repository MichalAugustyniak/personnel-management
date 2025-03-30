export interface Page<T> {
    totalElements: number;
    totalPages: number;
    number: number;
    numberOfElements: number;
    size: number;
    content: T[];
}

export abstract class ApiBase {
    public readonly url: string;

    /*
    public constructor(address: string, isSecure: boolean) {
        this.url = `${isSecure ? "https" : "http"}://${address}`;
    }

     */

    public constructor(url: string) {
        this.url = url;
    }
}

export interface ApiResponse<T> {
    raw: Response;
    body: T;
}