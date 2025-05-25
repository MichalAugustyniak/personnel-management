import {type SubmitHandler, useForm} from "react-hook-form";
import {useContext, useEffect, useState} from "react";
import {AttendanceApiContext, AttendanceStatusApiContext, ScheduleApiContext, UserApiContext} from "~/context/context";
import type {AttendanceStatus} from "~/api/attendance-status-api";
import type {Schedule, ScheduleDay, SchedulesRequest} from "~/api/schedule-api";
import type {SimplifiedUser} from "~/api/user-api";
import {DefaultRoles} from "~/commons/commons";
import type {AttendanceCreationRequest} from "~/api/attendance-api";

type FormData = {
    startTime: string;
    endTime: string;
    user: string;
};

export default function AttendanceCreationPage() {
    const attendanceApi = useContext(AttendanceApiContext);
    const scheduleApi = useContext(ScheduleApiContext);
    const userApi = useContext(UserApiContext);
    const attendanceStatusApi = useContext(AttendanceStatusApiContext);
    const { register, handleSubmit, setError, watch, formState: { isSubmitting, errors, isSubmitSuccessful } } = useForm<FormData>();
    const [schedule, setSchedule] = useState<Schedule | undefined>(undefined);
    const [scheduleDays, setScheduleDays] = useState<ScheduleDay[] | undefined>(undefined);
    const [selectedScheduleDay, setSelectedScheduleDay] = useState<ScheduleDay | undefined>(undefined);
    const [attendanceStatuses, setAttendanceStatuses] = useState<AttendanceStatus[] | undefined>(undefined);
    const [users, setUsers] = useState<SimplifiedUser[] | undefined>(undefined);
    const [selectedUser, setSelectedUser] = useState<SimplifiedUser | undefined>(undefined);
    const [selectedAttendanceStatus, setSelectedAttendanceStatus] = useState<AttendanceStatus | undefined>(undefined);



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

    const fetchSchedule = async (user: string) => {
        const schedulesRequest: SchedulesRequest = {
            user: user,
            isActive: true,
            pageNumber: 0,
            pageSize: 2
        };
        const response = await scheduleApi.getSchedules(schedulesRequest);
        if (!response.raw.ok) {
            const message = "Something went wrong while fetching the schedule";
            setError("root", {
                message: message
            });
            setSchedule(undefined);
            throw new Error(message);
        }
        if (response.body.content.length > 1) {
            const message = "More than one schedule found";
            setError("root", {
                message: message
            });
            setSchedule(undefined);
            throw new Error(message);
        }
        if (response.body.content.length === 0) {
            const message = "No schedule found";
            setError("root", {
                message: message
            });
            setSchedule(undefined);
            throw new Error(message);
        }
        const response2 = await scheduleApi.getSchedule(response.body.content[0].uuid);
        setSchedule(response2.body);
    }

    const fetchUsers = async () => {
        const response = await userApi.getSimplifiedUsers({
            role: DefaultRoles.EMPLOYEE,
            pageNumber: 0,
            pageSize: 300
        });
        if (!response.raw.ok) {
            throw new Error("Something went wrong while fetching the users");
        }
        setUsers([...response.body.users]);
    }

    const createAttendance: SubmitHandler<FormData> = async (data: FormData) => {
        if (!selectedScheduleDay) {
            const message = "Schedule day is not selected";
            setError("root", {
                message: message
            });
            throw new Error(message);
        }
        if (!selectedUser) {
            const message = "User is not selected";
            setError("root", {
                message: message
            });
            throw new Error(message);
        }
        if (!selectedAttendanceStatus) {
            const message = "Attendance status is not selected";
            setError("root", {
                message: message
            })
            throw new Error(message);
        }
        const [startHours, startMinutes] = data.startTime.split(":").map(Number);
        const [endHours, endMinutes] = data.endTime.split(":").map(Number);
        const startDateTime = new Date(selectedScheduleDay.startDateTime);
        startDateTime.setHours(startHours);
        startDateTime.setMinutes(startMinutes);
        const endDateTime = new Date(selectedScheduleDay.startDateTime);
        endDateTime.setHours(endHours);
        endDateTime.setMinutes(endMinutes);
        if (startHours > endHours) {
            endDateTime.setDate(endDateTime.getDate() + 1);
        }
        if (startDateTime < new Date(selectedScheduleDay.startDateTime) || startDateTime > new Date(selectedScheduleDay.endDateTime)) {
            const message = "Start must be between start and end of selected schedule day";
            setError("startTime", {
                message: message
            });
        }
        if (endDateTime < new Date(selectedScheduleDay.startDateTime) || endDateTime > new Date(selectedScheduleDay.endDateTime)) {
            const message = "End must be between start and end of selected schedule day";
            setError("endTime", {
                message: message
            });
        }
        const request: AttendanceCreationRequest = {
            startDateTime: startDateTime.toISOString(),
            endDateTime: endDateTime.toISOString(),
            user: selectedUser.username,
            attendanceStatusUUID: selectedAttendanceStatus.uuid,
            scheduleDayUUID: selectedScheduleDay.uuid
        };
        const response = await attendanceApi.createAttendance(request);
        if (response.raw.status !== 201) {
            const message = "Something went wrong while creating the attendance";
            setError("root", {
                message: message
            });
            throw new Error(message);
        }
    }

    useEffect(() => {
        fetchUsers();
        fetchAttendanceStatuses();
    }, []);

    useEffect(() => {
        if (!selectedUser) {
            return;
        }
        fetchSchedule(selectedUser.username);
    }, [selectedUser]);

    useEffect(() => {
        if (!schedule) {
            return;
        }
        setScheduleDays([...(schedule.days)]);
    }, [schedule]);

    return (
        <div className={"h-full w-full flex flex-col items-center"}>
            <div className={"h-full w-fit flex flex-col"}>
                <div className={"h-1/6 w-fit flex-col content-center"}>
                    <h1 className={"text-3xl"}>Create a new attendance</h1>
                    <h2 className={"text-xl"}>Fill the form below and click <span className={"font-bold"}>Create</span> button to create an attendance</h2>
                </div>
                <div className={"h-fit w-full py-2 space-y-3"}>
                    <form id={"attendance-form"}
                          className={"h-fit w-full flex flex-col space-y-3"}
                          onSubmit={handleSubmit(createAttendance)}
                    >
                        <div className={`h-fit w-full flex flex-col rounded space-y-1`}>
                            <div className={""}>Start</div>
                            <input type={"time"}
                                {...register("startTime", {
                                    required: "Start is required"
                                })}
                                   className={`h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1`}/>
                            { errors.startTime && <div className={"text-red-500"}>{errors.startTime.message}</div> }
                        </div>
                        <div className={`h-fit w-full flex flex-col rounded space-y-1`}>
                            <div className={""}>End</div>
                            <input type={"time"}
                                {...register("endTime", {
                                required: "End is required",
                            })}
                                   className={`h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1`}/>
                            { errors.endTime && <div className={"text-red-500"}>{errors.endTime.message}</div> }
                        </div>
                    </form>
                    {!selectedUser &&
                        <div className={"w-full h-fit flex flex-col space-y-2 py-2"}>
                            <div>Select a user</div>
                            {users
                                ? <ul className={"space-y-2"}>
                                    {users.map(user =>
                                        <li>
                                            <div onClick={() => setSelectedUser(user)}
                                                 className={"h-10 w-full bg-gray-200 hover:bg-gray-300 rounded px-2 cursor-pointer truncate content-center"}>
                                                {user.firstName}{user.middleName ? " " + user.middleName + " " : " "}{user.lastName} ({user.username})
                                            </div>
                                        </li>)}
                                </ul>
                                : <div className={"h-10 w-full bg-gray-200 rounded px-2"}>Cannot find any user</div>}
                        </div>}
                    {selectedUser &&
                        <div className={"w-full h-fit flex flex-col space-y-2 py-2"}>
                            <div className={"w-full h-fit flex flex-row space-x-2 items-center"}>
                                <div>Selected user</div>
                                <button onClick={() => {
                                    setSelectedUser(undefined)
                                    setSchedule(undefined);
                                    setScheduleDays(undefined);
                                    setSelectedScheduleDay(undefined);
                                }}
                                        className={"size-5 bg-red-500 hover:bg-red-600 rounded-2xl"}>
                                    <img src={"cancel.png"} alt={"x"}/>
                                </button>
                            </div>
                            <div
                                className={"h-10 w-full bg-gray-200 rounded px-2 truncate content-center"}>
                                {selectedUser.firstName}{selectedUser.middleName ? " " + selectedUser.middleName + " " : " "}{selectedUser.lastName} ({selectedUser.username})
                            </div>
                        </div>}

                    {!selectedScheduleDay &&
                        <div className={"w-full h-fit flex flex-col space-y-2 py-2"}>
                            <div>Select a schedule day</div>
                            <div className={"h-8 w-full content-center bg-blue-600 text-white font-bold flex flex-row rounded"}>
                                <div className={"h-8 w-1/2 text-center content-center"}>
                                    START
                                </div>
                                <div className={"h-8 w-1/2 text-center content-center"}>
                                    END
                                </div>
                            </div>
                            {scheduleDays
                                ? <ul className={"space-y-2"}>
                                    {scheduleDays.map(day =>
                                        <li>
                                            <div onClick={() => setSelectedScheduleDay(day)}
                                                 className={"h-10 w-full bg-gray-200 hover:bg-gray-300 rounded px-2 cursor-pointer truncate content-center flex flex-row"}>
                                                <div className={"h-full w-1/2 text-center content-center"}>
                                                    {new Date(day.startDateTime).toLocaleString()}
                                                </div>
                                                <div className={"h-full w-1/2 text-center content-center"}>
                                                    {new Date(day.endDateTime).toLocaleString()}
                                                </div>
                                            </div>
                                        </li>)}
                                </ul>
                                : <>
                                    {selectedUser
                                        ? <div className={"h-10 w-full bg-gray-200 rounded px-2 content-center"}>Cannot
                                            find any schedule day</div>
                                        : <div className={"h-10 w-full bg-gray-200 rounded px-2 content-center"}>User is not selected</div>}
                                </>}
                        </div>}
                    {selectedScheduleDay &&
                        <div className={"w-full h-fit flex flex-col space-y-2 py-2"}>
                            <div className={"w-full h-fit flex flex-row space-x-2 items-center"}>
                                <div>Selected schedule day</div>
                                <button onClick={() => setSelectedScheduleDay(undefined)}
                                        className={"size-5 bg-red-500 hover:bg-red-600 rounded-2xl"}>
                                    <img src={"cancel.png"} alt={"x"}/>
                                </button>
                            </div>
                            <div
                                className={"h-10 w-full bg-gray-200 rounded px-2 truncate content-center flex flex-row"}>
                                <div className={"h-full w-1/2 text-center content-center"}>
                                    {new Date(selectedScheduleDay.startDateTime).toLocaleString()}
                                </div>
                                <div className={"h-full w-1/2 text-center content-center"}>
                                    {new Date(selectedScheduleDay.endDateTime).toLocaleString()}
                                </div>
                            </div>
                        </div>}
                    {!selectedAttendanceStatus &&
                        <div className={"w-full h-fit flex flex-col space-y-2 py-2"}>
                            <div>Select a attendance status</div>
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
                                                 className={"h-fit min-h-10 w-full bg-gray-200 hover:bg-gray-300 rounded px-2 cursor-pointer truncate content-center flex flex-row"}>
                                                <div className={"h-fit w-1/2 text-center content-center text-wrap"}>
                                                    {status.name}
                                                </div>
                                                <div className={"h-fit w-1/2 text-center content-center text-wrap"}>
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
                                className={"h-fit min-h-10 w-full bg-gray-200 rounded px-2 truncate content-center flex flex-row"}>
                                <div className={"h-fit w-1/2 text-center content-center text-wrap"}>
                                    {selectedAttendanceStatus.name}
                                </div>
                                <div className={"h-fit w-1/2 text-center content-center text-wrap"}>
                                    {selectedAttendanceStatus.description}
                                </div>
                            </div>
                        </div>}
                    <button form={"attendance-form"}
                        type={"submit"}
                        className={`h-10 w-full bg-blue-600 text-center font-bold text-white rounded hover:bg-blue-700 ${isSubmitting && "bg-blue-700"} transition-all duration-200`}
                    >
                        Create
                    </button>
                    {errors.root && <div className={"text-red-500 text-center"}>{errors.root.message}</div>}
                            {isSubmitSuccessful &&
                                <div className={"text-green-500 text-center"}>Attendance created
                                    successfully</div>}
                        </div>
                        </div>
                        </div>
                        );
                    }