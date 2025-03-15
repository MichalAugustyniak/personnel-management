import type {Sex, User, UserApi, UserEditionRequest} from "~/api/user-api";
import React, {useContext, useEffect, useState} from "react";
import {AddressApiContext, UserApiContext} from "~/context/context";
import {DefaultRoles} from "~/commons/commons";
import {type SubmitHandler, useForm} from "react-hook-form";
import type {AddressApi, AddressesRequest} from "~/api/address-api";
import {fetchAddress, getAddressUUID} from "~/helpers/helpers";

interface Props {
    uuid: string;
}

type Address = {
    countryIsoCode: string | undefined;
    postalCode: string | undefined;
    region: string | undefined;
    city: string | undefined;
    streetName: string | undefined;
    streetNumber: string | undefined;
    houseNumber: string | undefined;
    apartmentNumber: string | undefined;
};

type FormData = {
    isActive: string | undefined;
    role: string | undefined;
    sex: Sex;
    birthday: string | undefined;
    username: string | undefined;
    password: string | undefined;
    firstName: string | undefined;
    middleName: string | undefined;
    lastName: string | undefined;
    email: string | undefined;
    phone: string | undefined;
    primaryAddress: Address;
    additionalAddress: Address
};

const trimFormData = (form: FormData): FormData => {
    return {
        isActive: form.isActive ? form.isActive.trim() : undefined,
        role: form.role ? form.role.trim() : undefined,
        sex: form.sex,
        birthday: form.birthday ? form.birthday.trim() : undefined,
        username: form.username ? form.username.trim() : undefined,
        password: form.password ? form.password.trim() : undefined,
        firstName: form.firstName ? form.firstName.trim() : undefined,
        middleName: form.middleName ? form.middleName.trim() : undefined,
        lastName: form.lastName ? form.lastName.trim() : undefined,
        email: form.email ? form.email.trim() : undefined,
        phone: form.phone ? form.phone.trim() : undefined,
        primaryAddress: {
            countryIsoCode: form.primaryAddress.countryIsoCode ? form.primaryAddress.countryIsoCode.trim() : undefined,
            postalCode: form.primaryAddress.postalCode ? form.primaryAddress.postalCode.trim() : undefined,
            region: form.primaryAddress.region ? form.primaryAddress.region.trim() : undefined,
            city: form.primaryAddress.city ? form.primaryAddress.city.trim() : undefined,
            streetName: form.primaryAddress.streetName ? form.primaryAddress.streetName.trim() : undefined,
            streetNumber: form.primaryAddress.streetNumber ? form.primaryAddress.streetNumber.trim() : undefined,
            houseNumber: form.primaryAddress.houseNumber ? form.primaryAddress.houseNumber.trim() : undefined,
            apartmentNumber: form.primaryAddress.apartmentNumber ? form.primaryAddress.apartmentNumber.trim() : undefined,
        },
        additionalAddress: {
            countryIsoCode: form.additionalAddress.countryIsoCode ? form.additionalAddress.countryIsoCode.trim() : undefined,
            postalCode: form.additionalAddress.postalCode ? form.additionalAddress.postalCode.trim() : undefined,
            region: form.additionalAddress.region ? form.additionalAddress.region.trim() : undefined,
            city: form.additionalAddress.city ? form.additionalAddress.city.trim() : undefined,
            streetName: form.additionalAddress.streetName ? form.additionalAddress.streetName.trim() : undefined,
            streetNumber: form.additionalAddress.streetNumber ? form.additionalAddress.streetNumber.trim() : undefined,
            houseNumber: form.additionalAddress.houseNumber ? form.additionalAddress.houseNumber.trim() : undefined,
            apartmentNumber: form.additionalAddress.apartmentNumber ? form.additionalAddress.apartmentNumber.trim() : undefined,
        }
    }
};

