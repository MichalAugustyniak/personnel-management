import type {Task} from "~/api/task-api";

export interface Props {
    task: Task;
    onClick?: (task: Task) => void;
}

export default function TaskListElement({ task }: Props) {
    return (
        <div className={"h-16 w-fit flex flex-row hover:bg-blue-100"}>
            <div className={"h-full w-20 flex flex-row items-center justify-center"}>
                <div className={"h-7 w-3 rounded-2xl"} style={{ backgroundColor: task.color }}></div>
            </div>
            <div className={"h-full w-96 text-center flex flex-row items-center justify-center text-wrap"}>
                {task.name}
            </div>
            <div className={"h-full w-72 text-center flex flex-row items-center justify-center text-wrap"}>
                {`${new Date(task.startDateTime).toLocaleDateString()} ${new Date(task.startDateTime).toLocaleTimeString()}`}
            </div>
            <div className={"h-full w-48 text-center flex flex-row items-center justify-center text-wrap"}>
                {`${new Date(task.endDateTime).toLocaleDateString()} ${new Date(task.endDateTime).toLocaleTimeString()}`}
            </div>
            <div className={"h-full w-56 text-center flex flex-row items-center justify-center text-wrap"}>
                {task.createdBy}
            </div>
        </div>
    )
}