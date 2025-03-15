import {type SubmitHandler, useForm} from "react-hook-form";
import {useContext} from "react";
import {TaskEventApiContext} from "~/context/context";
import type {TaskEventCreationRequest} from "~/api/task-event-api";

type FormData = {
    name: string;
    description: string;
    startDateTime: string;
    endDateTime: string;
};

export default function TaskEventCreationPage() {
    const taskEventApi = useContext(TaskEventApiContext);
    const { register, handleSubmit, setError, formState: { isSubmitting, errors, isSubmitSuccessful } } = useForm<FormData>();

    const onSubmit = async (formData: FormData) => {
        const request: TaskEventCreationRequest = {
            name: formData.name.trim(),
            description: formData.description ? formData.description : undefined,
            startDateTime: formData.startDateTime,
            endDateTime: formData.endDateTime
        };
        const response = await taskEventApi.createTaskEvent(request);
        if (response.raw.status !== 201) {
            setError("root", {
                message: "Something went wrong while creating the task event",
            });
            throw new Error("Something went wrong while creating the task event");
        }
    }

    return (
        <div className={"h-full w-full flex flex-col items-center"}>
            <div className={"h-full w-fit flex flex-col"}>
                <div className={"h-1/6 w-fit flex-col content-center"}>
                    <h1 className={"text-3xl"}>Create a new task event</h1>
                    <h2 className={"text-xl"}>Fill the form below and click <span className={"font-bold"}>Save</span> button to create a new task event</h2>
                </div>
                <div className={"h-5/6 w-full py-2"}>
                    <form className={"h-full w-full flex flex-col space-y-3"}
                          onSubmit={handleSubmit(onSubmit)}
                    >
                        <div className={`h-fit w-full flex flex-col rounded space-y-1`}>
                            <div className={""}>Name</div>
                            <input {...register("name", {
                                required: "Name is required",
                                validate: (value) => {
                                    if (value.length === 0) {
                                        return "Name is required";
                                    }
                                    return true;
                                }
                            })}
                                   className={`h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1`}/>
                            { errors.name && <div className={"text-red-500"}>{errors.name.message}</div> }
                        </div>
                        <div className={`h-fit w-full flex flex-col rounded space-y-1`}>
                            <div className={""}>Description</div>
                            <input {...register("description", {
                                required: false,
                                validate: (value) => {
                                    if (value.trim().length > 500) {
                                        return "Description must be shorter or equal 500 characters"
                                    }
                                },
                            })}
                                   className={`h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1`}/>
                            { errors.description && <div className={"text-red-500"}>{errors.description.message}</div> }
                        </div>
                        <div className={`h-fit w-full flex flex-col rounded space-y-1`}>
                            <div className={""}>Start</div>
                            <input {...register("startDateTime", {
                                required: "Start is required",
                                validate: (value) => {
                                    try {
                                        if (Date.parse(value) < Date.now()) {
                                            return "Start must be in the future"
                                        }
                                        return true;
                                    } catch (error) {
                                        return "Something went wrong while validating the start";
                                    }
                                },
                            })}
                                   type={"datetime-local"}
                                   className={`h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1`}/>
                            { errors.startDateTime && <div className={"text-red-500"}>{errors.startDateTime.message}</div> }
                        </div>
                        <div className={`h-fit w-full flex flex-col rounded space-y-1`}>
                            <div className={""}>End</div>
                            <input {...register("endDateTime", {
                                required: "End is required",
                                validate: (value) => {
                                    try {
                                        if (Date.parse(value) < Date.now()) {
                                            return "End must be in the future"
                                        }
                                        return true;
                                    } catch (error) {
                                        return "Something went wrong while validating the end";
                                    }
                                },
                            })}
                                   type={"datetime-local"}
                                   className={`h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1`}/>
                            { errors.endDateTime && <div className={"text-red-500"}>{errors.endDateTime.message}</div> }
                        </div>
                        <button type={"submit"}
                                className={`h-10 w-full bg-blue-600 text-center font-bold text-white rounded hover:bg-blue-700 ${isSubmitting && "bg-blue-700"} transition-all duration-200`}
                        >
                            Create
                        </button>
                        { errors.root && <div className={"text-red-500 text-center"}>{errors.root.message}</div> }
                        { isSubmitSuccessful && <div className={"text-green-500 text-center"}>Task event created successfully</div> }
                    </form>
                </div>
            </div>
        </div>
    );
}