const handleEmptyFields = (form: FormData): FormData => {
    return {
        isActive: form.isActive === "" ? undefined : form.isActive,
        role: form.role === "" ? undefined : form.role,
        sex: form.sex,
        birthday: form.birthday === "" ? undefined : form.birthday,
        username: form.username === "" ? undefined : form.username,
        password: form.password === "" ? undefined : form.password,
        firstName: form.firstName === "" ? undefined : form.firstName,
        middleName: form.middleName === "" ? undefined : form.middleName,
        lastName: form.lastName === "" ? undefined : form.lastName,
        email: form.email === "" ? undefined : form.email,
        phone: form.phone === "" ? undefined : form.phone,
        primaryAddress: {
            countryIsoCode: form.primaryAddress.countryIsoCode === "" ? undefined : form.primaryAddress.countryIsoCode,
            postalCode: form.primaryAddress.postalCode === "" ? undefined : form.primaryAddress.postalCode,
            region: form.primaryAddress.region === "" ? undefined : form.primaryAddress.region,
            city: form.primaryAddress.city === "" ? undefined : form.primaryAddress.city,
            streetName: form.primaryAddress.streetName === "" ? undefined : form.primaryAddress.streetName,
            streetNumber: form.primaryAddress.streetNumber === "" ? undefined : form.primaryAddress.streetNumber,
            houseNumber: form.primaryAddress.houseNumber === "" ? undefined : form.primaryAddress.houseNumber,
            apartmentNumber: form.primaryAddress.apartmentNumber === "" ? undefined : form.primaryAddress.apartmentNumber,
        },
        additionalAddress: {
            countryIsoCode: form.additionalAddress.countryIsoCode === "" ? undefined : form.additionalAddress.countryIsoCode,
            postalCode: form.additionalAddress.postalCode === "" ? undefined : form.additionalAddress.postalCode,
            region: form.additionalAddress.region === "" ? undefined : form.additionalAddress.region,
            city: form.additionalAddress.city === "" ? undefined : form.additionalAddress.city,
            streetName: form.additionalAddress.streetName === "" ? undefined : form.additionalAddress.streetName,
            streetNumber: form.additionalAddress.streetNumber === "" ? undefined : form.additionalAddress.streetNumber,
            houseNumber: form.additionalAddress.houseNumber === "" ? undefined : form.additionalAddress.houseNumber,
            apartmentNumber: form.additionalAddress.apartmentNumber === "" ? undefined : form.additionalAddress.apartmentNumber,
        }
    };
};

const processFormData = (form: FormData): FormData => {
    return handleEmptyFields(trimFormData(form));
};


