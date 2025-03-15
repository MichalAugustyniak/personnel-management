import React, {type ChangeEvent, useContext, useEffect, useState} from "react";
import { addressApi, AddressApiContext, UserApiContext } from "~/context/context";
import type { Sex, UserApi, UserCreationRequest } from "~/api/user-api";
import type { AddressApi, AddressCreationRequest, AddressesRequest } from "~/api/address-api";
import {type SubmitHandler, useForm} from "react-hook-form";

async function getAddress(addressApi: AddressApi, request: AddressesRequest) {
    let response = await addressApi.getAddresses(request);
    if (response.body.totalElements > 1) {
        throw new Error(`Too many returned addresses (${response.body.totalElements})`);
    }
    if (!response.raw.ok) {
        throw new Error(`Something went wrong while fetching address. Response status ${response.raw.status}`);
    }
    if (response.body.totalElements === 0) {
        return undefined;
    }
    let address = response.body.content.at(0);
    if (address === undefined) {
        throw new Error("Undefined fetched address");
    }
    return address;
}

async function createAddress(addressApi: AddressApi, request: AddressCreationRequest) {
    let response = await addressApi.createAddress(request);
    if (!response.raw.ok) {
        throw new Error(`Something went wrong while creating address: Response status ${response.raw.status}`);
    }
    return response.body;
}

async function handleAddress(addressApi: AddressApi, request: AddressCreationRequest) {
    let addressResponse = await getAddress(addressApi, request as AddressesRequest);
    if (addressResponse === undefined) {
        let creationResponse = await createAddress(addressApi, request);
        return creationResponse.addressUUID;
    }
    return addressResponse.uuid;
}

async function createUser(userApi: UserApi, addressApi: AddressApi, form: UserCreationForm) {
    let uuid: string = await handleAddress(addressApi, form.address);
    console.log(uuid);
    let response = await userApi.createUser({
        firstName: form.firstName,
        middleName: form.middleName,
        lastName: form.lastName,
        role: form.role,
        birthday: form.birthday,
        username: form.username,
        password: form.password,
        isActive: form.isActive,
        sex: form.sex,
        addressUUID: uuid,
        additionalAddressUUID: undefined,
        email: form.email,
        phone: form.phone
    });
    if (response.raw.status !== 201) {
        console.log(response.raw.status);
        console.log(form);
        throw new Error("Something went wrong while creating a new user");
    }
    return response.body;
}

type FormData = {
    firstName: string,
    middleName: string,
    lastName: string,
    sex: Sex,
    birthday: string,
    email: string,
    phone: string,
    username: string,
    password: string,
    address: {
        postalCode: string,
        countryIsoCode: string,
        region: undefined,
        city: string,
        streetName: string,
        streetNumber: string,
        houseNumber: string,
        apartmentNumber: string,
    },
    role: string,
    isActive: 0 | 1,
}

type UserCreationForm = {
    firstName: string;
    middleName: string | undefined;
    lastName: string;
    sex: Sex;
    birthday: string;
    email: string | undefined;
    phone: string | undefined;
    username: string | undefined;
    password: string;
    address: AddressCreationRequest;
    role: string;
    isActive: boolean;
}

