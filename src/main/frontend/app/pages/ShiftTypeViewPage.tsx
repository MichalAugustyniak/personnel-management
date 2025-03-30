import {useLocation, useNavigate} from "react-router";
import {useContext, useEffect, useState} from "react";
import {ShiftTypeContext, TaskApiContext, TaskEventApiContext, useCurrentSessionContext} from "~/context/context";
import type {Task} from "~/api/task-api";
import {useForm} from "react-hook-form";
import type {TaskEvent} from "~/api/task-event-api";
import type {ShiftType} from "~/api/shift-type-api";
import {DefaultRoles} from "~/commons/commons";

type FormData = {
    name: string;
    description: string;
    startDateTime: string;
    endDateTime: string;
}

export default function ShiftTypeViewPage() {
    const navigate = useNavigate();
    const session = useCurrentSessionContext();
    const shiftTypeApi = useContext(ShiftTypeContext);
    const [hasMore, setHasMore] = useState<boolean | undefined>(undefined);
    const [shiftTypes, setShiftTypes] = useState<ShiftType[] | undefined>(undefined);
    const { register, handleSubmit, setError, watch, formState: { isSubmitSuccessful, errors, isSubmitting } } = useForm<FormData>();
    const [currentPage, setCurrentPage] = useState<number | undefined>(undefined);
    const [shiftType, setShiftType] = useState<ShiftType | undefined>(undefined);
    const location = useLocation();
    const [editMode, setEditMode] = useState(false);
    const [isDeleted, setIsDeleted] = useState<boolean | undefined>(undefined);

    useEffect(() => {
        const checkTaskParam = async () => {
            setShiftTypes(undefined);
            const params = new URLSearchParams(location.search);
            const taskParam = params.get("shift-type");
            if (taskParam) {
                const response = await shiftTypeApi.getShiftType(taskParam);
                if (!response.raw.ok) {
                    throw new Error("shift-type param exists but something went wrong while fetching the shift type");
                }
                setShiftType(response.body);
                return true;
            }
            return false;
        }
        const fetchTasks = async () => {
            if (currentPage !== undefined || shiftType) {
                return;
            }
            const response = await shiftTypeApi.getShiftTypes({pageSize: 50, pageNumber: 0});
            if (!response.raw.ok) {
                throw new Error("Something went wrong while fetching shift types");
            }
            if (response.body.totalPages > 1) {
                setHasMore(true);
            } else {
                setHasMore(false);
            }
            setShiftTypes([...response.body.content]);
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
        const response = await shiftTypeApi.updateShiftType(shiftType?.uuid!, {
            name: formData.name ? formData.name.trim() : undefined,
            description: formData.description ? formData.description.trim() : undefined,
            startTime: formData.startDateTime ? formData.startDateTime : undefined,
            endTime: formData.endDateTime ? formData.endDateTime : undefined,
        });
        if (!response.ok) {
            setError("root", {
                message: "Something went wrong editing the shift type"
            });
            throw new Error("Something went wrong editing the shift type");
        }
    }

    const redirectToCreationPage = () => {
        navigate("?tab=new-shift-type");
    }

    const deleteShiftType = async () => {
        if (!shiftType) {
            throw new Error("Cannot delete the shift type: the shift type is undefined");
        }
        const response = await shiftTypeApi.deleteShiftType(shiftType.uuid);
        if (!response.ok) {
            throw new Error("Something went wrong while deleting the shift type");
        }
        setIsDeleted(true);
    }

    return (
        <div className={"h-full w-full flex flex-col justify-center items-center"}>
            <div className={"h-full w-fit p-5 flex flex-col"}>
                <div className={"h-1/6 w-full flex flex-col space-y-3 mb-3 justify-center"}>
                    {shiftTypes
                        && <div>
                            <div className={"text-2xl"}>Shift types</div>
                            <div className={"text-md"}>View all shift types</div>
                        </div>}

                    {shiftTypes && session.session && (session.session.roles.includes(DefaultRoles.ADMIN) || session.session.roles.includes(DefaultRoles.HR))
                        && <div className={"h-fit w-full flex flex-row-reverse"}>
                            <button
                                onClick={() => redirectToCreationPage()}
                                className={"bg-indigo-400 rounded p-2 cursor-pointer hover:bg-indigo-500 text-white font-bold"}
                            >Create new
                            </button>
                        </div>}
                    {!shiftTypes && shiftType
                        && <div>
                            <div className={"text-2xl"}>Shift type info</div>
                            <div className={"text-md"}>Here you can inspect the shift types</div>
                        </div>}
                </div>
                <div className={"h-5/6 w-full"}>
                    <div className={"h-full w-fit flex flex-col"}>
                        {shiftTypes
                            && <div className={"h-fit w-fit flex flex-col"}>
                                <div className={"h-fit w-fit bg-blue-600 flex flex-row font-bold text-white rounded-t"}>
                                    <div className={"h-10 w-48 text-center flex flex-row items-center justify-center"}>
                                        NAME
                                    </div>
                                    <div className={"h-10 w-72 text-center flex flex-row items-center justify-center"}>
                                        DESCRIPTION
                                    </div>
                                    <div className={"h-10 w-48 text-center flex flex-row items-center justify-center"}>
                                        START
                                    </div>
                                    <div className={"h-10 w-48 text-center flex flex-row items-center justify-center"}>
                                        END
                                    </div>
                                </div>
                                <div className={"w-full bg-gray-100"}>
                                    {shiftTypes && shiftTypes.length > 0
                                        ? <ul className={"h-fit w-full flex flex-col"}>
                                            {shiftTypes.map((task, key) => <li key={key}
                                                                             onClick={() => navigate("?tab=shift-types&shift-type=" + task.uuid)}
                                            >
                                                <div
                                                    className={"h-16 w-fit flex flex-row hover:bg-blue-100 cursor-pointer"}>
                                                    <div
                                                        className={"min-h-full h-fit w-48 text-center flex flex-row items-center justify-center text-wrap"}>
                                                        {task.name}
                                                    </div>
                                                    <div
                                                        className={"min-h-full h-fit w-72 text-center flex flex-row items-center justify-center text-wrap"}>
                                                        {task.description}
                                                    </div>
                                                    <div
                                                        className={"h-full w-48 text-center flex flex-row items-center justify-center text-wrap"}>
                                                        {task.startTime}
                                                    </div>
                                                    <div
                                                        className={"h-full w-48 text-center flex flex-row items-center justify-center text-wrap"}>
                                                        {task.endTime}
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
                        { !shiftTypes && shiftType && !editMode
                            && <div>
                                {session.session && (session.session.roles.includes(DefaultRoles.ADMIN) || session.session.roles.includes(DefaultRoles.HR)) &&
                                    <div className={"h-fit w-full flex flex-row justify-between mb-2"}>
                                        <button
                                            className={"h-10 w-20 rounded bg-red-500 hover:bg-red-600 transition-all duration-200 text-white font-bold"}
                                            onClick={deleteShiftType}>
                                            Delete
                                        </button>
                                        <button
                                            className={"h-10 w-20 rounded bg-indigo-500 hover:bg-indigo-600 text-white font-bold"}
                                            onClick={() => setEditMode(true)}>
                                            Edit
                                        </button>
                                    </div>}
                                {isDeleted && <div className={"h-fit w-full text-center content-center text-green-500"}>Shift type deleted successfully</div>}
                                <div className={"h-fit w-96 flex flex-col space-y-2"}>
                                    <div className={"h-fit w-full"}>
                                        <div>
                                            Name
                                        </div>
                                        <div className={"h-10 w-full bg-gray-200 rounded content-center px-1"}>
                                            {shiftType.name}
                                        </div>
                                    </div>
                                    <div className={"h-fit w-full"}>
                                        <div>
                                            Description
                                        </div>
                                        <div className={"h-10 w-full bg-gray-200 rounded content-center px-1"}>
                                            {shiftType.description}
                                        </div>
                                    </div>
                                    <div className={"h-fit w-full"}>
                                        <div>
                                            Start
                                        </div>
                                        <div className={"h-10 w-full bg-gray-200 rounded content-center px-1"}>
                                            {shiftType.startTime}
                                        </div>
                                    </div>
                                    <div className={"h-fit w-full"}>
                                        <div>
                                            End
                                        </div>
                                        <div className={"h-10 w-full bg-gray-200 rounded content-center px-1"}>
                                            {shiftType.startTime}
                                        </div>
                                    </div>
                                    <div className={"h-fit w-full"}>
                                        <div>
                                            UUID
                                        </div>
                                        <div className={"h-10 w-full bg-gray-200 rounded content-center px-1"}>
                                            {shiftType.uuid}
                                        </div>
                                    </div>
                                </div>
                            </div>}

                        { !shiftTypes && shiftType && editMode
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
                                                if (value.trim().length < 2 || value.trim().length > 30) {
                                                    return "Name must be between 2 and 30 characters";
                                                }
                                                return true;
                                            }
                                        })}
                                               className={"h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1"}
                                               defaultValue={shiftType.name}/>
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
                                               defaultValue={shiftType.description}/>
                                        { errors.description && <div className={"text-red-500"}>{errors.description.message}</div> }
                                    </div>
                                    <div className={"h-fit w-full"}>
                                        <div>Start</div>
                                        <input {...register("startDateTime")}
                                               type={"time"}
                                               className={"h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1"}
                                               defaultValue={shiftType.startTime}/>
                                        { errors.startDateTime && <div className={"text-red-500"}>{errors.startDateTime.message}</div> }
                                    </div>
                                    <div className={"h-fit w-full"}>
                                        <div>End</div>
                                        <input {...register("endDateTime",)}
                                               type={"time"}
                                               className={"h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1"}
                                               defaultValue={shiftType.endTime}/>
                                        { errors.endDateTime && <div className={"text-red-500"}>{errors.endDateTime.message}</div> }
                                    </div>
                                    <button type={"submit"}
                                            className={"h-10 w-full rounded bg-indigo-500 hover:bg-indigo-600 text-center text-white font-bold"}>
                                        Save
                                    </button>
                                    { errors.root && <div className={"text-red-500 text-center"}>{errors.root.message}</div> }
                                    { isSubmitSuccessful && <div className={"text-green-500 text-center"}>Shift type edited successfully</div> }
                                </form>
                            </div>}
                    </div>
                </div>
            </div>
        </div>
    );
}