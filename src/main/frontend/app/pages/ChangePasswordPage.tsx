import {useCurrentSessionContext, UserApiContext} from "~/context/context";
import {useContext} from "react";
import {type SubmitHandler, useForm} from "react-hook-form";
import type {User, UserEditionRequest} from "~/api/user-api";

type FormData = {
    password: string;
    repeatedPassword: string;
}

export default function ChangePasswordPage() {
    const session = useCurrentSessionContext();
    const userApi = useContext(UserApiContext);
    const { register, handleSubmit, setError, formState: {errors, isSubmitSuccessful, isSubmitting} } = useForm<FormData>();

    const onSubmit: SubmitHandler<FormData> = async (data: FormData) => {
        if (data.password !== data.repeatedPassword) {
            const message = "Repeated password is different";
            setError("repeatedPassword", {
                message: message
            });
            throw new Error(message);
        }
        if (!session.session) {
            throw new Error("Session is undefined");
        }
        const username = session.session.username;
        const usersResponse = await userApi.getSimplifiedUsers({
            like: session.session.username,
            pageNumber: 0,
            pageSize: 50,
        });
        if (!usersResponse.raw.ok) {
            throw new Error("Something went wrong while fetching the user");
        }
        const user = usersResponse.body.users.find(u => u.username === username);
        if (!user) {
            throw new Error("User not found");
        }
        const request: UserEditionRequest = {
            password: data.password
        }
        const response = await userApi.updateUser(user.uuid, request);
        if (!response.ok) {
            const message = "Something went wrong while changing the password";
            setError("root", {
                message: message
            });
            throw new Error(message);
        }
    }

    return (
        <div className={"h-full w-full flex flex-col items-center"}>
            <div className={"h-full w-fit flex flex-col"}>
                <div className={"h-1/6 grow content-center"}>
                    <div className={"text-2xl"}>Change password</div>
                    <div className={"text-md"}>Type a new password, then repeat and click <span className={"font-bold"}>Save</span> button to submit changes</div>
                </div>
                <div className={"h-5/6"}>
                    <form className={"space-y-2"} onSubmit={handleSubmit(onSubmit)}>
                        <div className={`h-fit w-full flex flex-col rounded space-y-1`}>
                            <div>New password</div>
                            <input {...register("password", {
                                required: "New password is required",
                                validate: value => {
                                    if (value.trim().length < 4) {
                                        return "Password must have at least 4 characters";
                                    }
                                    return true;
                                },
                            })}
                                   type={"password"}
                                   className={`h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1`}/>
                            {errors.password && <div className={"text-red-500"}>{errors.password.message}</div>}
                        </div>
                        <div className={`h-fit w-full flex flex-col rounded space-y-1`}>
                            <div>Repeat</div>
                            <input {...register("repeatedPassword", {
                                required: "Password repeat is required"
                            })}
                                   type={"password"}
                                   className={`h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1`}/>
                            {errors.repeatedPassword && <div className={"text-red-500"}>{errors.repeatedPassword.message}</div>}
                        </div>
                        <button type={"submit"}
                                className={`h-10 w-full bg-blue-600 text-center font-bold text-white rounded hover:bg-blue-700 ${isSubmitting && "bg-blue-700"} transition-all duration-200`}
                        >
                            Save
                        </button>
                        { errors.root && <div className={"text-red-500 text-center"}>{errors.root.message}</div> }
                        { isSubmitSuccessful && <div className={"text-green-500 text-center"}>Password changed successfully</div> }
                    </form>
                </div>
            </div>
        </div>
    );
}