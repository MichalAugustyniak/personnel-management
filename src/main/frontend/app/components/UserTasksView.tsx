import {useContext, useEffect, useState} from "react";
import {TaskApiContext, useCurrentSessionContext, UserApiContext} from "~/context/context";
import type {Task} from "~/api/task-api";
import type {User} from "~/api/user-api";
import UserTasksListElement from "~/components/UserTasksListElement";
import {DefaultRoles} from "~/commons/commons";

export interface Props {
    uuid: string;
}

export default function UserTasksView({ uuid }: Props) {
    const userApi = useContext(UserApiContext);
    const taskApi = useContext(TaskApiContext);
    const session = useCurrentSessionContext();
    const [user, setUser] = useState<User | undefined>(undefined)
    const [taskList, setTaskList] = useState<Task[] | undefined>(undefined);
    const [hasMoreTasks, setHasMoreTasks] = useState<boolean | undefined>(undefined);
    const [totalElements, setTotalElements] = useState<number | undefined>(undefined);
    const [currentPage, setCurrentPage] = useState<number | undefined>(undefined)
    const [userFetched, setUserFetched] = useState(false);

    useEffect(() => {
        const fetchUser = async () => {
            const response = await userApi.getUser({ userUUID: uuid });
            if (!response.raw.ok) {
                throw new Error("Something went wrong while fetching the user");
            }
            setUser(response.body);
        };
        fetchUser();
        setUserFetched(true);
    }, []);

    useEffect(() => {
        if (!user) {
            return;
        }
        const fetchTasks = async () => {
            console.log("fetching tasks");
            const response = await taskApi.getTasks({
                user: user.role === DefaultRoles.EMPLOYEE ? user.username : undefined,
                createdBy: user.role.includes(DefaultRoles.MANAGER) ? user.username : undefined,
                pageSize: 50,
                pageNumber: 0,
            });
            if (!response.raw.ok) {
                throw new Error("Something went wrong while fetching tasks");
            }
            if (response.body.totalPages > 1) {
                setHasMoreTasks(true);
            }
            const tasks: Task[] = [];
            if (taskList) {
                tasks.push(...taskList);
            }
            tasks.push(...response.body.content);
            console.log("start");
            console.log(response.body.content);
            console.log("stop");
            setTaskList(tasks);
            setCurrentPage(0);
            //setTaskList(response.body.content);
            //setHasMoreTasks(true);
            setTotalElements(response.body.totalElements);
        };
        fetchTasks();
    }, [user]);

    useEffect(() => {
        if (currentPage === 0 || undefined) {
            return;
        }
        const loadMore = async () => {
            if (!user) {
                throw new Error("User is undefined");
            }
            const response = await taskApi.getTasks({
                user: user.role === DefaultRoles.EMPLOYEE ? user.username : undefined,
                createdBy: user.role === DefaultRoles.MANAGER ? user.username : undefined,
                pageSize: 50,
                pageNumber: currentPage
            });
            if (!response.raw.ok) {
                throw new Error("Something went wrong while fetching tasks");
            }
            let tasks: Task[] = [];
            if (taskList) {
                tasks.push(...taskList);
            }
            tasks.push(...response.body.content);
            if (response.body.totalPages <= currentPage!) {
                setHasMoreTasks(false);
            }
            setTotalElements(response.body.totalElements);
            setTaskList(tasks);
        };
        loadMore();
    }, [currentPage]);

    return (
        <div className={"h-fit w-fit bg-green-200 rounded flex flex-col"}>
            <div className={"h-fit w-full text-center bg-green-300 rounded"}>
                Tasks
            </div>
            <div className={"h-fit w-full flex flex-col min-w-96"}>
                <div className={"h-fit w-full p-2"}>
                    { totalElements
                    ? `Total tasks: ${totalElements}`
                    : <div className={"text-center"}>This user has no tasks</div>}
                </div>
            </div>
            { taskList
            ? <ul className={"space-y-2"}>
                    { taskList.map(task =>
                    <li>
                        <UserTasksListElement task={task} color={"green"} onClick={undefined}/>
                    </li> ) }
                    { hasMoreTasks
                        ? <button className={"h-14 w-full hover:bg-green-600 text-center"}
                                  disabled={!hasMoreTasks}
                        onClick={() => setCurrentPage(currentPage ? currentPage + 1 : 0)}>
                            more...
                        </button>
                        : null}
                </ul>
                : null}
        </div>
    )
}
