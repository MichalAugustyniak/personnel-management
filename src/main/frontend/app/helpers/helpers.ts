import type {Address, AddressApi, AddressCreationRequest, AddressesRequest} from "~/api/address-api";
import {trimOrThrow, trimString} from "~/util/utils";

export const mapToCreationRequest = (request: AddressesRequest): AddressCreationRequest=> {
    return {
        countryIsoCode: trimOrThrow(request.countryIsoCode, new Error("Null countryIsoCode")),
        postalCode: trimOrThrow(request.postalCode, new Error("Null postalCode")),
        region: trimString(request.region),
        city: trimOrThrow(request.city, new Error("Null city")),
        streetName: trimOrThrow(request.streetNumber, new Error("Null streetName")),
        streetNumber: trimOrThrow(request.streetNumber, new Error("Null streetNumber")),
        houseNumber: trimString(request.houseNumber),
        apartmentNumber: trimString(request.apartmentNumber)
    };
};

export const createAddress = async (request: AddressCreationRequest, addressApi: AddressApi) => {
    const response = await addressApi.createAddress(request);
    if (response.raw.status !== 201) {
        throw new Error("Something went wrong while creating the address");
    }
    return response.body;
}

export async function getAddressUUID(request: AddressesRequest, addressApi: AddressApi): Promise<string> {
    let addressResponse = await addressApi.getAddresses(request);
    if (!addressResponse.raw.ok) {
        throw new Error("Something went while while fetching the address");
    }
    if (addressResponse.body.content.length > 1) {
        throw new Error(`Duplicated addresses (${addressResponse.body.totalElements})`);
    }
    let addressUUID: string | undefined;
    if (addressResponse.body.content.length === 0) {
        const primaryAddressCreationResponse = await createAddress(mapToCreationRequest(request), addressApi);
        addressUUID = primaryAddressCreationResponse.addressUUID;
    } else {
        addressUUID = addressResponse.body.content.at(0)?.uuid;
    }
    return addressUUID!;
}

export async function fetchAddress(uuid: string, addressApi: AddressApi): Promise<Address> {
    const response = await addressApi.getAddress({uuid: uuid});
    if (!response.raw.ok) {
        throw new Error("Something went wrong while fetching the user");
    }
    if (!response.body) {
        throw new Error("Fetched user is undefined");
    }
    return response.body;
}
