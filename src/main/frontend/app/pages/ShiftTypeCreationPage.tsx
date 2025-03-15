import {type SubmitHandler, useForm} from "react-hook-form";
import {useContext} from "react";
import {ShiftTypeContext} from "~/context/context";

type FormData = {
    name: string;
    description: string;
    start: string;
    end: string;
}

export default function ShiftTypeCreationPage() {
    const { register, handleSubmit, setError, formState: { isSubmitting, errors, isSubmitSuccessful } } = useForm<FormData>();
    const shiftTypeApi = useContext(ShiftTypeContext);

    const onSubmit: SubmitHandler<FormData> = async (formData: FormData) => {
        const response = await shiftTypeApi.createShiftType({
            name: formData.name.trim(),
            description: formData.description.trim(),
            startTime: formData.start,
            endTime: formData.end
        });
        if (response.raw.status !== 201) {
            const message = "Something went wrong while creating the shift type";
            setError("root", {
                message: message
            });
            throw new Error(message);
        }
    }

    return (
        <div className={"h-full w-full flex flex-col items-center"}>
            <form onSubmit={handleSubmit(onSubmit)}
                  className={"h-fit w-fit flex flex-col py-5 space-y-2"}>
                <div>
                    <div className={"text-3xl"}>Create a new shift type</div>
                    <div className={"text-xl"}>
                        Fill the form below and click <span className={"font-bold"}>Create</span> button to create a new
                        shift type
                    </div>
                </div>
                <div className={`h-fit w-full flex flex-col rounded space-y-1`}>
                    <div className={""}>Name</div>
                    <input {...register("name", {
                        required: "Name is required",
                        validate: value => {
                            if (value.trim().length < 2 || value.trim().length > 30) {
                                return "Name must be between 2 and 30 characters";
                            }
                            return true;
                        },
                    })}
                           className={`h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1`}/>
                    {errors.name && <div className={"text-red-500"}>{errors.name.message}</div>}
                </div>
                <div className={`h-fit w-full flex flex-col rounded space-y-1`}>
                    <div className={""}>Description</div>
                    <input {...register("description", {
                        required: false,
                        validate: value => {
                            if (value.trim().length > 500) {
                                return "Description must be shorter or equal 500 characters";
                            }
                            return true;
                        },
                    })}
                           className={`h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1`}/>
                    {errors.description && <div className={"text-red-500"}>{errors.description.message}</div>}
                </div>
                <div className={`h-fit w-full flex flex-col rounded space-y-1`}>
                    <div className={""}>Start</div>
                    <input type={"time"} {...register("start", {
                        required: "Start time is required"
                    })}
                           className={`h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1`}/>
                    {errors.start && <div className={"text-red-500"}>{errors.start.message}</div>}
                </div>
                <div className={`h-fit w-full flex flex-col rounded space-y-1`}>
                    <div className={""}>End</div>
                    <input type={"time"} {...register("end", {
                        required: "End time is required"
                    })}
                           className={`h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1`}/>
                    {errors.end && <div className={"text-red-500"}>{errors.end.message}</div>}
                </div>
                <button type={"submit"}
                        className={`h-10 w-full bg-blue-600 text-center font-bold text-white rounded hover:bg-blue-700 ${isSubmitting && "bg-blue-700"} transition-all duration-200`}
                >
                    Create
                </button>
                { errors.root && <div className={"text-red-500 text-center"}>{errors.root.message}</div> }
                { isSubmitSuccessful && <div className={"text-green-500 text-center"}>Shift type created successfully</div> }
            </form>
        </div>
    );
}