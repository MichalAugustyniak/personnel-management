import React, {type ChangeEvent, useContext, useEffect, useState} from "react";
import { addressApi, AddressApiContext, UserApiContext } from "~/context/context";
import type { Sex, UserApi, UserCreationRequest } from "~/api/user-api";
import type { AddressApi, AddressCreationRequest, AddressesRequest } from "~/api/address-api";

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
        throw new Error("Something went wrong while creating an user");
    }
    return response.body;
}

async function submit(
    userApi: UserApi,
    addressApi: AddressApi,
    form: UserCreationForm,
    messageStateSetter: React.Dispatch<React.SetStateAction<string | undefined>>,
    messageColorSetter: React.Dispatch<React.SetStateAction<string>>,
    userCountIncrementFunction: Function
) {
    try {
        let response = await createUser(userApi, addressApi, form);
        messageStateSetter("User created successfully");
        messageColorSetter(successfulTextColor);
        userCountIncrementFunction();
    } catch (error) {
        if (error instanceof Error) {
            messageStateSetter(error.message);
            messageColorSetter(errorTextColor);
        } else {
            messageStateSetter("Something went wrong");
            messageColorSetter(errorTextColor);
        }
    }
}

const errorTextColor = "text-red-500";
const successfulTextColor = "text-green-500";

