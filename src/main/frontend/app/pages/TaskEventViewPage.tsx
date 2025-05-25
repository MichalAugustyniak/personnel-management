import {useLocation, useNavigate} from "react-router";
import {useContext, useEffect, useState} from "react";
import {TaskEventApiContext, useCurrentSessionContext} from "~/context/context";
import {useForm} from "react-hook-form";
import type {TaskEvent} from "~/api/task-event-api";
import {DefaultRoles} from "~/commons/commons";

type FormData = {
    name: string;
    description: string;
    startDateTime: string;
    endDateTime: string;
}

export default function TaskEventViewPage() {
    const navigate = useNavigate();
    const session = useCurrentSessionContext();
    const taskEventApi = useContext(TaskEventApiContext);
    const [hasMore, setHasMore] = useState<boolean | undefined>(undefined);
    const [taskList, setTaskList] = useState<TaskEvent[] | undefined>(undefined);
    const { register, handleSubmit, setError, watch, formState: { isSubmitSuccessful, errors, isSubmitting } } = useForm<FormData>();
    const [currentPage, setCurrentPage] = useState<number | undefined>(undefined);
    const [taskEvent, setTaskEvent] = useState<TaskEvent | undefined>(undefined);
    const location = useLocation();
    const [editMode, setEditMode] = useState(false);
    const [isDeleted, setIsDeleted] = useState<boolean | undefined>(undefined);

    useEffect(() => {
        const checkTaskParam = async () => {
            setTaskList(undefined);
            const params = new URLSearchParams(location.search);
            const param = params.get("task-event");
            if (param) {
                const response = await taskEventApi.getTaskEvent(param);
                if (!response.raw.ok) {
                    throw new Error("task-event param exists but something went wrong while fetching the task event");
                }
                setTaskEvent(response.body);
                return true;
            }
            return false;
        }
        const fetchTasks = async () => {
            if (currentPage !== undefined || taskEvent) {
                return;
            }
            if (!session.session) {
                throw new Error("Session is undefined");
            }
            const response = await taskEventApi.getTaskEvents({
                user: session.session.roles.includes(DefaultRoles.EMPLOYEE) ? session.session.username : undefined,
                createdBy: session.session.roles.includes(DefaultRoles.MANAGER) ? session.session.username : undefined,
                pageSize: 50,
                pageNumber: 0
            });
            if (!response.raw.ok) {
                throw new Error("Something went wrong while fetching task events");
            }
            if (response.body.totalPages > 1) {
                setHasMore(true);
            } else {
                setHasMore(false);
            }
            setTaskList([...response.body.content]);
            setCurrentPage(0);
        };
        const init = async () => {
            if (await checkTaskParam()) {
                return;
            }
            await fetchTasks();
        }
        init();
    }, [currentPage, location]);

    const onSubmit = async (formData: FormData) => {
        const response = await taskEventApi.updateTaskEvent(taskEvent?.uuid!, {
            name: formData.name ? formData.name.trim() : undefined,
            description: formData.description ? formData.description.trim() : undefined,
            startDateTime: formData.startDateTime ? formData.startDateTime : undefined,
            endDateTime: formData.endDateTime ? formData.endDateTime : undefined,
        });
        if (!response.ok) {
            setError("root", {
                message: "Something went wrong editing the task event"
            });
            throw new Error("Something went wrong editing the task event");
        }
    }

    const redirectToCreationPage = () => {
        navigate("?tab=new-task-event");
    }

    const deleteTaskEvent = async () => {
        if (!taskEvent) {
            throw new Error("Cannot delete the task event: the task event is undefined");
        }
        const response = await taskEventApi.deleteTaskEvent(taskEvent.uuid);
        if (!response.ok) {
            throw new Error("Something went wrong while deleting the task event");
        }
        setIsDeleted(true);
    }

    return (
        <div className={"h-full w-full flex flex-col justify-center items-center"}>
            <div className={"h-full w-fit p-5 flex flex-col"}>
                <div className={"h-1/6 w-full flex flex-col space-y-3 mb-3 justify-center"}>
                    {taskList
                        && <div>
                            <div className={"text-2xl"}>Task events</div>
                            <div className={"text-md"}>View all task events</div>
                        </div>}

                    {taskList && session.session && (session.session.roles.includes(DefaultRoles.ADMIN) || session.session.roles.includes(DefaultRoles.MANAGER))
                        && <div className={"h-fit w-full flex flex-row-reverse"}>
                            <button
                                onClick={() => redirectToCreationPage()}
                                className={"bg-indigo-400 rounded p-2 cursor-pointer hover:bg-indigo-500 text-white font-bold"}
                            >Create new
                            </button>
                        </div>}
                    {!taskList && taskEvent
                        && <div>
                            <div className={"text-2xl"}>Task event info</div>
                            <div className={"text-md"}>Here you can inspect the task event</div>
                        </div>}
                </div>
                <div className={"h-5/6 w-full"}>
                    <div className={"h-full w-fit flex flex-col"}>
                        {taskList
                            && <div className={"h-fit w-fit flex flex-col"}>
                                <div className={"h-fit w-fit bg-blue-600 flex flex-row font-bold text-white rounded-t"}>
                                    <div className={"h-10 w-96 text-center flex flex-row items-center justify-center"}>
                                        TASK EVENT NAME
                                    </div>
                                    <div className={"h-10 w-72 text-center flex flex-row items-center justify-center"}>
                                        START
                                    </div>
                                    <div className={"h-10 w-48 text-center flex flex-row items-center justify-center"}>
                                        END
                                    </div>
                                    <div className={"h-10 w-56 text-center flex flex-row items-center justify-center"}>
                                        CREATED BY
                                    </div>
                                </div>
                                <div className={"w-full bg-gray-100"}>
                                    {taskList && taskList.length > 0
                                        ? <ul className={"h-fit w-full flex flex-col"}>
                                            {taskList.map((task, key) => <li key={key}
                                                                             onClick={() => navigate("?tab=task-events&task-event=" + task.uuid)}
                                            >
                                                <div className={"h-16 w-fit flex flex-row hover:bg-blue-100 cursor-pointer"}>
                                                    <div
                                                        className={"h-full w-96 text-center flex flex-row items-center justify-center text-wrap"}>
                                                        {task.name}
                                                    </div>
                                                    <div
                                                        className={"h-full w-72 text-center flex flex-row items-center justify-center text-wrap"}>
                                                        {`${new Date(task.startDateTime).toLocaleDateString()} ${new Date(task.startDateTime).toLocaleTimeString()}`}
                                                    </div>
                                                    <div
                                                        className={"h-full w-48 text-center flex flex-row items-center justify-center text-wrap"}>
                                                        {`${new Date(task.endDateTime).toLocaleDateString()} ${new Date(task.endDateTime).toLocaleTimeString()}`}
                                                    </div>
                                                    <div
                                                        className={"h-full w-56 text-center flex flex-row items-center justify-center text-wrap"}>
                                                        {task.createdBy}
                                                    </div>
                                                </div>
                                            </li>)}
                                        </ul>
                                        : <div className={"h-fit w-full text-center"}>Empty list</div>}
                                    <div className={"h-fit w-full py-2"}>
                                        {hasMore
                                            ? <button
                                                type={"submit"}
                                                className={"h-10 w-full bg-indigo-500 rounded"}
                                                disabled={!hasMore}
                                            >
                                                more...
                                            </button>
                                            : null}
                                    </div>
                                </div>
                            </div> }
                        { !taskList && taskEvent && !editMode
                            && <div>
                                {session.session && (session.session.roles.includes(DefaultRoles.ADMIN) || taskEvent.createdBy === session.session.username)
                                    && <div className={"h-fit w-full flex flex-row justify-between mb-2"}>
                                        <button
                                            className={"h-10 w-20 rounded bg-red-500 hover:bg-red-600 text-white font-bold"}
                                            onClick={deleteTaskEvent}>
                                            Delete
                                        </button>
                                        <button
                                            className={"h-10 w-20 rounded bg-indigo-500 hover:bg-indigo-600 text-white font-bold"}
                                            onClick={() => setEditMode(true)}>
                                            Edit
                                        </button>
                                    </div>}
                                {isDeleted && <div className={"h-fit w-full text-center content-center text-green-500"}>Task event deleted successfully</div>}
                                <div className={"h-fit w-96 flex flex-col space-y-2"}>
                                    <div className={"h-fit w-full"}>
                                        <div>
                                            Name
                                        </div>
                                        <div className={"min-h-10 h-fit text-wrap w-full bg-gray-200 rounded content-center px-1"}>
                                            {taskEvent.name}
                                        </div>
                                    </div>
                                    <div className={"h-fit w-full"}>
                                        <div>
                                            Description
                                        </div>
                                        <div className={"min-h-10 h-fit text-wrap w-full bg-gray-200 rounded content-center px-1"}>
                                            {taskEvent.description}
                                        </div>
                                    </div>
                                    <div className={"h-fit w-full"}>
                                        <div>
                                            Start
                                        </div>
                                        <div className={"h-10 w-full bg-gray-200 rounded content-center px-1"}>
                                            {new Date(taskEvent.startDateTime).toLocaleDateString()} {new Date(taskEvent.startDateTime).toLocaleTimeString()}
                                        </div>
                                    </div>
                                    <div className={"h-fit w-full"}>
                                        <div>
                                            End
                                        </div>
                                        <div className={"h-10 w-full bg-gray-200 rounded content-center px-1"}>
                                            {new Date(taskEvent.endDateTime).toLocaleDateString()} {new Date(taskEvent.endDateTime).toLocaleTimeString()}
                                        </div>
                                    </div>
                                    <div className={"h-fit w-full"}>
                                        <div>
                                            Created by
                                        </div>
                                        <div className={"h-10 w-full bg-gray-200 rounded content-center px-1"}>
                                            {taskEvent.createdBy}
                                        </div>
                                    </div>
                                    <div className={"h-fit w-full"}>
                                        <div>
                                            UUID
                                        </div>
                                        <div className={"h-10 w-full bg-gray-200 rounded content-center px-1"}>
                                            {taskEvent.uuid}
                                        </div>
                                    </div>
                                </div>
                            </div>}

                        { !taskList && taskEvent && editMode
                            && <div>
                                <div className={"h-10 w-full flex flex-row-reverse"}>
                                    <button className={"h-10 w-20 rounded bg-indigo-500 hover:bg-indigo-600 text-white font-bold"}
                                            onClick={() => setEditMode(false)}>
                                        Cancel
                                    </button>
                                </div>
                                <form className={"h-fit w-96 flex flex-col space-y-2"}
                                      onSubmit={handleSubmit(onSubmit)}>
                                    <div className={"h-fit w-full"}>
                                        <div>Name</div>
                                        <input {...register("name", {
                                            required: false,
                                            validate: value => {
                                                if (value.trim().length < 4 || value.trim().length > 30) {
                                                    return "Name must be between 4 and 30 characters";
                                                }
                                                return true;
                                            }
                                        })}
                                               className={"h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1"}
                                               defaultValue={taskEvent.name}/>
                                        { errors.name && <div className={"text-red-500"}>{errors.name.message}</div> }
                                    </div>
                                    <div className={"h-fit w-full"}>
                                        <div>Description</div>
                                        <input {...register("description", {
                                            required: false,
                                            validate: value => {
                                                if (value.trim().length > 500) {
                                                    return "Description must be shorter or equal 500 characters";
                                                }
                                                return true;
                                            },
                                        })}
                                               className={"h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1"}
                                               defaultValue={taskEvent.description}/>
                                        { errors.description && <div className={"text-red-500"}>{errors.description.message}</div> }
                                    </div>
                                    <div className={"h-fit w-full"}>
                                        <div>Start</div>
                                        <input {...register("startDateTime", {
                                            required: false,
                                            validate: value => {
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
                                               className={"h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1"}
                                               defaultValue={taskEvent.startDateTime.toString()}/>
                                        { errors.startDateTime && <div className={"text-red-500"}>{errors.startDateTime.message}</div> }
                                    </div>
                                    <div className={"h-fit w-full"}>
                                        <div>End</div>
                                        <input {...register("endDateTime", {
                                            required: false,
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
                                               className={"h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1"}
                                               defaultValue={taskEvent.endDateTime.toString()}/>
                                        { errors.endDateTime && <div className={"text-red-500"}>{errors.endDateTime.message}</div> }
                                    </div>
                                    <button type={"submit"}
                                            className={"h-10 w-full rounded bg-indigo-500 hover:bg-indigo-600 text-center text-white font-bold"}>
                                        Save
                                    </button>
                                    { errors.root && <div className={"text-red-500 text-center"}>{errors.root.message}</div> }
                                    { isSubmitSuccessful && <div className={"text-green-500 text-center"}>Task event edited successfully</div> }
                                </form>
                            </div>}
                    </div>
                </div>
            </div>
        </div>
    );
}