export default function UserCreationPageV2() {
    const userApi: UserApi = useContext(UserApiContext);
    const addressApi: AddressApi = useContext(AddressApiContext);
    const {register, handleSubmit, setError, formState: {errors, isSubmitSuccessful, isSubmitting}} = useForm<FormData>();

    const onSubmit: SubmitHandler<FormData> = async(data: FormData) => {
        const form: UserCreationForm = {
            firstName: data.firstName.trim(),
            middleName: data.middleName ? data.middleName.trim() : undefined,
            lastName: data.lastName.trim(),
            sex: data.sex,
            birthday: data.birthday,
            email: data.email ? data.email.trim() : undefined,
            phone: data.phone ? data.phone.trim() : undefined,
            username: data.username ? data.username : undefined,
            password: data.password.trim(),
            address: {
                postalCode: data.address.postalCode.trim(),
                countryIsoCode: data.address.countryIsoCode.trim(),
                region: undefined,
                city: data.address.city.trim(),
                streetName: data.address.streetName.trim(),
                streetNumber: data.address.streetNumber.trim(),
                houseNumber: data.address.houseNumber ? data.address.houseNumber.trim() : undefined,
                apartmentNumber: data.address.apartmentNumber ? data.address.apartmentNumber.trim() : undefined,
            },
            role: data.role,
            isActive: data.isActive === 1,
        };
        //submit(userApi, addressApi, form);
        console.log(form);

        try {
            await createUser(userApi, addressApi, form);
        } catch (error) {
            if (error instanceof Error) {
                setError("root", {
                    message: error.message
                });
            }
        }

    };

    return (
        <div className={"h-full w-full flex flex-col items-center"}>
            <div className={"h-full w-fit flex flex-col"}>
                <div className={"h-1/6 w-fit flex-col content-center"}>
                    <h1 className={"text-3xl"}>Create a new user</h1>
                    <h2 className={"text-xl"}>Fill the form below and click <span
                        className={"font-bold"}>Create</span> button to create a new user</h2>
                </div>
                <div className={"h-5/6 w-full py-2"}>
                    <form className={"h-fit w-full flex flex-col space-y-3 pb-2"}
                          onSubmit={handleSubmit(onSubmit)}
                    >
                        <div className={`h-fit w-full flex flex-col rounded space-y-1`}>
                            <div className={""}>First name</div>
                            <input {...register("firstName", {
                                required: "First name is required",
                                validate: value => {
                                    if (value.trim().length > 30 || value.trim().length < 4) {
                                        return "First name must be between 4 and 30 characters long";
                                    }
                                    return true;
                                },
                            })}
                                   className={`h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1`}/>
                            {errors.firstName && <div className={"text-red-500"}>{errors.firstName.message}</div>}
                        </div>
                        <div className={`h-fit w-full flex flex-col rounded space-y-1`}>
                            <div className={""}>Middle name</div>
                            <input {...register("middleName", {
                                required: false,
                                validate: value => {
                                    if (value && (value.trim().length > 30 || value.trim().length < 4)) {
                                        return "Middle name must be between 4 and 30 characters long";
                                    }
                                    return true;
                                },
                            })}
                                   className={`h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1`}/>
                            {errors.middleName && <div className={"text-red-500"}>{errors.middleName.message}</div>}
                        </div>
                        <div className={`h-fit w-full flex flex-col rounded space-y-1`}>
                            <div className={""}>Last name</div>
                            <input {...register("lastName", {
                                required: "Last name is required",
                                validate: value => {
                                    if (value.trim().length > 30 || value.trim().length < 4) {
                                        return "Last name must be between 4 and 30 characters long";
                                    }
                                    return true;
                                },
                            })}
                                   className={`h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1`}/>
                            {errors.lastName && <div className={"text-red-500"}>{errors.lastName.message}</div>}
                        </div>

                        <div className={`h-fit w-full flex flex-col rounded space-y-1`}>
                            <div className={""}>Sex</div>
                            <select className={"h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1"} defaultValue={"MALE"}
                                    {...register("sex", {
                                        required: "Sex is required"
                                    })}>
                                <option value={"MALE"}>male</option>
                                <option value={"FEMALE"}>female</option>
                            </select>
                            {errors.sex && <div className={"text-red-500"}>{errors.sex.message}</div>}
                        </div>

                        <div className={`h-fit w-full flex flex-col rounded space-y-1`}>
                            <div>Birthday</div>
                            <input {...register("birthday", {
                                required: "Birthday is required"
                            })}
                                   type={"date"}
                                   className={"h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1"}/>
                            {errors.birthday && <div className={"text-red-500"}>{errors.birthday.message}</div>}
                        </div>

                        <div className={`h-fit w-full flex flex-col rounded space-y-1`}>
                            <div className={""}>Email</div>
                            <input {...register("email", {
                                required: false,
                                validate: value => {
                                    if (value && (value.trim().length > 30 || value.trim().length < 4)) {
                                        return "Email must be between 4 and 30 characters long if not empty";
                                    }
                                    return true;
                                },
                            })}
                                   className={`h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1`}/>
                            {errors.email && <div className={"text-red-500"}>{errors.email.message}</div>}
                        </div>

                        <div className={`h-fit w-full flex flex-col rounded space-y-1`}>
                            <div className={""}>Phone</div>
                            <input {...register("phone", {
                                required: false,
                                validate: value => {
                                    if (value && (value.trim().length > 15 || value.trim().length < 7)) {
                                        return "Phone must be between 7 and 15 characters long if not empty";
                                    }
                                    return true;
                                },
                            })}
                                   className={`h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1`}/>
                            {errors.phone && <div className={"text-red-500"}>{errors.phone.message}</div>}
                        </div>

                        <div className={`h-fit w-full flex flex-col rounded space-y-1`}>
                            <div className={""}>Username</div>
                            <input {...register("username", {
                                required: false,
                                validate: value => {
                                    if (value && (value.trim().length > 30 || value.trim().length < 4)) {
                                        return "Username must be between 4 and 30 characters long if not empty";
                                    }
                                    return true;
                                },
                            })}
                                   className={`h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1`}/>
                            {errors.username && <div className={"text-red-500"}>{errors.username.message}</div>}
                        </div>

                        <div className={`h-fit w-full flex flex-col rounded space-y-1`}>
                            <div className={""}>Password</div>
                            <input {...register("password", {
                                required: "Password is required",
                                validate: value => {
                                    if (value.trim().length > 30 || value.trim().length < 8) {
                                        return "Password must be between 8 and 30 characters long";
                                    }
                                    return true;
                                },
                            })}
                                   type={"password"}
                                   className={`h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1`}/>
                            {errors.password && <div className={"text-red-500"}>{errors.password.message}</div>}
                        </div>

                        <div className={`h-fit w-full flex flex-col rounded space-y-1`}>
                            <div className={""}>Country iso code</div>
                            <input {...register("address.countryIsoCode", {
                                required: "Country iso code is required",
                                validate: value => {
                                    if (value.trim().length !== 2) {
                                        return "Country iso code must 2 characters long";
                                    }
                                    return true;
                                },
                            })}
                                   className={`h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1`}/>
                            {errors.address?.countryIsoCode &&
                                <div className={"text-red-500"}>{errors.address.countryIsoCode.message}</div>}
                        </div>

                        <div className={`h-fit w-full flex flex-col rounded space-y-1`}>
                            <div className={""}>Postal code</div>
                            <input {...register("address.postalCode", {
                                required: "Postal code is required",
                                validate: value => {
                                    if (value.trim().length > 10 || value.trim().length < 4) {
                                        return "Postal code must be between 4 and 10 characters long";
                                    }
                                    return true;
                                },
                            })}
                                   className={`h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1`}/>
                            {errors.address?.postalCode &&
                                <div className={"text-red-500"}>{errors.address.postalCode.message}</div>}
                        </div>

                        <div className={`h-fit w-full flex flex-col rounded space-y-1`}>
                            <div className={""}>City</div>
                            <input {...register("address.city", {
                                required: "City is required",
                                validate: value => {
                                    if (value.trim().length > 30 || value.trim().length < 2) {
                                        return "City must be between 2 and 30 characters long";
                                    }
                                    return true;
                                },
                            })}
                                   className={`h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1`}/>
                            {errors.address?.city &&
                                <div className={"text-red-500"}>{errors.address.city.message}</div>}
                        </div>

                        <div className={`h-fit w-full flex flex-col rounded space-y-1`}>
                            <div className={""}>Street name</div>
                            <input {...register("address.streetName", {
                                required: "Street name is required",
                                validate: value => {
                                    if (value.trim().length > 30 || value.trim().length < 2) {
                                        return "Street name must be between 2 and 30 characters long";
                                    }
                                    return true;
                                },
                            })}
                                   className={`h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1`}/>
                            {errors.address?.streetName &&
                                <div className={"text-red-500"}>{errors.address.streetName.message}</div>}
                        </div>

                        <div className={`h-fit w-full flex flex-col rounded space-y-1`}>
                            <div className={""}>Street number</div>
                            <input {...register("address.streetNumber", {
                                required: "Street number is required",
                                validate: value => {
                                    if (value.trim().length > 5 || value.trim().length < 1) {
                                        return "Email must be between 1 and 5 characters long";
                                    }
                                    return true;
                                },
                            })}
                                   className={`h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1`}/>
                            {errors.address?.streetNumber &&
                                <div className={"text-red-500"}>{errors.address.streetNumber.message}</div>}
                        </div>

                        <div className={`h-fit w-full flex flex-col rounded space-y-1`}>
                            <div className={""}>House number</div>
                            <input {...register("address.houseNumber", {
                                required: false,
                                validate: value => {
                                    if (value && (value.trim().length > 5 || value.trim().length < 1)) {
                                        return "House number must be between 1 and 5 characters long if not empty";
                                    }
                                    return true;
                                },
                            })}
                                   className={`h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1`}/>
                            {errors.address?.houseNumber &&
                                <div className={"text-red-500"}>{errors.address.houseNumber.message}</div>}
                        </div>

                        <div className={`h-fit w-full flex flex-col rounded space-y-1`}>
                            <div className={""}>Apartment number</div>
                            <input {...register("address.apartmentNumber", {
                                required: false,
                                validate: value => {
                                    if (value && (value.trim().length > 5 || value.trim().length < 1)) {
                                        return "Apartment number must be between 1 and 5 characters long if not empty";
                                    }
                                    return true;
                                },
                            })}
                                   className={`h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1`}/>
                            {errors.address?.apartmentNumber &&
                                <div className={"text-red-500"}>{errors.address.apartmentNumber.message}</div>}
                        </div>

                        <div className={`h-fit w-full flex flex-col rounded space-y-1`}>
                            <div className={""}>Role</div>
                            <select
                                className={"h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1"}
                                defaultValue={"Admin"}
                                {...register("role", {
                                    required: "Role is required"
                                })}>
                                <option value={"Admin"}>Admin</option>
                                <option value={"Employee"}>Employee</option>
                                <option value={"Manager"}>Manager</option>
                                <option value={"HR"}>HR</option>
                            </select>
                        </div>

                        <div className={`h-fit w-full flex flex-col rounded space-y-1`}>
                            <div className={""}>Status</div>
                            <select
                                className={"h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1"}
                                defaultValue={"1"}
                                {...register("isActive", {
                                    required: "Status is required"
                                })}>
                                <option value={"1"}>active</option>
                                <option value={"0"}>inactive</option>
                            </select>
                        </div>

                        <button type={"submit"}
                                className={`h-10 w-full bg-blue-600 text-center font-bold text-white rounded hover:bg-blue-700 ${isSubmitting && "bg-blue-700"} transition-all duration-200`}
                                disabled={isSubmitting}
                        >
                            Create
                        </button>
                        {errors.root && <div className={"text-red-500 text-center"}>{errors.root.message}</div>}
                        {isSubmitSuccessful &&
                            <div className={"text-green-500 text-center"}>User created successfully</div>}
                    </form>
                </div>
            </div>
        </div>
    );
}
