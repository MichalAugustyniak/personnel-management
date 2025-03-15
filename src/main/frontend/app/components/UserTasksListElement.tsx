import type {Task} from "~/api/task-api";

export interface Props {
    task: Task;
    color: string;
    onClick?: (task: Task) => void;
}

export default function UserTasksListElement({task, color, onClick}: Props) {
    return (
        <div onClick={() => {
            onClick ? onClick(task) : null
        }} className={`h-10 w-full flex flex-row bg-${color}-300 rounded`}>
            <div className={"h-full w-full bg-green-300 rounded content-center items-center justify-center flex flex-row"}>{task.name}</div>
        </div>
    )
}