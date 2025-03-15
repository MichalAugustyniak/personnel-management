import {useLocation, useNavigate} from "react-router";
import {useContext, useEffect, useState} from "react";
import {AttendanceApiContext, AttendanceStatusApiContext, useCurrentSessionContext} from "~/context/context";
import {useForm} from "react-hook-form";
import type {Attendance} from "~/api/attendance-api";
import type {AttendanceStatus} from "~/api/attendance-status-api";
import {DefaultRoles} from "~/commons/commons";

type FormData = {
    startDateTime: string;
    endDateTime: string;
    attendanceStatus: string;
}

export default function AttendanceViewPage() {
    const navigate = useNavigate();
    const session = useCurrentSessionContext();
    const attendanceStatusApi = useContext(AttendanceStatusApiContext);
    const attendanceApi = useContext(AttendanceApiContext);
    const [hasMoreAttendances, setHasMoreAttendances] = useState<boolean | undefined>(undefined);
    const [attendanceList, setAttendanceList] = useState<Attendance[] | undefined>(undefined);
    const { register, handleSubmit, setError, formState: { isSubmitSuccessful, errors, isSubmitting } } = useForm<FormData>();
    const [currentPage, setCurrentPage] = useState<number | undefined>(undefined);
    const [attendance, setAttendance] = useState<Attendance | undefined>(undefined);
    const location = useLocation();
    const [editMode, setEditMode] = useState(false);
    const [selectedAttendanceStatus, setSelectedAttendanceStatus] = useState<AttendanceStatus | undefined>(undefined);
    const [attendanceStatuses, setAttendanceStatuses] = useState<AttendanceStatus[] | undefined>(undefined);
    const [isDeleted, setIsDeleted] = useState<boolean | undefined>(undefined);
    //const nameLikeWatch = useWatch("nameLike");

    useEffect(() => {
        const checkParam = async () => {
            setAttendanceList(undefined);
            const params = new URLSearchParams(location.search);
            const param = params.get("attendance");
            console.log("attendance param = " + param);
            if (param) {
                const response = await attendanceApi.getAttendance(param);
                if (!response.raw.ok) {
                    throw new Error("attendance param exists but something went wrong while fetching the attendance");
                }
                setAttendance(response.body);
                return true;
            }
            return false;
        }
        const fetchAttendances = async () => {
            if (currentPage !== undefined || attendance) {
                return;
            }
            if (!session.session) {
                throw new Error("Session is undefined");
            }
            const response = await attendanceApi.getAttendances({
                user: session.session.roles.includes(DefaultRoles.EMPLOYEE) ? session.session.username : undefined,
                pageSize: 500,
                pageNumber: 0
            });
            if (!response.raw.ok) {
                throw new Error("Something went wrong while fetching attendances");
            }
            if (response.body.totalPages > 1) {
                setHasMoreAttendances(true);
            } else {
                setHasMoreAttendances(false);
            }
            setAttendanceList([...response.body.content]);
            setCurrentPage(0);
        };
        const init = async () => {
            if (await checkParam()) {
                return;
            }
            await fetchAttendances();
        }
        init();
    }, [currentPage, location]);

    const onSubmit = async (formData: FormData) => {
        const response = await attendanceApi.updateAttendance(attendance?.uuid!, {
            startDateTime: formData.startDateTime ? formData.startDateTime : undefined,
            endDateTime: formData.endDateTime ? formData.endDateTime : undefined,
            attendanceStatus: formData.attendanceStatus ? formData.attendanceStatus : undefined
        });
        if (!response.ok) {
            setError("root", {
                message: "Something went wrong editing the attendance"
            });
            throw new Error("Something went wrong while editing the attendance");
        }
    }

    const fetchAttendanceStatuses = async () => {
        const response = await attendanceStatusApi.getAttendanceStatuses({
            pageNumber: 0,
            pageSize: 300
        });
        if (!response.raw.ok) {
            throw new Error("Something went wrong while fetching the attendance statuses");
        }
        setAttendanceStatuses([...response.body.content]);
    }

    const deleteAttendance = async () => {
        if (!attendance) {
            throw new Error("Cannot delete the attendance: the attendance is undefined");
        }
        const response = await attendanceApi.deleteAttendance(attendance.uuid);
        if (!response.ok) {
            throw new Error("Something went wrong while delete the attendance");
        }
        setIsDeleted(true);
    }

    useEffect(() => {
        if (!editMode) {
            setAttendanceStatuses(undefined);
        } else {
            fetchAttendanceStatuses();
        }
    }, [editMode]);

    useEffect(() => {
        if (!attendanceStatuses || !attendance) {
            return;
        }
        for (const status of attendanceStatuses) {
            if (status.uuid === attendance.attendanceStatus.uuid) {
                setSelectedAttendanceStatus(status);
            }
        }
    }, [attendanceStatuses]);

    const redirectToCreationPage = () => {
        const params = new URLSearchParams(location.search);
        const url = location.pathname + "?tab=new-attendance";
        console.log(`Navigating to: ${url}...`);
        navigate("?tab=new-attendance");
    }

    return (
        <div className={"h-full w-full flex flex-col justify-center items-center"}>
            <div className={"h-full w-fit p-5 flex flex-col"}>
                <div className={"h-1/6 w-full flex flex-col space-y-3 mb-3 justify-center"}>
                    {attendanceList
                        && <div>
                            <div className={"text-2xl"}>Attendances</div>
                            <div className={"text-md"}>View all attendances</div>
                        </div>}

                    {attendanceList && session.session && (session.session.roles.includes(DefaultRoles.ADMIN) || session.session.roles.includes(DefaultRoles.MANAGER))
                        && <div className={"h-fit w-full flex flex-row-reverse"}>
                            <button
                                onClick={() => redirectToCreationPage()}
                                className={"bg-indigo-400 rounded p-2 cursor-pointer hover:bg-indigo-500 text-white font-bold"}
                            >Create new
                            </button>
                        </div>}
                    {!attendanceList && attendance
                        && <div>
                            <div className={"text-2xl"}>Attendance info</div>
                            <div className={"text-md"}>Here you can inspect the attendance</div>
                        </div>}
                </div>
                <div className={"h-5/6 w-full"}>
                    <div className={"h-full w-fit flex flex-col"}>
                        {attendanceList
                            && <div className={"h-fit w-fit flex flex-col"}>
                                {session.session && (session.session.roles.includes(DefaultRoles.MANAGER) || session.session.roles.includes(DefaultRoles.ADMIN) || session.session.roles.includes(DefaultRoles.HR)) &&
                                    <div className={"h-fit w-full flex flex-row bg-blue-600 rounded mb-2"}>
                                        <div className={"h-full w-fit content-center p-2"}>
                                            <input
                                                className={"h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1"}
                                                placeholder={"username"}/>
                                        </div>
                                    </div>}
                                <div className={"h-fit w-fit bg-blue-600 flex flex-row font-bold text-white rounded-t"}>
                                    <div className={"h-10 w-64 text-center flex flex-row items-center justify-center"}>
                                        START
                                    </div>
                                    <div className={"h-10 w-64 text-center flex flex-row items-center justify-center"}>
                                        END
                                    </div>
                                    <div className={"h-10 w-48 text-center flex flex-row items-center justify-center"}>
                                        STATUS
                                    </div>
                                    <div className={"h-10 w-72 text-center flex flex-row items-center justify-center"}>
                                        USER
                                    </div>
                                </div>
                                <div className={"w-full bg-gray-100"}>
                                    {attendanceList && attendanceList.length > 0
                                        ? <ul className={"h-fit w-full flex flex-col"}>
                                            {attendanceList.map((attendance, key) => <li key={key}
                                                                             onClick={() => navigate("?tab=attendances&attendance=" + attendance.uuid)}
                                            >
                                                <div className={"h-16 w-fit flex flex-row hover:bg-blue-100 cursor-pointer"}>
                                                    <div
                                                        className={"h-full w-64 text-center flex flex-row items-center justify-center text-wrap"}>
                                                        {new Date(attendance.startDateTime).toLocaleString()}
                                                    </div>
                                                    <div
                                                        className={"h-full w-64 text-center flex flex-row items-center justify-center text-wrap"}>
                                                        {new Date(attendance.endDateTime).toLocaleString()}
                                                    </div>
                                                    <div
                                                        className={"h-full w-48 text-center flex flex-row items-center justify-center text-wrap"}>
                                                        {attendance.attendanceStatus.name}
                                                    </div>
                                                    <div
                                                        className={"h-full w-72 text-center flex flex-row items-center justify-center text-wrap"}>
                                                        {attendance.user}
                                                    </div>
                                                </div>
                                            </li>)}
                                        </ul>
                                        : <div className={"h-fit w-full text-center"}>Empty list</div>}
                                    <div className={"h-fit w-full py-2"}>
                                        {hasMoreAttendances
                                            ? <button
                                                type={"submit"}
                                                className={"h-10 w-full bg-indigo-500 rounded"}
                                                disabled={!hasMoreAttendances}
                                            >
                                                more...
                                            </button>
                                            : null}
                                    </div>
                                </div>
                            </div> }
                        { !attendanceList && attendance && !editMode
                            && <div>
                                {session.session && (session.session.roles.includes(DefaultRoles.ADMIN) || session.session.roles.includes(DefaultRoles.MANAGER))
                                    && <div className={"h-fit w-full flex flex-row justify-between mb-2"}>
                                        <button
                                            className={"h-10 w-20 rounded bg-red-500 hover:bg-red-600 text-white font-bold"}
                                            onClick={deleteAttendance}>
                                            Delete
                                        </button>
                                        <button
                                            className={"h-10 w-20 rounded bg-indigo-500 hover:bg-indigo-600 text-white font-bold"}
                                            onClick={() => setEditMode(true)}>
                                            Edit
                                        </button>
                                    </div>}
                                {isDeleted && <div className={"text-green-500 text-center content-center h-fit w-full"}>Attendance deleted successfully</div> }
                                <div className={"h-fit w-96 flex flex-col space-y-2"}>
                                    <div className={"h-fit w-full"}>
                                        <div>
                                            Start
                                        </div>
                                        <div className={"h-10 w-full bg-gray-200 rounded content-center px-1"}>
                                            {new Date(attendance.startDateTime).toLocaleString()}
                                        </div>
                                    </div>
                                    <div className={"h-fit w-full"}>
                                        <div>
                                            End
                                        </div>
                                        <div className={"h-10 w-full bg-gray-200 rounded content-center px-1"}>
                                            {new Date(attendance.endDateTime).toLocaleString()}
                                        </div>
                                    </div>
                                    <div className={"h-fit w-full"}>
                                        <div>
                                            Status
                                        </div>
                                        <div className={"h-10 w-full bg-gray-200 rounded content-center px-1"}>
                                            {attendance.attendanceStatus.name}
                                        </div>
                                    </div>
                                    <div className={"h-fit w-full"}>
                                        <div>
                                            User
                                        </div>
                                        <div className={"h-10 w-full bg-gray-200 rounded content-center px-1"}>
                                            {attendance.user}
                                        </div>
                                    </div>
                                    <div className={"h-fit w-full"}>
                                        <div>
                                            Schedule UUID
                                        </div>
                                        <div className={"h-10 w-full bg-gray-200 rounded content-center px-1"}>
                                            {attendance.scheduleDayUUID}
                                        </div>
                                    </div>
                                    <div className={"h-fit w-full"}>
                                        <div>
                                            UUID
                                        </div>
                                        <div className={"h-10 w-full bg-gray-200 rounded content-center px-1"}>
                                            {attendance.uuid}
                                        </div>
                                    </div>
                                </div>
                            </div>}

                        {!attendanceList && attendance && editMode
                            && <div>
                                <div className={"h-10 w-full flex flex-row-reverse"}>
                                    <button
                                        className={"h-10 w-20 rounded bg-indigo-500 hover:bg-indigo-600 text-white font-bold"}
                                        onClick={() => setEditMode(false)}>
                                        Cancel
                                    </button>
                                </div>
                                <form id={"attendance-form"}
                                      className={"h-fit w-96 flex flex-col space-y-2"}
                                      onSubmit={handleSubmit(onSubmit)}>
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
                                               defaultValue={attendance.startDateTime.toString()}/>
                                        { errors.startDateTime && <div className={"text-red-500"}>{errors.startDateTime.message}</div> }
                                    </div>
                                    <div className={"h-fit w-full"}>
                                        <div>End</div>
                                        <input {...register("endDateTime", {
                                            required: false,
                                            validate: (value) => {
                                                console.log(value);
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
                                               defaultValue={attendance.endDateTime.toString()}/>
                                        { errors.endDateTime && <div className={"text-red-500"}>{errors.endDateTime.message}</div> }
                                    </div>



                                    {!selectedAttendanceStatus &&
                                        <div className={"w-full h-fit flex flex-col space-y-2 py-2"}>
                                            <div>Select an attendance status</div>
                                            <div className={"h-8 w-full content-center bg-blue-600 text-white font-bold flex flex-row rounded"}>
                                                <div className={"h-8 w-1/2 text-center content-center"}>
                                                    NAME
                                                </div>
                                                <div className={"h-8 w-1/2 text-center content-center"}>
                                                    DESCRIPTION
                                                </div>
                                            </div>
                                            {attendanceStatuses
                                                ? <ul className={"space-y-2"}>
                                                    {attendanceStatuses.map(status =>
                                                        <li>
                                                            <div onClick={() => setSelectedAttendanceStatus(status)}
                                                                 className={"h-10 w-full bg-gray-200 hover:bg-gray-300 rounded px-2 cursor-pointer truncate content-center flex flex-row"}>
                                                                <div className={"h-full w-1/2 text-center content-center"}>
                                                                    {status.name}
                                                                </div>
                                                                <div className={"h-full w-1/2 text-center content-center"}>
                                                                    {status.description}
                                                                </div>
                                                            </div>
                                                        </li>)}
                                                </ul>
                                                : <div className={"h-10 w-full bg-gray-200 rounded px-2 content-center"}>Cannot find any attendance status</div>}
                                        </div>}
                                    {selectedAttendanceStatus &&
                                        <div className={"w-full h-fit flex flex-col space-y-2 py-2"}>
                                            <div className={"w-full h-fit flex flex-row space-x-2 items-center"}>
                                                <div>Selected attendance status</div>
                                                <button onClick={() => setSelectedAttendanceStatus(undefined)}
                                                        className={"size-5 bg-red-500 hover:bg-red-600 rounded-2xl"}>
                                                    <img src={"cancel.png"} alt={"x"}/>
                                                </button>
                                            </div>
                                            <div
                                                className={"min-h-10 h-fit w-full bg-gray-200 rounded px-2 truncate content-center flex flex-row"}>
                                                <div className={"h-full w-1/2 text-center content-center text-wrap"}>
                                                    {selectedAttendanceStatus.name}
                                                </div>
                                                <div className={"h-full w-1/2 text-center content-center text-wrap"}>
                                                    {selectedAttendanceStatus.description}
                                                </div>
                                            </div>
                                        </div>}



                                    <button type={"submit"}
                                            className={"h-10 w-full rounded bg-indigo-500 hover:bg-indigo-600 text-center text-white font-bold"}
                                            form={"attendance-form"}>
                                        Save
                                    </button>
                                    { errors.root && <div className={"text-red-500 text-center"}>{errors.root.message}</div> }
                                    { isSubmitSuccessful && <div className={"text-green-500 text-center"}>Attendance edited successfully</div> }
                                </form>
                            </div>}
                    </div>
                </div>
            </div>
        </div>
    );
}