export default function UserInfoEditor({uuid}: Props) {
    const userApi: UserApi = useContext(UserApiContext);
    const addressApi: AddressApi = useContext(AddressApiContext);
    const [primaryAddress, setPrimaryAddress] = useState<Address | undefined>(undefined);
    const [additionalAddress, setAdditionalAddress] = useState<Address | undefined>(undefined);
    const [isAdditionalAddressFetched, setIsAdditionalAddressFetched] = useState(false);
    const [user, setUser] = useState<User | undefined>(undefined);
    const { register, handleSubmit, formState: { errors, isSubmitting, isValid } } = useForm<FormData>();

    const onSubmit: SubmitHandler<FormData> = async (data) => {
        const processedFormData = processFormData(data);
        let primaryAddress = processedFormData.primaryAddress;
        let additionalAddress = processedFormData.additionalAddress;
        const primaryAddressRequest: AddressesRequest = {
            countryIsoCode: primaryAddress.countryIsoCode === "" ? undefined : primaryAddress.countryIsoCode,
            region: primaryAddress.region === "" ? undefined : primaryAddress.region,
            postalCode: primaryAddress.postalCode === "" ? undefined : primaryAddress.postalCode,
            city: primaryAddress.city === "" ? undefined : primaryAddress.city,
            streetName: primaryAddress.streetName === "" ? undefined : primaryAddress.streetName,
            streetNumber: primaryAddress.streetNumber === "" ? undefined : primaryAddress.streetNumber,
            houseNumber: primaryAddress.houseNumber === "" ? undefined : primaryAddress.houseNumber,
            apartmentNumber: primaryAddress.apartmentNumber === "" ? undefined : primaryAddress.apartmentNumber,
            pageSize: 2,
            pageNumber: 0
        };
        const additionalAddressRequest: AddressesRequest = {
            countryIsoCode: additionalAddress.countryIsoCode === "" ? undefined : additionalAddress.countryIsoCode,
            region: additionalAddress.region === "" ? undefined : additionalAddress.region,
            postalCode: additionalAddress.postalCode === "" ? undefined : additionalAddress.postalCode,
            city: additionalAddress.city === "" ? undefined : additionalAddress.city,
            streetName: additionalAddress.streetName === "" ? undefined : additionalAddress.streetName,
            streetNumber: additionalAddress.streetNumber === "" ? undefined : additionalAddress.streetNumber,
            houseNumber: additionalAddress.houseNumber === "" ? undefined : additionalAddress.houseNumber,
            apartmentNumber: additionalAddress.apartmentNumber === "" ? undefined : additionalAddress.apartmentNumber,
            pageSize: 2,
            pageNumber: 0
        };
        const primaryAddressUUID = await getAddressUUID(primaryAddressRequest, addressApi);
        let additionalAddressUUID: string | undefined = undefined;
        const isAdditionalAddressFromDataUndefined = Object.values(additionalAddress).every(value => value === undefined);
        if (!isAdditionalAddressFromDataUndefined) {
            additionalAddressUUID = await getAddressUUID(additionalAddressRequest, addressApi);
        }
        const userUpdateRequest: UserEditionRequest = {
            ...processedFormData,
            addressUUID: primaryAddressUUID,
            additionalAddressUUID: additionalAddressUUID,
            isActive: !!processedFormData.isActive
        };
        await userApi.updateUser(user?.uuid!, userUpdateRequest);
    };

    useEffect(() => {
        const fetchUser = async () => {
            let response = await userApi.getUser({userUUID: uuid});
            if (!response.raw.ok) {
                throw new Error("Something went wrong while fetching the user");
            }
            if (!response.body) {
                throw new Error("Fetched user is undefined");
            }
            setUser(response.body);
        }
        fetchUser();
    }, []);

    useEffect(() => {
        if (!user) {
            console.log("User is still not fetches");
            return;
        }
        console.log("User is fetched, now fetching addresses...");
        const setAddressState = async (uuid: string, setter: React.Dispatch<React.SetStateAction<Address | undefined>>) => {
            const fetchedAddress = await fetchAddress(uuid, addressApi);
            setter(fetchedAddress);
        };
        setAddressState(user.addressUUID, setPrimaryAddress);
        if (!user.additionalAddressUUID) {
            setIsAdditionalAddressFetched(true);
            return;
        }
        setAddressState(user.additionalAddressUUID, setAdditionalAddress);
        setIsAdditionalAddressFetched(true);
    }, [user]);

    return (
        <div className={"h-fit w-96 rounded bg-indigo-200 flex flex-col p-2 space-y-3"}>
            <div className={"h-full w-full"}>
                {user ?
                    <form className={"space-y-2 flex flex-col"} onSubmit={handleSubmit(onSubmit)}>
                        <div className={"h-fit w-full p-2 bg-indigo-300 rounded space-y-1"}>
                            <div className={"h-fit w-full"}>
                                First name
                            </div>
                            <input {...register("firstName")} placeholder={"first name"} defaultValue={user.firstName}
                                   className={"bg-indigo-100 rounded w-full px-1"}/>
                        </div>
                        <div className={"h-fit w-full p-2 bg-indigo-300 rounded space-y-1"}>
                            <div className={"h-fit w-full"}>
                                Middle name
                            </div>
                            <input {...register("middleName")} placeholder={"middle name"}
                                   defaultValue={user.middleName}
                                   className={"bg-indigo-100 rounded w-full px-1"}/>
                        </div>
                        <div className={"h-fit w-full p-2 bg-indigo-300 rounded space-y-1"}>
                            <div className={"h-fit w-full"}>
                                Last name
                            </div>
                            <input {...register("lastName")} placeholder={"last name"} defaultValue={user.lastName}
                                   className={"bg-indigo-100 rounded w-full px-1"}/>
                        </div>
                        <div className={"h-fit w-full p-2 bg-indigo-300 rounded space-y-1"}>
                            <div className={"h-fit w-full"}>
                                Username
                            </div>
                            <input {...register("username")} placeholder={"username"} defaultValue={user.username}
                                   className={"bg-indigo-100 rounded w-full px-1"}/>
                        </div>
                        <div className={"h-fit w-full p-2 bg-indigo-300 rounded space-y-1"}>
                            <div className={"h-fit w-full"}>
                                Set new password
                            </div>
                            <input {...register("password", {
                                validate: (value) => {
                                    if (value === "" || value === undefined) {
                                        return true;
                                    }
                                    if (value.trim().length < 4) {
                                        return "Password length must be at least 4 characters long";
                                    }
                                    return true;
                                }
                            })} placeholder={"password"}
                                   className={"bg-indigo-100 rounded w-full px-1"}/>
                            {errors.password &&
                                <p className={"text-red-500"}>{errors.password && errors.password.message}</p>}
                        </div>
                        <div className={"h-fit w-full p-2 bg-indigo-300 rounded space-y-1"}>
                            <div className={"h-fit w-full"}>
                                Role
                            </div>
                            <select {...register("role")} defaultValue={user.role}
                                    className={"bg-indigo-100 rounded w-full px-1"}>
                                <option value={DefaultRoles.ADMIN}>Admin</option>
                                <option value={DefaultRoles.EMPLOYEE}>Employee</option>
                                <option value={DefaultRoles.MANAGER}>Manager</option>
                                <option value={DefaultRoles.HR}>HR</option>
                            </select>
                        </div>
                        <div className={"h-fit w-full p-2 bg-indigo-300 rounded space-y-1"}>
                            <div className={"h-fit w-full"}>
                                Birthday
                            </div>
                            <input {...register("birthday")} type={"date"}
                                   className={"bg-indigo-100 rounded w-full px-1"}
                            defaultValue={user.birthday}/>
                        </div>
                        <div className={"h-fit w-full p-2 bg-indigo-300 rounded space-y-1"}>
                            <div className={"h-fit w-full"}>
                                Sex
                            </div>
                            <select {...register("sex")} defaultValue={user.sex}
                                    className={"bg-indigo-100 rounded w-full px-1"}>
                                <option value={"MALE"}>Male</option>
                                <option value={"FEMALE"}>Female</option>
                            </select>
                        </div>
                        <div className={"h-fit w-full p-2 bg-indigo-300 rounded space-y-1"}>
                            <div className={"h-fit w-full"}>
                                Email
                            </div>
                            <input {...register("email")} placeholder={"email"} defaultValue={user.email}
                                   className={"bg-indigo-100 rounded w-full px-1"}/>
                        </div>
                        <div className={"h-fit w-full p-2 bg-indigo-300 rounded space-y-1"}>
                            <div className={"h-fit w-full"}>
                                Phone
                            </div>
                            <input {...register("phone")} placeholder={"phone"} defaultValue={user.phone}
                                   className={"bg-indigo-100 rounded w-full px-1"}/>
                        </div>
                        {primaryAddress
                            ? <div className={"h-fit w-full bg-indigo-300 rounded flex-col space-y-1 p-2"}>
                                <div className={"h-fit w-full"}>
                                    Primary address
                                </div>
                                <input {...register("primaryAddress.countryIsoCode")} placeholder={"country iso code"}
                                       defaultValue={primaryAddress?.countryIsoCode!}
                                       className={"w-full bg-indigo-100 rounded"}/>
                                <input {...register("primaryAddress.postalCode")} placeholder={"postal code"}
                                       defaultValue={primaryAddress?.postalCode!}
                                       className={"w-full bg-indigo-100 rounded"}/>
                                <input {...register("primaryAddress.region")} placeholder={"region"}
                                       defaultValue={primaryAddress?.region!}
                                       className={"w-full bg-indigo-100 rounded"}/>
                                <input {...register("primaryAddress.city")} placeholder={"city"}
                                       defaultValue={primaryAddress?.city!}
                                       className={"w-full bg-indigo-100 rounded"}/>
                                <input {...register("primaryAddress.streetName")} placeholder={"street name"}
                                       defaultValue={primaryAddress?.streetName!}
                                       className={"w-full bg-indigo-100 rounded"}/>
                                <div className={"h-fit w-full flex flex-row"}>
                                    <div className={"w-1/3 pr-1"}>
                                        <input {...register("primaryAddress.streetNumber")} placeholder={"st. no."}
                                               defaultValue={primaryAddress?.streetNumber!}
                                               className={"bg-indigo-100 rounded w-full"}/>
                                    </div>
                                    <div className={"w-1/3 px-1"}>
                                        <input {...register("primaryAddress.houseNumber")} placeholder={"h. no."}
                                               defaultValue={primaryAddress?.houseNumber!}
                                               className={"bg-indigo-100 rounded w-full"}/>
                                    </div>
                                    <div className={"w-1/3 pl-1"}>
                                        <input {...register("primaryAddress.apartmentNumber")} placeholder={"ap. no."}
                                               defaultValue={primaryAddress?.apartmentNumber!}
                                               className={"bg-indigo-100 rounded w-full"}/>
                                    </div>
                                </div>
                            </div>
                            : null}
                        {isAdditionalAddressFetched
                            ? <div className={"h-fit w-full bg-indigo-300 rounded flex-col space-y-1 p-2"}>
                                <div className={"h-fit w-full"}>
                                    Additional address
                                </div>
                                <input {...register("additionalAddress.countryIsoCode")}
                                       placeholder={"country iso code"}
                                       defaultValue={additionalAddress?.countryIsoCode!}
                                       className={"w-full bg-indigo-100 rounded"}/>
                                <input {...register("additionalAddress.postalCode")} placeholder={"postal code"}
                                       defaultValue={additionalAddress?.postalCode!}
                                       className={"w-full bg-indigo-100 rounded"}/>
                                <input {...register("additionalAddress.region")} placeholder={"region"}
                                       defaultValue={additionalAddress?.region!}
                                       className={"w-full bg-indigo-100 rounded"}/>
                                <input {...register("additionalAddress.city")} placeholder={"city"}
                                       defaultValue={additionalAddress?.city!}
                                       className={"w-full bg-indigo-100 rounded"}/>
                                <input {...register("additionalAddress.streetName")} placeholder={"street name"}
                                       defaultValue={additionalAddress?.streetName!}
                                       className={"w-full bg-indigo-100 rounded"}/>
                                <div className={"h-fit w-full flex flex-row"}>
                                    <div className={"w-1/3 pr-1"}>
                                        <input {...register("additionalAddress.streetNumber")} placeholder={"st. no."}
                                               defaultValue={additionalAddress?.streetNumber!}
                                               className={"bg-indigo-100 rounded w-full"}/>
                                    </div>
                                    <div className={"w-1/3 px-1"}>
                                        <input {...register("additionalAddress.houseNumber")} placeholder={"h. no."}
                                               defaultValue={additionalAddress?.houseNumber!}
                                               className={"bg-indigo-100 rounded w-full"}/>
                                    </div>
                                    <div className={"w-1/3 pl-1"}>
                                        <input {...register("additionalAddress.apartmentNumber")}
                                               placeholder={"ap. no."}
                                               defaultValue={additionalAddress?.apartmentNumber!}
                                               className={"bg-indigo-100 rounded w-full"}/>
                                    </div>
                                </div>
                            </div>
                            : null}
                        <div className={"h-fit w-full p-2 bg-indigo-300 rounded space-y-1"}>
                            <div className={"h-fit w-full"}>
                                Status
                            </div>
                            <select {...register("isActive")} defaultValue={user.isActive ? 1 : 0}
                                    className={"bg-indigo-100 rounded w-full px-1"}>
                                <option value={1}>Active</option>
                                <option value={0}>Inactive</option>
                            </select>
                        </div>

                        <button type={"submit"}
                                disabled={isSubmitting && !isValid}
                                className={"h-10 w-full bg-indigo-400 rounded hover:bg-indigo-500 text-center"}>
                            Save
                        </button>
                    </form>
                    : null
                }
            </div>
        </div>
    );
}