interface UserCreationForm {
    firstName: string;
    middleName: string;
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

export default function UserCreationPage() {
    const userApi: UserApi = useContext(UserApiContext);
    const addressApi: AddressApi = useContext(AddressApiContext);

    // States for form fields
    const [firstName, setFirstName] = useState<string | undefined>(undefined);
    const [middleName, setMiddleName] = useState<string | undefined>(undefined);
    const [lastName, setLastName] = useState<string | undefined>(undefined);
    const [sex, setSex] = useState<Sex>("MALE");
    const [birthday, setBirthday] = useState<string | undefined>(undefined);
    const [pesel, setPesel] = useState<string | undefined>(undefined);
    const [email, setEmail] = useState<string | undefined>(undefined);
    const [phone, setPhone] = useState<string | undefined>(undefined);
    const [username, setUsername] = useState<string | undefined>(undefined);
    const [password, setPassword] = useState<string | undefined>(undefined);
    const [country, setCountry] = useState<string>("PL");
    const [postal, setPostal] = useState<string | undefined>(undefined);
    const [city, setCity] = useState<string | undefined>(undefined);
    const [street, setStreet] = useState<string | undefined>(undefined);
    const [streetNumber, setStreetNumber] = useState<string | undefined>(undefined);
    const [houseNumber, setHouseNumber] = useState<string | undefined>(undefined);
    const [role, setRole] = useState<string>("Admin");
    const [isActive, setActive] = useState<string>("1");
    const [apartmentNumber, setApartmentNumber] = useState<string | undefined>(undefined);
    const [message, setMessage] = useState<string | undefined>(undefined);
    const [messageColor, setMessageColor] = useState<string>(successfulTextColor);
    const [canSubmit, setCanSubmit] = useState<boolean>(false);
    const [createdUserCount, setCreatedUserCount] = useState<number>(0);

    useEffect(() => {
        let passwordOk = password !== undefined && password.length >= 4;
        let birthdayOk = birthday !== undefined && new Date(birthday) < new Date(Date.now());
        setCanSubmit(passwordOk && birthdayOk);
    }, [password, birthday]);

    const handleSubmit = () => {
        const form: UserCreationForm = {
            firstName: firstName!,
            middleName: middleName!,
            lastName: lastName!,
            sex: sex!,
            birthday: birthday!,
            email: email,
            phone: phone,
            username: username,
            password: password!,
            address: {
                postalCode: postal!,
                countryIsoCode: country!,
                region: undefined,
                city: city!,
                streetName: street!,
                streetNumber: streetNumber!,
                houseNumber: houseNumber,
                apartmentNumber: apartmentNumber,
            },
            role: role!,
            isActive: isActive === '1',
        };
        submit(userApi, addressApi, form, setMessage, setMessageColor, incrementCreatedUserCounter);
    };

    const incrementCreatedUserCounter = () => {
        console.log("incrementing");
        setCreatedUserCount(createdUserCount.valueOf() + 1);
    }

    useEffect(() => {
        console.log("próba resetu");
        if (messageColor == successfulTextColor) {
            resetForm();
            console.log("zresetowane");
        }
    }, [createdUserCount]);

    const resetForm = () => {
        setFirstName(undefined);
        setMiddleName(undefined);
        setLastName(undefined);
        setSex("MALE");
        setBirthday(undefined);
        setPesel(undefined);
        setEmail(undefined);
        setPhone(undefined);
        setUsername(undefined);
        setPassword(undefined);
        setCountry("PL");
        setPostal(undefined);
        setCity(undefined);
        setStreet(undefined);
        setStreetNumber(undefined);
        setApartmentNumber(undefined);
        setHouseNumber(undefined);
        setRole("Admin");
        setActive("1");
        setMessage(undefined);
    }

    return (
        <div className={"w-full h-full flex justify-center my-3"}>
            <div className={"h-fit w-fit rounded-2xl bg-gray-200 border-2 border-gray-300"}>
                <div className={"h-fit w-fit m-5 flex flex-col space-y-3"}>
                    <div className={"h-fit w-full flex justify-center items-center"}>
                        <h1 className={"text-xl"}>Create new user</h1>
                    </div>
                    <input defaultValue={firstName} placeholder={"first name"} className={"rounded px-1"} onChange={(e) => setFirstName(e.target.value)} />
                    <input defaultValue={middleName} placeholder={"middle name"} className={"rounded px-1"} onChange={(e) => setMiddleName(e.target.value)} />
                    <input defaultValue={lastName} placeholder={"last name"} className={"rounded px-1"} onChange={(e) => setLastName(e.target.value)} />
                    <select className={"rounded"} defaultValue={sex} onChange={(e) => setSex(e.target.value ? "FEMALE" : "MALE")}>
                        <option value={0}>male</option>
                        <option value={1}>female</option>
                    </select>
                    <div className={"space-y-1"}>
                        <div>Birthday</div>
                        <input defaultValue={undefined} type={"date"} className={"rounded px-1"}
                               onChange={(e) => setBirthday(e.target.value)}/>
                    </div>
                    <input defaultValue={email} type={"email"} placeholder={"email"} className={"rounded px-1"} onChange={(e) => setEmail(e.target.value)} />
                    <input defaultValue={phone} placeholder={"phone number"} className={"rounded px-1"} onChange={(e) => setPhone(e.target.value)} />
                    <input defaultValue={username} placeholder={"username"} className={"rounded px-1"} onChange={(e) => setUsername(e.target.value === "" ? undefined : e.target.value)} />
                    <input defaultValue={password} placeholder={"password"} className={"rounded px-1"} onChange={(e) => setPassword(e.target.value)} />
                    <input defaultValue={country} placeholder={"country iso code"} className={"rounded px-1"}
                           onChange={(e) => setCountry(e.target.value)}/>
                    <input defaultValue={postal} placeholder={"postal"} className={"rounded px-1"}
                           onChange={(e) => setPostal(e.target.value)}/>
                    <input defaultValue={city} placeholder={"city"} className={"rounded px-1"} onChange={(e) => setCity(e.target.value)} />
                    <input defaultValue={street} placeholder={"street"} className={"rounded px-1"} onChange={(e) => setStreet(e.target.value)} />
                    <input defaultValue={streetNumber} placeholder={"street number"} className={"rounded px-1"} onChange={(e) => setStreetNumber(e.target.value)} />
                    <input defaultValue={houseNumber} placeholder={"house number"} className={"rounded px-1"} onChange={(e) => setHouseNumber(e.target.value)} />
                    <input defaultValue={apartmentNumber} placeholder={"apartment number"} className={"rounded px-1"} onChange={(e) => setApartmentNumber(e.target.value)} />
                    <select className={"rounded"} defaultValue={role} onChange={(e) => setRole(e.target.value)}>
                        <option value={"Admin"}>Admin</option>
                        <option value={"Employee"}>Employee</option>
                        <option value={"Manager"}>Manager</option>
                        <option value={"HR"}>HR</option>
                    </select>
                    <select className={"rounded"} defaultValue={isActive} onChange={(e) => setActive(e.target.value)}>
                        <option value={"1"}>active</option>
                        <option value={"0"}>inactive</option>
                    </select>
                    <button
                        className={"w-full h-10 rounded " + (!canSubmit  ? "bg-gray-400" : "bg-blue-400 hover:bg-blue-500")}
                        disabled={!canSubmit}
                        onClick={handleSubmit}
                    >
                        Create
                    </button>
                    {message && (
                        <div className={"mt-3 text-center " + messageColor}>{message}</div>
                    )}
                </div>
            </div>
        </div>
    );
}
