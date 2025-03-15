import {useContext, useEffect, useState} from "react";
import {LoginApiContext, LogoApiContext} from "~/context/context";
import {type LoginApi, type SessionResponse} from "~/api/login-api";
import {type NavigateFunction, useNavigate} from "react-router";
import {useForm} from "react-hook-form";
import {DefaultRoles} from "~/commons/commons";

async function login(username: string, password: string, loginApi: LoginApi) {
    const response = await loginApi.login({username: username, password: password});
    if (!response.ok) {
        console.log(response.status);
        throw new Error("Response status not ok");
    }
}

async function session(loginApi: LoginApi, navigate: NavigateFunction) {
    const response = await loginApi.session();
    if (!response.ok) {
        throw new Error("Response status not ok");
    }
    const session = await response.json() as SessionResponse;
    const roles = session.roles;
    if (roles.includes(DefaultRoles.MANAGER)) {
        navigate("/manager");
    }
    if (roles.includes(DefaultRoles.ADMIN)) {
        console.log("roles includes admin");
        navigate("/admin");
    }
    if (roles.includes(DefaultRoles.EMPLOYEE)) {
        navigate("/employee");
    }
    if (roles.includes(DefaultRoles.HR)) {
        navigate("/hr");
    }
}

async function submit(username: string, password: string, loginApi: LoginApi, navigate: NavigateFunction) {
    const loginResponse = await login(username, password, loginApi);
    const sessionResponse = await session(loginApi, navigate);
}

type FormData = {
    username: string;
    password: string;
}

export default function LoginPage() {
    const loginApi = useContext(LoginApiContext);
    const navigate = useNavigate();
    const { register, handleSubmit, setError, formState: { errors, isSubmitting } } = useForm<FormData>();

    const onSubmit = async (formData: FormData) => {
        try {
            const loginResponse = await login(formData.username, formData.password, loginApi);
            const sessionResponse = await session(loginApi, navigate);
        } catch (error) {
            setError("root", {
                message: "Login failed",
            });
        }
    };

    const logoApi = useContext(LogoApiContext);
    const [logo, setLogo] = useState("");

    const fetchLogo = async () => {
        const response = await logoApi.getLogo();
        if (!response.raw.ok) {
            throw new Error("Something went wrong while fetching the logo");
        }
        console.log(logoApi.getHost() + response.body.propertyValue);
        setLogo(logoApi.getHost() + response.body.propertyValue);
    }

    useEffect(() => {
        fetchLogo();
    }, []);

    return (
        <div className={"flex h-screen w-screen content-center justify-center items-center bg-gradient-to-br from-blue-300 to-purple-300"}>
            <div className={"h-1/2 2xl:h-1/3 w-1/4 2xl:w-1/5 rounded bg-white drop-shadow-lg flex justify-center"}>
                <form onSubmit={handleSubmit(onSubmit)}
                    className={"flex flex-col my-2 space-y-4 w-full"}>
                    <div className={"flex h-1/5 content-center justify-center "}>
                        <img src={logo} alt={"logo"} className={"h-full border-2 border-white rounded"}/>
                    </div>
                    <div className={"flex flex-col items-center space-y-5 h-3/5"}>
                        <h1 className={"font-bo text-xl"}>Login</h1>
                        <input {...register("username", {
                            required: "Username is required",
                            validate: (value) => {
                                if (value.length === 0) {
                                    return "Username cannot be empty";
                                }
                                return true;
                            }
                        })}
                               className={"h-10 w-4/5 border-b-2 border-gray-200 focus:border-0 focus:rounded focus:px-2 focus:bg-gray-200 transition-all outline-none duration-200 px-1"}
                               placeholder={"username"}/>
                        {errors.username
                            ? <div className={"text-red-500"}>{errors.username.message}</div>
                            : null}
                        <input {...register("password", {
                            required: "Password is required",
                            validate: (value) => {
                                if (value.length === 0) {
                                    return "Password cannot be empty";
                                }
                                return true;
                            }
                        })}
                               className={"h-10 w-4/5 border-b-2 border-gray-200 focus:border-0 focus:rounded focus:px-2 focus:bg-gray-200 transition-all outline-none duration-200 px-1"}
                               placeholder={"password"}
                               type={"password"}/>
                        { errors.password
                            ? <div className={"text-red-500"}>{errors.password.message}</div>
                        : null}
                    </div>
                    <div className={"h-1/5 w-full flex items-center justify-center"}>
                        <button type={"submit"}
                                disabled={isSubmitting}
                                className={`h-10 w-4/5 rounded text-center transition-all duration-200 bg-blue-300 hover:bg-blue-400 ${ isSubmitting ? "bg-blue-400" : null }`}>Login</button>
                    </div>
                    {errors.root && <div className={"text-red-500 text-center"}>{errors.root.message}</div>}
                </form>
            </div>
        </div>
    )
}
