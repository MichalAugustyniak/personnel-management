import {ApiBase, type ApiResponse, type Page} from "~/api/commons";


export interface Address {
    uuid: string;
    postalCode: string;
    countryIsoCode: string;
    region: string | undefined;
    city: string;
    streetName: string;
    streetNumber: string;
    houseNumber: string | undefined;
    apartmentNumber: string | undefined;
}

export interface AddressRequest {
    uuid: string;
}

export type Addresses = Page<Address>;

export interface AddressesRequest {
    postalCode: string | undefined;
    countryIsoCode: string | undefined;
    region: string | undefined;
    city: string | undefined;
    streetName: string | undefined;
    streetNumber: string | undefined;
    houseNumber: string | undefined;
    apartmentNumber: string | undefined;
    pageSize: number | undefined;
    pageNumber: number | undefined;
}

export interface AddressCreationRequest {
    postalCode: string;
    countryIsoCode: string;
    region: string | undefined;
    city: string;
    streetName: string;
    streetNumber: string;
    houseNumber: string | undefined;
    apartmentNumber: string | undefined;
}

export interface AddressCreationResponse {
    addressUUID: string;
}

export interface AddressApi {
    getAddress(request: AddressRequest): Promise<ApiResponse<Address>>;
    getAddresses(request: AddressesRequest): Promise<ApiResponse<Addresses>>;
    createAddress(request: AddressCreationRequest): Promise<ApiResponse<AddressCreationResponse>>;
}

export class AddressApiV1 extends ApiBase implements AddressApi {
    public async getAddress(request: AddressRequest): Promise<ApiResponse<Address>> {
        let response = await fetch(`${this.url}/api/addresses/${request.uuid}`, {
            credentials: "include",
        });
        return {raw: response, body: await response.json() as Address};
    }

    public async getAddresses(request: AddressesRequest): Promise<ApiResponse<Addresses>> {
        console.log("Fetching addresses...");
        let requestUrl = this.requestUrl(request);
        let response = await fetch(requestUrl, {
            credentials: "include",
        });
        return {raw: response, body: await response.json() as Addresses};
    }

    public async createAddress(request: AddressCreationRequest): Promise<ApiResponse<AddressCreationResponse>> {
        console.log("Saving address...");
        let response = await fetch(`${this.url}/api/addresses`, {
            method: "POST",
            credentials: "include",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(request)
        });
        return {raw: response, body: await response.json() as AddressCreationResponse};
    }

    private requestUrl(request: AddressesRequest): URL {
        let requestUrl = new URL(`${this.url}/api/addresses`);
        if (request.postalCode !== undefined) {
            requestUrl.searchParams.append("postalCode", request.postalCode);
        }
        if (request.countryIsoCode !== undefined) {
            requestUrl.searchParams.append("countryIsoCode", request.countryIsoCode);
        }
        if (request.region !== undefined) {
            requestUrl.searchParams.append("region", request.region);
        }
        if (request.city !== undefined) {
            requestUrl.searchParams.append("city", request.city);
        }
        if (request.streetName !== undefined) {
            requestUrl.searchParams.append("streetName", request.streetName);
        }
        if (request.streetNumber !== undefined) {
            requestUrl.searchParams.append("streetNumber", request.streetNumber);
        }
        if (request.houseNumber !== undefined) {
            requestUrl.searchParams.append("houseNumber", request.houseNumber);
        }
        if (request.apartmentNumber !== undefined) {
            requestUrl.searchParams.append("apartmentNumber", request.apartmentNumber);
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