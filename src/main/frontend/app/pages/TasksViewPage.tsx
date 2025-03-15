import {useLocation, useNavigate} from "react-router";
import {useContext, useEffect, useState} from "react";
import {TaskApiContext, TaskEventApiContext, useCurrentSessionContext, UserApiContext} from "~/context/context";
import type {Task} from "~/api/task-api";
import {useForm} from "react-hook-form";
import type {SimplifiedUser, UsersRequest} from "~/api/user-api";
import {DefaultRoles} from "~/commons/commons";
import type {TaskEvent} from "~/api/task-event-api";

type FormData = {
    name: string;
    description: string;
    startDateTime: string;
    endDateTime: string;
    createdBy: string;
    color: string;
    taskEventUUID: string;
    isCompleted: "0" | "1";
}

export default function TasksViewPage() {
    const navigate = useNavigate();
    const session = useCurrentSessionContext();
    const taskApi = useContext(TaskApiContext);
    const userApi = useContext(UserApiContext);
    const taskEventApi = useContext(TaskEventApiContext);
    const [hasMoreTasks, setHasMoreTasks] = useState<boolean | undefined>(undefined);
    const [taskList, setTaskList] = useState<Task[] | undefined>(undefined);
    const {
        register,
        handleSubmit,
        setError,
        watch,
        formState: {isSubmitSuccessful, errors, isSubmitting}
    } = useForm<FormData>();
    const [currentPage, setCurrentPage] = useState<number | undefined>(undefined);
    const [task, setTask] = useState<Task | undefined>(undefined);
    const location = useLocation();
    const [editMode, setEditMode] = useState(false);
    const [taskUsers, setTaskUsers] = useState<SimplifiedUser[] | undefined>(undefined);
    const [userAddingMode, setUserAddingMode] = useState(false);
    const [users, setUsers] = useState<SimplifiedUser[] | undefined>(undefined);
    const [taskEvent, setTaskEvent] = useState<TaskEvent | undefined>(undefined);
    const [taskEvents, setTaskEvents] = useState<TaskEvent[] | undefined>(undefined);
    //const nameLikeWatch = useWatch("nameLike");

    useEffect(() => {
        const checkTaskParam = async () => {
            console.log("checking the task...");
            setTaskList(undefined);
            const params = new URLSearchParams(location.search);
            const taskParam = params.get("task");
            if (taskParam) {
                const response = await taskApi.getTask(taskParam);
                if (!response.raw.ok) {
                    throw new Error("Task param exists but something went wrong while fetching the task");
                }
                setTask(response.body);
                return true;
            }
            return false;
        }
        const fetchTasks = async () => {
            if (currentPage !== undefined || task) {
                return;
            }
            if (!session.session) {
                throw new Error("Session is undefined");
            }
            const response = await taskApi.getTasks({
                user: session.session.roles.includes(DefaultRoles.EMPLOYEE) ? session.session.username : undefined,
                createdBy: session.session.roles.includes(DefaultRoles.MANAGER) ? session.session.username : undefined,
                pageSize: 50,
                pageNumber: 0
            });
            if (!response.raw.ok) {
                throw new Error("Something went wrong while fetching tasks");
            }
            if (response.body.totalPages > 1) {
                setHasMoreTasks(true);
            } else {
                setHasMoreTasks(false);
            }
            setTaskList([...response.body.content]);
            setCurrentPage(0);
        };
        console.log("location changed");
        const init = async () => {
            if (await checkTaskParam()) {
                return;
            }
            await fetchTasks();
        }
        init();
    }, [currentPage, location]);

    useEffect(() => {
        if (!task) {
            return;
        }
        fetchTaskEvent();
    }, [task]);

    const fetchTaskEvents = async () => {
        if (!task) {
            throw new Error("Task is undefined");
        }
        if (!session) {
            throw new Error("Session is undefined");
        }
        const response = await taskEventApi.getTaskEvents({
            createdBy: session.session && session.session.roles.includes(DefaultRoles.MANAGER) ? session.session.username : undefined,
            pageNumber: 0,
            pageSize: 500
        });
        if (!response.raw.ok) {
            throw new Error("Something went wrong while fetching the task events");
        }
        setTaskEvents([...response.body.content]);
    }

    const fetchTaskEvent = async () => {
        if (!task) {
            throw new Error("Task is undefined");
        }
        if (!task.taskEventUUID) {
            throw new Error("Task has no task event");
        }
        const response = await taskEventApi.getTaskEvent(task.taskEventUUID);
        if (!response.raw.ok) {
            throw new Error("Something went wrong while fetching the task event");
        }
        setTaskEvent(response.body);
    }

    const fetchTaskUsers = async (task: Task) => {
        const response = await taskApi.getUsersByTask(task.uuid);
        console.log(response.body.users);
        setTaskUsers(response.body.users.length > 0 ? [...response.body.users] : []);
    }

    const fetchUsers = async () => {
        const request: UsersRequest = {
            role: DefaultRoles.EMPLOYEE,
            isActive: true,
            pageNumber: 0,
            pageSize: 300
        };
        const response = await userApi.getSimplifiedUsers(request);
        if (!response.raw.ok) {
            throw new Error("Something went wrong while fetching the users");
        }
        console.log(response.body);
        setUsers([...response.body.users]);
    }

    const assignUserToTask = async (user: SimplifiedUser, task: Task) => {
        const response = await taskApi.assignUser(user.uuid, task.uuid);
        if (!response.ok) {
            throw new Error("Something went wrong while assigning the user");
        }
        fetchTaskUsers(task);
        setUserAddingMode(false);
    }

    const dismissUserFromTask = async (user: SimplifiedUser, task: Task) => {
        const response = await taskApi.dismissUser(user.uuid, task.uuid);
        if (!response.ok) {
            throw new Error("Something went wrong while dismissing the user");
        }
        fetchTaskUsers(task);
        setUserAddingMode(false);
    }

    const assignTaskEvent = async (taskEvent: TaskEvent) => {
        if (!task) {
            throw new Error("Task is undefined");
        }
        const response = await taskApi.patchTask({
            taskEventUUID: taskEvent.uuid
        }, task.uuid);
        if (!response.ok) {
            throw new Error("Something went wrong while assigning the task event");
        }
        setTaskEvent(taskEvent);
    }

    useEffect(() => {
        if (!userAddingMode) {
            return;
        }
        fetchUsers();
    }, [userAddingMode]);

    useEffect(() => {
        if (!task) {
            return;
        }
        fetchTaskUsers(task);
    }, [task]);

    const onSubmit = async (formData: FormData) => {
        const response = await taskApi.patchTask({
            name: formData.name ? formData.name.trim() : undefined,
            description: formData.description ? formData.description.trim() : undefined,
            startDateTime: formData.startDateTime ? new Date(formData.startDateTime) : undefined,
            endDateTime: formData.endDateTime ? new Date(formData.endDateTime) : undefined,
            color: formData.color ? formData.color : undefined,
            taskEventUUID: formData.taskEventUUID ? formData.taskEventUUID.trim() : undefined,
            isCompleted: formData.isCompleted === "1"
        }, task?.uuid!);
        if (!response.ok) {
            setError("root", {
                message: "Something went wrong editing the task"
            });
            throw new Error("Something went wrong editing the task");
        }
    }

    const redirectToCreationPage = () => {
        const params = new URLSearchParams(location.search);
        const url = location.pathname + "?tab=new-task";
        console.log(`Navigating to: ${url}...`);
        navigate("?tab=new-task");
    }

    return (
        <div className={"h-full w-full flex flex-col justify-center items-center"}>
            <div className={"h-full w-fit p-5 flex flex-col"}>
                <div className={"h-1/6 w-full flex flex-col space-y-3 mb-3 justify-center"}>
                    {taskList
                        && <div>
                            <div className={"text-2xl"}>Tasks</div>
                            <div className={"text-md"}>View all tasks</div>
                        </div>}

                    {taskList
                        && <div className={"h-fit w-full flex flex-row-reverse"}>
                            {session.session && (session.session.roles.includes(DefaultRoles.ADMIN) || session.session.roles.includes(DefaultRoles.MANAGER)) &&
                                <button
                                    onClick={() => redirectToCreationPage()}
                                    className={"bg-indigo-400 rounded p-2 cursor-pointer hover:bg-indigo-500 text-white font-bold"}
                                >Create new
                                </button>}
                        </div>}
                    {!taskList && task
                        && <div>
                            <div className={"text-2xl"}>Task info</div>
                            <div className={"text-md"}>Here you can inspect the task</div>
                        </div>}
                </div>
                <div className={"h-5/6 w-full"}>
                    <div className={"h-full w-fit flex flex-col"}>
                        {taskList
                            && <div className={"h-fit w-fit flex flex-col"}>
                                <div className={"h-fit w-fit bg-blue-600 flex flex-row font-bold text-white rounded-t"}>
                                    <div className={"h-10 w-20 flex flex-row items-center justify-center"}>
                                        COLOR
                                    </div>
                                    <div className={"h-10 w-96 text-center flex flex-row items-center justify-center"}>
                                        TASK NAME
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
                                                                             onClick={() => navigate("?tab=tasks&task=" + task.uuid)}
                                            >
                                                <div
                                                    className={"h-16 w-fit flex flex-row hover:bg-blue-100 cursor-pointer"}>
                                                    <div
                                                        className={"h-full w-20 flex flex-row items-center justify-center"}>
                                                        <div className={"h-7 w-3 rounded-2xl"}
                                                             style={{backgroundColor: task.color}}></div>
                                                    </div>
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
                                        {hasMoreTasks
                                            ? <button
                                                type={"submit"}
                                                className={"h-10 w-full bg-indigo-500 rounded"}
                                                disabled={!hasMoreTasks}
                                            >
                                                more...
                                            </button>
                                            : null}
                                    </div>
                                </div>
                            </div>}
                        {!taskList && task && !editMode
                            && <div>
                                {session.session && (session.session.roles.includes(DefaultRoles.ADMIN) || session.session.roles.includes(DefaultRoles.MANAGER)) &&
                                    <div className={"h-fit w-full flex flex-row justify-between mb-2"}>
                                        <button
                                            className={"h-10 w-20 rounded bg-red-500 hover:bg-red-600 text-white font-bold"}
                                            onClick={() => setEditMode(true)}>
                                            Delete
                                        </button>
                                        <button
                                            className={"h-10 w-20 rounded bg-indigo-500 hover:bg-indigo-600 text-white font-bold"}
                                            onClick={() => setEditMode(true)}>
                                            Edit
                                        </button>
                                    </div>}
                                <div className={"h-fit w-96 flex flex-col space-y-2"}>
                                    <div className={"h-fit w-full"}>
                                        <div>
                                            Name
                                        </div>
                                        <div className={"h-10 w-full bg-gray-200 rounded content-center px-1"}>
                                            {task.name}
                                        </div>
                                    </div>
                                    <div className={"h-fit w-full"}>
                                        <div>
                                            Description
                                        </div>
                                        <div className={"h-10 w-full bg-gray-200 rounded content-center px-1"}>
                                            {task.description}
                                        </div>
                                    </div>
                                    <div className={"h-fit w-full"}>
                                        <div>
                                            Start
                                        </div>
                                        <div className={"h-10 w-full bg-gray-200 rounded content-center px-1"}>
                                            {new Date(task.startDateTime).toLocaleDateString()} {new Date(task.startDateTime).toLocaleTimeString()}
                                        </div>
                                    </div>
                                    <div className={"h-fit w-full"}>
                                        <div>
                                            End
                                        </div>
                                        <div className={"h-10 w-full bg-gray-200 rounded content-center px-1"}>
                                            {new Date(task.endDateTime).toLocaleDateString()} {new Date(task.endDateTime).toLocaleTimeString()}
                                        </div>
                                    </div>
                                    <div className={"h-fit w-full"}>
                                        <div>
                                            Created by
                                        </div>
                                        <div className={"h-10 w-full bg-gray-200 rounded content-center px-1"}>
                                            {task.createdBy}
                                        </div>
                                    </div>
                                    <div className={"h-fit w-full"}>
                                        <div>
                                            Color
                                        </div>
                                        <div
                                            className={`h-10 w-full bg-gray-200 rounded items-center space-x-2 px-1 flex flex-row`}>
                                            <div>{task.color}</div>
                                            <div className={"size-5 rounded"}
                                                 style={{backgroundColor: task.color}}>
                                            </div>
                                        </div>
                                    </div>
                                    <div className={"h-fit w-full"}>
                                        <div>
                                            Status
                                        </div>
                                        <div className={"h-10 w-full bg-gray-200 rounded content-center px-1"}>
                                            {task.isCompleted ? "Completed" : "Incomplete"}
                                        </div>
                                    </div>
                                    <div className={"h-fit w-full"}>
                                        <div>
                                            UUID
                                        </div>
                                        <div className={"h-10 w-full bg-gray-200 rounded content-center px-1"}>
                                            {task.uuid}
                                        </div>
                                    </div>
                                    <div className={"h-fit w-full"}>
                                        <div>
                                            Task event uuid
                                        </div>
                                        <div className={"h-10 w-full bg-gray-200 rounded content-center px-1"}>
                                            {task.taskEventUUID}
                                        </div>
                                    </div>
                                </div>
                                {!userAddingMode && session.session && (session.session.roles.includes(DefaultRoles.MANAGER) || session.session.roles.includes(DefaultRoles.ADMIN)) &&
                                    <div className={"w-full h-fit py-3 space-y-2 flex flex-col"}>
                                        <div className={"flex flex-row"}>
                                            Users
                                            <button onClick={() => setUserAddingMode(true)}>
                                                <img className={"size-5 bg-gray-500 rounded mx-2"} src={"add-icon.png"}
                                                     alt={"+"}/>
                                            </button>
                                        </div>
                                        {taskUsers && taskUsers.length > 0
                                            ? <ul className={"space-y-2"}>
                                                {taskUsers.map(user =>
                                                    <li className={"flex flex-row"}>
                                                        <div
                                                            className={"grow h-10 bg-gray-300 content-center rounded px-2 truncate"}>
                                                            {user.firstName}{user.middleName ? " " + user.middleName + " " : " "}{user.lastName} ({user.username})
                                                        </div>
                                                        <button onClick={() => dismissUserFromTask(user, task)}
                                                                className={"h-10 w-10 bg-red-500 hover:bg-red-600 flex justify-center items-center rounded"}>
                                                            <img className={"size-7"} src={"cancel.png"} alt={"x"}/>
                                                        </button>
                                                    </li>)}
                                            </ul>
                                            : <div className={"w-full h-10"}>No user has been added</div>}
                                    </div>}
                                {userAddingMode &&
                                    <div className={"w-full h-fit py-3 flex flex-col space-y-2"}>
                                        <div className={"flex flex-row"}>
                                            Select a user
                                            <button onClick={() => setUserAddingMode(false)}>
                                                <img className={"size-5 bg-red-500 rounded-2xl mx-2"} src={"cancel.png"}
                                                     alt={"x"}/>
                                            </button>
                                        </div>
                                        {users
                                            ? <ul className={"w-full h-fit space-y-2"}>
                                                {users.map(user =>
                                                    <li>
                                                        <div onClick={() => assignUserToTask(user, task)}
                                                             className={"w-full h-10 bg-gray-200 hover:bg-gray-300 px-2 content-center rounded truncate cursor-pointer"}>
                                                            {user.firstName}{user.middleName ? " " + user.middleName + " " : " "}{user.lastName} ({user.username})
                                                        </div>
                                                    </li>)}
                                            </ul>
                                            : <div className={"w-full h-10"}>No user has been found</div>}
                                    </div>}
                            </div>}

                        {!taskList && task && editMode
                            && <div>
                                <div className={"h-10 w-full flex flex-row-reverse"}>
                                    <button
                                        className={"h-10 w-20 rounded bg-indigo-500 hover:bg-indigo-600 text-white font-bold"}
                                        onClick={() => {
                                            setEditMode(false);
                                            setTaskEvents(undefined);
                                        }}>
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
                                               defaultValue={task.name}/>
                                        {errors.name && <div className={"text-red-500"}>{errors.name.message}</div>}
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
                                               defaultValue={task.description}/>
                                        {errors.description &&
                                            <div className={"text-red-500"}>{errors.description.message}</div>}
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
                                               defaultValue={task.startDateTime.toString()}/>
                                        {errors.startDateTime &&
                                            <div className={"text-red-500"}>{errors.startDateTime.message}</div>}
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
                                               defaultValue={task.endDateTime.toString()}/>
                                        {errors.endDateTime &&
                                            <div className={"text-red-500"}>{errors.endDateTime.message}</div>}
                                    </div>
                                    <div className={"h-fit w-full"}>
                                        <div>Color</div>
                                        <input type={"color"}
                                               className={"h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1"}
                                               defaultValue={task.endDateTime.toString()}/>
                                    </div>
                                    <div className={"h-fit w-full"}>
                                        <div>Status</div>
                                        <select defaultValue={task.isCompleted ? 1 : 0}
                                                {...register("isCompleted")}
                                                className={"h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1"}>
                                            <option value={0}>Incomplete</option>
                                            <option value={1}>Completed</option>
                                        </select>
                                    </div>
                                    <button type={"submit"}
                                            className={"h-10 w-full rounded bg-indigo-500 hover:bg-indigo-600 text-center text-white font-bold"}>
                                        Save
                                    </button>
                                    {errors.root &&
                                        <div className={"text-red-500 text-center"}>{errors.root.message}</div>}
                                    {isSubmitSuccessful &&
                                        <div className={"text-green-500 text-center"}>Task edited successfully</div>}
                                </form>
                            </div>}
                    </div>
                </div>
            </div>
        </div>
    );
}