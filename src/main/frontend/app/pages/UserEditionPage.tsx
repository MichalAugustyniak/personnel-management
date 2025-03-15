import type {UserApi} from "~/api/user-api";
import {useContext, useState} from "react";
import {UserApiContext} from "~/context/context";

function foo() {
    const userApi: UserApi = useContext(UserApiContext);
    const [firstName, setFirstName] = useState<string | undefined>(undefined);
    const [middleName, setMiddleName] = useState<string | undefined>(undefined);
    const [lastName, setLastName] = useState<string | undefined>(undefined);
    const [sex, setSex] = useState<string | undefined>(undefined);
    const [birthday, setBirthday] = useState<string | undefined>(undefined);
    const [pesel, setPesel] = useState<string | undefined>(undefined);
    const [email, setEmail] = useState<string | undefined>(undefined);
    const [phone, setPhone] = useState<string | undefined>(undefined);
    const [username, setUsername] = useState<string | undefined>(undefined);
    const [password, setPassword] = useState<string | undefined>(undefined);
    const [country, setCountry] = useState<string | undefined>(undefined);
    const [postal, setPostal] = useState<string | undefined>(undefined);
    const [city, setCity] = useState<string | undefined>(undefined);
    const [street, setStreet] = useState<string | undefined>(undefined);
    const [streetNumber, setStreetNumber] = useState<string | undefined>(undefined);
    const [homeNumber, setHomeNumber] = useState<string | undefined>(undefined);
    const [role, setRole] = useState<string | undefined>(undefined);
    const [isActive, setActive] = useState<string | undefined>(undefined);
}