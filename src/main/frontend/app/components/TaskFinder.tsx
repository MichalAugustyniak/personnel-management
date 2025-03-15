import {useContext, useEffect, useState} from "react";
import {TaskApiContext} from "~/context/context";
import type {Task} from "~/api/task-api";
import {useForm, useWatch} from "react-hook-form";
import TaskListElement from "~/components/TaskListElement";
import {useNavigate} from "react-router";

type FormData = {
    nameLike: string;
    createdByUUID: string;
    userUUID: string;
    taskEventUUID: string;
    from: string;
    to: string;
}

export default function TaskFinder() {
    const taskApi = useContext(TaskApiContext);
    const [hasMoreTasks, setHasMoreTasks] = useState<boolean | undefined>(undefined);
    const [taskList, setTaskList] = useState<Task[] | undefined>(undefined);
    const { register, handleSubmit, watch } = useForm<FormData>();
    const [currentPage, setCurrentPage] = useState<number | undefined>(undefined);
    const navigate = useNavigate();
    //const nameLikeWatch = useWatch("nameLike");

    useEffect(() => {
        if (currentPage !== undefined) {
            return;
        }
        const fetchTasks = async () => {
            const response = await taskApi.getTasks({pageSize: 50, pageNumber: 0});
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
        fetchTasks();
    }, []);

    useEffect(() => {
        if (currentPage === 0 || currentPage === undefined) {
            return;
        }
        const fetchTasks = async () => {
            const response = await taskApi.getTasks({pageSize: 50, pageNumber: currentPage});
            if (!response.raw.ok) {
                throw new Error("Something went wrong while fetching tasks");
            }
            if (response.body.totalPages > 1) {
                setHasMoreTasks(true);
            } else {
                setHasMoreTasks(false);
            }
            if (!taskList) {
                return;
            }
            let nextTasks = taskList;
            nextTasks.push(...response.body.content);
            setTaskList(nextTasks);
        };
        fetchTasks();
    }, [currentPage]);

    const onSubmit = async (formData: FormData) => {
        //console.log()
    }

    const sampleTask: Task = {
        uuid: "uuid",
        name: "name",
        description: "test_description",
        startDateTime: new Date(Date.now()),
        endDateTime: new Date(Date.now()),
        taskEventUUID: "test_task_event_uuid",
        color: "#36a142",
        createdBy: "creator uuid"
    }

    return (
        <div className={"h-full w-fit flex flex-col"}>
            <div className={"h-fit w-fit flex flex-col"}>
                <div className={"h-10 w-full flex flex-row bg-indigo-200 rounded mb-2 justify-center"}>

                </div>
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
                            {taskList.map((task, key) => <li key={key}>
                                <TaskListElement task={task}/>
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
            </div>
        </div>
    );
}