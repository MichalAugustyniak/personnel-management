import {useContext, useEffect, useState} from "react";
import {TaskApiContext, TaskEventApiContext} from "~/context/context";
import {useForm} from "react-hook-form";
import type {TaskEvent} from "~/api/task-event-api";

type FormData = {
    name: string;
    description: string;
    startDateTime: string;
    endDateTime: string;
    color: string;
    taskEventUUID: string;
    isCompleted: 0 | 1;
};

const formInputBgColor = "gray";
const formInputLabelBgColor = "blue";
const formInputBgColorTone = "200";
const formInputLabelBgColorTone = "600";
const inputColor = `${formInputBgColor}-${formInputBgColorTone}`;
const labelColor = `${formInputLabelBgColor}-${formInputLabelBgColorTone}`;


export default function TaskCreationPage() {
    const taskApi = useContext(TaskApiContext);
    const taskEventApi = useContext(TaskEventApiContext);
    const { register, handleSubmit, setError, formState: { isSubmitting, errors, isSubmitSuccessful } } = useForm<FormData>();
    const [taskEvent, setTaskEvent] = useState<TaskEvent | undefined>(undefined);
    const [taskEventsList, setTaskEventsList] = useState<TaskEvent[] | undefined>(undefined);
    const [currentPage, setCurrentPage] = useState<number | undefined>(undefined);
    const [hasMore, setHasMore] = useState<boolean | undefined>(undefined)

    const onSubmit = async (formData: FormData) => {
        const response = await taskApi.createTask({
            name: formData.name.trim(),
            description: formData.description.trim() ? undefined : formData.description.trim(),
            startDateTime: new Date(formData.startDateTime),
            endDateTime: new Date(formData.endDateTime),
            color: formData.color,
            taskEventUUID: taskEvent ? taskEvent.uuid : undefined,
            isCompleted: formData.isCompleted === 1
        });
        if (response.raw.status !== 201) {
            const message = "Something went wrong while creating the task";
            setError("root", {
                message: message
            });
            throw new Error(message);
        }
    }

    const fetchTaskEvents = async () => {
        const response = await taskEventApi.getTaskEvents({
            pageNumber: currentPage ? currentPage : 0,
            pageSize: 50,
        });
        if (!response.raw.ok) {
            throw new Error("Something went wrong while fetching tasks");
        }
        if (response.body.totalPages > (currentPage ? currentPage : 0) + 1) {
            setHasMore(true);
        } else {
            setHasMore(false);
        }
        let nextTaskEvents = taskEventsList || [];
        nextTaskEvents.push(...response.body.content);
        setTaskEventsList(nextTaskEvents);
        return response.body;
    }

    useEffect(() => {
        if (!taskEventsList || taskEventsList.length !== 0) {
            return;
        }
        fetchTaskEvents();
    }, [taskEventsList]);

    return (
        <div className={"h-full w-full flex flex-col items-center"}>
            <div className={"h-full w-fit flex flex-col"}>
                <div className={"h-1/6 w-fit flex-col content-center"}>
                    <h1 className={"text-3xl"}>Create a new task</h1>
                    <h2 className={"text-xl"}>Fill the form below and click <span className={"font-bold"}>Create</span> button to create a new task</h2>
                </div>
                <div className={"h-5/6 w-full py-2"}>
                    <form className={"h-fit w-full flex flex-col space-y-3 pb-2"}
                          onSubmit={handleSubmit(onSubmit)}
                    >
                        <div className={`h-fit w-full flex flex-col rounded space-y-1`}>
                            <div className={""}>Name</div>
                            <input {...register("name", {
                                required: "Name is required",
                                validate: value => {
                                    if (value.trim().length < 4 || value.trim().length > 30) {
                                        return "Name must be between 4 and 30 characters";
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
                            {errors.startDateTime &&
                                <div className={"text-red-500"}>{errors.startDateTime.message}</div>}
                        </div>
                        <div className={`h-fit w-full flex flex-col rounded space-y-1`}>
                            <div className={""}>End</div>
                            <input {...register("endDateTime", {
                                required: "Start is required",
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
                            {errors.endDateTime && <div className={"text-red-500"}>{errors.endDateTime.message}</div>}
                        </div>
                        <div className={`h-fit w-full flex flex-col rounded space-y-1`}>
                            <div className={""}>Color</div>
                            <input {...register("color", {
                                required: "Color is required",
                            })}
                                   type={"color"}
                                   className={`h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1`}/>
                            {errors.color && <div className={"text-red-500"}>{errors.color.message}</div>}
                        </div>
                        <div className={`h-fit w-full flex flex-col rounded space-y-1`}>
                            <div className={""}>Status</div>
                            <select {...register("isCompleted")}
                                    defaultValue={0}
                                    className={`h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1`}>
                                <option value={0}>Incomplete</option>
                                <option value={1}>Completed</option>
                            </select>
                            {errors.color && <div className={"text-red-500"}>{errors.color.message}</div>}
                        </div>
                        {!taskEvent && !taskEventsList
                            && <button onClick={() => setTaskEventsList([])}
                                       className={"h-10 w-full bg-blue-500 text-center font-bold text-white rounded hover:bg-blue-600 transition-all duration-200"}>
                                Associate with a task event
                            </button>
                        }
                        {taskEventsList
                            && <div className={"h-fit w-full transition-all duration-500"}>
                                <div
                                    className={"h-10 w-full bg-indigo-500 rounded-t text-center content-center text-white font-semibold"}>
                                    Select task event
                                    <button onClick={() => {
                                        setTaskEventsList(undefined)
                                        setCurrentPage(undefined);
                                        setHasMore(undefined);
                                    }}
                                            className={"rounded text-center p-1 bg-red-500 ml-5 hover:bg-red-600"}>
                                        Cancel
                                    </button>
                                </div>
                                <ul className={"space-y-2 p-2 border-indigo-500 border-2 rounded-b"}>
                                    {taskEventsList.map(taskEvent => <li className={"h-fit w-full"}>
                                        <div onClick={() => {
                                            setTaskEvent(taskEvent);
                                            setTaskEventsList(undefined);
                                            setCurrentPage(undefined);
                                        }}
                                             className={"h-fit w-full bg-indigo-400 rounded flex flex-row cursor-pointer"}>
                                            <div
                                                className={"h-10 w-1/3 text-center text-white font-semibold content-center"}>
                                                {taskEvent.name}
                                            </div>
                                            <div
                                                className={"h-10 w-1/3 text-center text-white font-semibold content-center"}>
                                                {`${new Date(taskEvent.startDateTime).toLocaleDateString()} ${new Date(taskEvent.startDateTime).toLocaleTimeString()}`}
                                            </div>
                                            <div
                                                className={"h-10 w-1/3 text-center text-white font-semibold content-center"}>
                                                {`${new Date(taskEvent.endDateTime).toLocaleDateString()} ${new Date(taskEvent.endDateTime).toLocaleTimeString()}`}
                                            </div>
                                        </div>
                                    </li>)}
                                    {currentPage && hasMore
                                        && <button onSubmit={() => setCurrentPage(currentPage + 1)}
                                                   className={"h-10 w-full rounded bg-blue-600 text-white text-center font-semibold"}>more...</button>}
                                </ul>
                            </div>}
                        {taskEvent
                            && <div className={"h-fit w-full flex flex-col space-y-1"}>
                                <div className={"h-fit w-full flex flex-row space-x-2"}>
                                    <div>Selected task event</div>
                                    <button onClick={() => {
                                        setTaskEvent(undefined);
                                        setHasMore(undefined);
                                    }}
                                            className="flex items-center justify-center w-6 h-6 bg-red-500 text-white rounded-full hover:bg-red-600 focus:outline-none">
                                        <span className="text-xl">X</span>
                                    </button>

                                </div>
                                <div className={"h-10 w-full bg-indigo-500 flex flex-row rounded"}>
                                    <div className={"h-10 w-1/3 text-center text-white font-semibold content-center"}>
                                        {taskEvent.name}
                                    </div>
                                    <div className={"h-10 w-1/3 text-center text-white font-semibold content-center"}>
                                        {`${new Date(taskEvent.startDateTime).toLocaleDateString()} ${new Date(taskEvent.startDateTime).toLocaleTimeString()}`}
                                    </div>
                                    <div className={"h-10 w-1/3 text-center text-white font-semibold content-center"}>
                                        {`${new Date(taskEvent.endDateTime).toLocaleDateString()} ${new Date(taskEvent.endDateTime).toLocaleTimeString()}`}
                                    </div>
                                </div>
                            </div>}
                        <button type={"submit"}
                                className={`h-10 w-full bg-blue-600 text-center font-bold text-white rounded hover:bg-blue-700 ${isSubmitting && "bg-blue-700"} transition-all duration-200`}
                        >
                            Create
                        </button>
                        {errors.root && <div className={"text-red-500 text-center"}>{errors.root.message}</div>}
                        {isSubmitSuccessful &&
                            <div className={"text-green-500 text-center"}>Task created successfully</div>}
                    </form>
                </div>
            </div>
        </div>
    );
}