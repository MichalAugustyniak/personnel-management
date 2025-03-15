import type {User} from "~/api/user-api";
import {useContext, useEffect, useState} from "react";
import type {
    AttachUsersToScheduleRequest,
    DetachUsersFromScheduleRequest,
    Schedule,
    SchedulesRequest
} from "~/api/schedule-api";
import {ScheduleApiContext} from "~/context/context";

interface Props {
    user: User
}

export default function UserScheduleView({user}: Props) {
    const scheduleApi = useContext(ScheduleApiContext);
    const [activeSchedule, setActiveSchedule] = useState<Schedule | undefined>(undefined);
    const [scheduleFetchingErrorMessage, setScheduleFetchingErrorMessage] = useState<string | undefined>(undefined);
    const [schedules, setSchedules] = useState<Schedule[] | undefined>(undefined);
    const [scheduleSelectionMode, setScheduleSelectionMode] = useState(false);

    const fetchActiveSchedule = async () => {
        if (!user) {
            throw new Error("Cannot fetch the active schedule: user is undefined");
        }
        const request: SchedulesRequest = {
            isActive: true,
            user: user.username,
            pageNumber: 0,
            pageSize: 5
        };
        const response = await scheduleApi.getSchedules(request);
        if (!response.raw.ok) {
            const message = "Something went wrong while fetching the active schedule";
            setScheduleFetchingErrorMessage(message)
            throw new Error(message);
        }
        if (response.body.totalElements > 1) {
            const message = `More than one active schedules (${response.body.totalElements})`;
            console.log(response.body);
            setScheduleFetchingErrorMessage(message);
            throw new Error(message);
        }
        if (response.body.totalElements === 0) {
            setActiveSchedule(undefined);
            return;
        }
        setActiveSchedule(response.body.content[0]);
    }

    const fetchSchedules = async () => {
        const request: SchedulesRequest = {
            pageNumber: 0,
            pageSize: 300
        }
        const response = await scheduleApi.getSchedules(request);
        if (!response.raw.ok) {
            throw new Error("Something went wrong while fetching the schedules");
        }
        setSchedules(response.body.content);
    }

    const attachUserToSchedule = async (schedule: Schedule) => {
        const request: AttachUsersToScheduleRequest = {
            users: [user.username]
        };
        const response = await scheduleApi.attachUsersToSchedule(schedule.uuid, request);
        if (!response.ok) {
            throw new Error("Something went wrong while attaching user to the schedule");
        }
        setScheduleSelectionMode(false);
        fetchActiveSchedule();
    }

    const detachUserFromSchedule = async (schedule: Schedule) => {
        const request: DetachUsersFromScheduleRequest = {
            users: [user.username]
        };
        const response = await scheduleApi.detachUsersFromSchedule(schedule.uuid, request);
        if (!response.ok) {
            throw new Error("Something went wrong while detaching user from the schedule");
        }
        fetchActiveSchedule()
    }

    useEffect(() => {
        if (!user) {
            return;
        }
        fetchActiveSchedule();
    }, [user]);

    useEffect(() => {
        if (!scheduleSelectionMode) {
            return;
        }
        fetchSchedules();
    }, [scheduleSelectionMode]);

    return (
        <div className={"flex flex-row h-fit w-full justify-between"}>
            {!scheduleSelectionMode &&
                <div className={"h-fit w-fit bg-sky-400 rounded flex flex-col"}>
                    <div className={"h-fit w-full py-2 text-center flex flex-row justify-center items-center"}>
                        Active schedule
                        {activeSchedule ?
                            <button onClick={() => detachUserFromSchedule(activeSchedule)}
                                    className={"size-5 bg-red-500 rounded-2xl hover:bg-red-600 mx-2"}>
                                <img src={"cancel.png"} alt={"x"}/>
                            </button>
                            : <button onClick={() => setScheduleSelectionMode(true)}
                                      className={"size-5 mx-2"}>
                                <img src={"add-icon.png"} alt={"x"}/>
                            </button>}
                    </div>
                    {activeSchedule ?
                        <div>
                            <div className={"h-fit w-full flex flex-row"}>
                                <div className={"w-72 text-center"}>Name</div>
                                <div className={"w-72 text-center"}>Description</div>
                            </div>
                            <div className={"h-fit w-full flex flex-row bg-sky-200 rounded"}>
                                <div className={"h-10 w-72 text-center truncate content-center"}>{activeSchedule.name}</div>
                                <div className={"h-10 w-72 text-center truncate content-center"}>{activeSchedule.description}</div>
                            </div>
                        </div>
                        :
                        <div className={"h-fit w-[576px] text-center truncate rounded bg-sky-200 py-2"}>This user has no
                            active schedule yet</div>}
                </div>}
            {scheduleSelectionMode &&
                <div className={"h-fit w-fit bg-sky-400 rounded flex flex-col"}>
                    <div className={"h-fit w-full py-2 text-center flex flex-row justify-center items-center"}>
                        Select a schedule
                        <button onClick={() => setScheduleSelectionMode(false)}
                                className={"size-5 bg-red-500 rounded-2xl hover:bg-red-600 mx-2"}>
                            <img src={"cancel.png"} alt={"x"}/>
                        </button>
                    </div>
                    <div className={"h-fit w-full flex flex-row"}>
                        <div className={"w-72 text-center"}>Name</div>
                        <div className={"w-72 text-center"}>Description</div>
                    </div>
                    {schedules &&
                        <ul className={"h-fit w-[576px] text-center truncate rounded bg-sky-200 py-2"}>
                            {schedules.map(schedule =>
                                <li>
                                    <div onClick={() => attachUserToSchedule(schedule)}
                                         className={"h-10 w-full flex flex-row items-center hover:bg-sky-300 cursor-pointer"}>
                                        <div className={"w-72 text-center truncate"}>{schedule.name}</div>
                                        <div className={"w-72 text-center truncate"}>{schedule.description}</div>
                                    </div>
                                </li>
                            )}
                        </ul>}
                    {!schedules &&
                        <div className={"h-fit w-[576px] text-center truncate rounded bg-sky-200 py-2"}>No schedules found</div>}
                </div>}
        </div>
    );
}