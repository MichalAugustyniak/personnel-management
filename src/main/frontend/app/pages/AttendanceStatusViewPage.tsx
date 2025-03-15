import {useLocation, useNavigate} from "react-router";
import {useContext, useEffect, useState} from "react";
import {AttendanceStatusApiContext, TaskApiContext, TaskEventApiContext} from "~/context/context";
import type {Task} from "~/api/task-api";
import {useForm} from "react-hook-form";
import type {TaskEvent} from "~/api/task-event-api";
import type {AttendanceStatus} from "~/api/attendance-status-api";

type FormData = {
    name: string;
    description: string;
    startDateTime: string;
    endDateTime: string;
}

export default function AttendanceStatusViewPage() {
    const navigate = useNavigate();

    const taskApi = useContext(AttendanceStatusApiContext);
    const [hasMoreTasks, setHasMoreTasks] = useState<boolean | undefined>(undefined);
    const [taskList, setTaskList] = useState<AttendanceStatus[] | undefined>(undefined);
    const { register, handleSubmit, setError, watch, formState: { isSubmitSuccessful, errors, isSubmitting } } = useForm<FormData>();
    const [currentPage, setCurrentPage] = useState<number | undefined>(undefined);
    const [task, setTask] = useState<AttendanceStatus | undefined>(undefined);
    const location = useLocation();
    const [editMode, setEditMode] = useState(false);
    const [deleteSuccessful, setDeleteSuccessful] = useState<boolean | undefined>(undefined);

    useEffect(() => {
        const checkTaskParam = async () => {
            console.log("checking the attendance-status param...");
            setTaskList(undefined);
            const params = new URLSearchParams(location.search);
            const taskParam = params.get("attendance-status");
            console.log("attendance-status param = " + taskParam);
            if (taskParam) {
                const response = await taskApi.getAttendanceStatus(taskParam);
                if (!response.raw.ok) {
                    throw new Error("task-event param exists but something went wrong while fetching the attendance status");
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
            const response = await taskApi.getAttendanceStatuses({pageSize: 50, pageNumber: 0});
            if (!response.raw.ok) {
                throw new Error("Something went wrong while fetching attendance statuses");
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

    const onSubmit = async (formData: FormData) => {
        const response = await taskApi.updateAttendanceStatus(task?.uuid!, {
            name: formData.name ? formData.name.trim() : undefined,
            description: formData.description ? formData.description.trim() : undefined,
        });
        if (!response.ok) {
            setError("root", {
                message: "Something went wrong editing the attendance status"
            });
            throw new Error("Something went wrong editing the attendance status");
        }
    }

    const redirectToCreationPage = () => {
        const params = new URLSearchParams(location.search);
        const url = location.pathname + "?tab=new-attendance-status";
        console.log(`Navigating to: ${url}...`);
        navigate("?tab=new-attendance-status");
    }

    const onDelete = async () => {
        if (!task) {
            throw new Error("Attendance status not selected");
        }
        const response = await taskApi.deleteAttendanceStatus(task.uuid);
        if (!response.ok) {
            const message = "Something went wrong while deleting the attendance status";
            setError("root", {
                message: message
            });
            throw new Error(message);
        }
        setDeleteSuccessful(true);
    }

    return (
        <div className={"h-full w-full flex flex-col justify-center items-center"}>
            <div className={"h-full w-fit p-5 flex flex-col"}>
                <div className={"h-1/6 w-full flex flex-col space-y-3 mb-3 justify-center"}>
                    {taskList
                        && <div>
                            <div className={"text-2xl"}>Attendance statuses</div>
                            <div className={"text-md"}>View all attendance statuses</div>
                        </div>}

                    {taskList
                        && <div className={"h-fit w-full flex flex-row-reverse"}>
                            <button
                                onClick={() => redirectToCreationPage()}
                                className={"bg-indigo-400 rounded p-2 cursor-pointer hover:bg-indigo-500 text-white font-bold"}
                            >Create new
                            </button>
                        </div>}
                    {!taskList && task
                        && <div>
                            <div className={"text-2xl"}>Attendance status info</div>
                            <div className={"text-md"}>Here you can inspect the attendance status</div>
                        </div>}
                </div>
                <div className={"h-5/6 w-full"}>
                    <div className={"h-full w-fit flex flex-col"}>
                        {taskList
                            && <div className={"h-fit w-fit flex flex-col"}>
                                <div className={"h-fit w-fit bg-blue-600 flex flex-row font-bold text-white rounded-t"}>
                                    <div className={"h-10 w-72 text-center flex flex-row items-center justify-center"}>
                                        NAME
                                    </div>
                                    <div className={"h-10 w-96 text-center flex flex-row items-center justify-center"}>
                                        DESCRIPTION
                                    </div>
                                    <div className={"h-10 w-48 text-center flex flex-row items-center justify-center"}>
                                        EXCUSABLE
                                    </div>
                                </div>
                                <div className={"w-full bg-gray-100"}>
                                    {taskList && taskList.length > 0
                                        ? <ul className={"h-fit w-full flex flex-col"}>
                                            {taskList.map((task, key) => <li key={key}
                                                                             onClick={() => navigate("?tab=attendance-statuses&attendance-status=" + task.uuid)}
                                            >
                                                <div className={"h-16 w-fit flex flex-row hover:bg-blue-100 cursor-pointer"}>
                                                    <div
                                                        className={"h-full w-72 text-center flex flex-row items-center justify-center text-wrap"}>
                                                        {task.name}
                                                    </div>
                                                    <div
                                                        className={"h-full w-96 text-center flex flex-row items-center justify-center text-wrap"}>
                                                        {task.description}
                                                    </div>
                                                    <div
                                                        className={"h-full w-48 text-center flex flex-row items-center justify-center text-wrap"}>
                                                        {task.isExcusable ? "yes" : "no"}
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
                            </div> }
                        { !taskList && task && !editMode
                            && <div className={"space-y-3"}>
                                <div className={"h-fit w-full flex flex-row justify-between"}>
                                    <button
                                        className={"h-10 w-20 rounded bg-red-500 hover:bg-red-600 text-white font-bold"}
                                        onClick={onDelete}>
                                        Delete
                                    </button>
                                    <button
                                        className={"h-10 w-20 rounded bg-indigo-500 hover:bg-indigo-600 text-white font-bold"}
                                        onClick={() => setEditMode(true)}>
                                        Edit
                                    </button>
                                </div>
                                {deleteSuccessful && <div className={"h-fit w-full text-green-500 text-center"}>Attendance status deleted successfully</div>}
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
                                            Excusable
                                        </div>
                                        <div className={"h-10 w-full bg-gray-200 rounded content-center px-1"}>
                                            {task.isExcusable ? "yes" : "no"}
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
                                </div>
                            </div>}

                        { !taskList && task && editMode
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
                                               defaultValue={task.name}/>
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
                                               defaultValue={task.description}/>
                                        { errors.description && <div className={"text-red-500"}>{errors.description.message}</div> }
                                    </div>
                                    <button type={"submit"}
                                            className={"h-10 w-full rounded bg-indigo-500 hover:bg-indigo-600 text-center text-white font-bold"}>
                                        Save
                                    </button>
                                    { errors.root && <div className={"text-red-500 text-center"}>{errors.root.message}</div> }
                                    { isSubmitSuccessful && <div className={"text-green-500 text-center"}>Attendance status edited successfully</div> }
                                </form>
                            </div>}
                    </div>
                </div>
            </div>
        </div>
    );
}