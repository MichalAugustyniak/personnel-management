import type {User} from "~/api/user-api";
import React, {type SetStateAction, useContext, useEffect, useState} from "react";
import type {Address, AddressApi} from "~/api/address-api";
import {AddressApiContext} from "~/context/context";

export interface Props {
    user: User;
}

export default function UserView({ user }: Props) {
    const addressApi: AddressApi = useContext(AddressApiContext);
    const [primaryAddress, setPrimaryAddress] = useState<Address | undefined>(undefined);
    const [additionalAddress, setAdditionalAddress] = useState<Address | undefined>(undefined);

    useEffect(() => {
        const fetchAddress = async (uuid: string, setter: React.Dispatch<SetStateAction<Address | undefined>>) => {
            let response = await addressApi.getAddress({ uuid: uuid });
            if (!response.raw.ok || !response.body) {
                throw new Error("Something went wrong while fetching the address");
            }
            console.log(response.body);
            setter(response.body);
        };
        fetchAddress(user.addressUUID, setPrimaryAddress);
        if (user.additionalAddressUUID) {
            fetchAddress(user.additionalAddressUUID, setAdditionalAddress);
        }
    }, []);

    return (
        <div className={"h-fit w-96 rounded bg-indigo-200 flex flex-col p-2 space-y-3"}>
            <div className={"h-full w-full space-y-2 flex flex-col"}>
                <p className={"font-bold"}>{`${user.firstName + " "}${user.middleName ? user.middleName + " " : ""}${user.lastName}`}</p>
                <p>UUID: {user.uuid}</p>
                <p>Username: {user.username}</p>
                <p>Role: {user.role}</p>
                <p>Created at: {new Date(user.createdAt).toLocaleString()}</p>
                <p>Last login at: {user.lastLoginAt ? new Date(user.lastLoginAt).toLocaleString() : "The user has not logged in yet"}</p>
                <p>Birthday: {new Date(user.birthday).toLocaleDateString()}</p>
                <p>Sex: {user.sex}</p>
                <p>Email: {user.email ? user.email : <span className={"text-red-500"}>Not set</span>}</p>
                <p>Phone: {user.phone ? user.phone : <span className={"text-red-500"}>Not set</span>}</p>
                {primaryAddress
                    ? <div className={"w-full h-fit rounded bg-indigo-300"}>
                        <p>{`${primaryAddress.streetName} ${primaryAddress.streetNumber} ${primaryAddress.houseNumber ? primaryAddress.houseNumber + " " : ""}${primaryAddress.apartmentNumber ? primaryAddress.apartmentNumber + " " : ""}`}</p>
                        <p>{`${primaryAddress.city} ${primaryAddress.postalCode}`}</p>
                        <p>{`${primaryAddress.region ? primaryAddress.region + " " : ""}${primaryAddress.countryIsoCode}`}</p>
                    </div>
                    : null
                }
                {additionalAddress
                    ? <div className={"w-full h-fit rounded bg-indigo-300"}>
                        <p>{`${additionalAddress.streetName} ${additionalAddress.streetNumber} ${additionalAddress.houseNumber ? additionalAddress.houseNumber + " " : ""}${additionalAddress.apartmentNumber ? additionalAddress.apartmentNumber + " " : ""}`}</p>
                        <p>{`${additionalAddress.city} ${additionalAddress.postalCode}`}</p>
                        <p>{`${additionalAddress.region ? additionalAddress.region + " " : ""}${additionalAddress.countryIsoCode}`}</p>
                    </div>
                    : null
                }
                <p>Status: {user.isActive ? "active" : "inactive"}</p>
            </div>
        </div>
    );
}
