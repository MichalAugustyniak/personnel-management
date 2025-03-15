import {ScheduleApiContext, ShiftTypeContext, useCurrentSessionContext} from "~/context/context";
import {useContext, useEffect, useState} from "react";
import type {Schedule, ScheduleDay, SchedulesRequest} from "~/api/schedule-api";
import type {ShiftType} from "~/api/shift-type-api";

interface Props {
    scheduleUUID?: string
}

export default function UserSchedulePage({scheduleUUID}: Props) {
    const session = useCurrentSessionContext();
    const scheduleApi = useContext(ScheduleApiContext);
    const shiftTypeApi = useContext(ShiftTypeContext);
    const [activeSchedule, setActiveSchedule] = useState<Schedule | undefined>(undefined);
    const [shiftTypes, setShiftTypes] = useState<ShiftType[] | undefined>(undefined);
    const [focusedScheduleDay, setFocusedScheduleDay] = useState<number | undefined>(undefined);

    const fetchActiveSchedule = async () => {
        if (!session.session) {
            throw new Error("Cannot fetch the active schedule: session is undefined");
        }
        const request: SchedulesRequest = {
            isActive: true,
            user: session.session.username,
            pageNumber: 0,
            pageSize: 5
        };
        const response = await scheduleApi.getSchedules(request);
        if (!response.raw.ok) {
            const message = "Something went wrong while fetching the active schedule";
            throw new Error(message);
        }
        if (response.body.totalElements > 1) {
            const message = `More than one active schedules (${response.body.totalElements})`;
            throw new Error(message);
        }
        if (response.body.totalElements === 0) {
            setActiveSchedule(undefined);
            return;
        }
        const response2 = await scheduleApi.getSchedule(response.body.content[0].uuid);
        setActiveSchedule(response2.body);
    }

    const fetchShiftType = async (uuid: string) => {
        const response = await shiftTypeApi.getShiftType(uuid);
        if (!response.raw.ok) {
            throw new Error("Something went wrong while fetching the shift type");
        }
        return response.body;
    }

    const fetchAllShiftTypes = async (uuids: Set<string>) => {
        const result: ShiftType[] = [];
        for (const uuid of uuids) {
            const shiftType = await fetchShiftType(uuid);
            result.push(shiftType);
        }
        setShiftTypes(result);
    }

    const findShiftType = (day: ScheduleDay) => {
        if (!shiftTypes) {
            throw new Error("Shift types is undefined");
        }
        const result = shiftTypes.find(shiftType => shiftType.uuid === day.shiftTypeUUID);
        if (!result) {
            throw new Error("Shift type not found");
        }
        return result;
    }

    const fetchScheduleByUUID = async (uuid: string) => {
        const response = await scheduleApi.getSchedule(uuid);
        if (!response.raw.ok) {
            throw new Error("Something went wrong while fetching the schedule");
        }
        setActiveSchedule(response.body);
    }

    useEffect(() => {
        if (!scheduleUUID) {
            fetchActiveSchedule();
        } else {
            fetchScheduleByUUID(scheduleUUID);
        }
    }, []);

    useEffect(() => {
        if (!activeSchedule) {
            return;
        }
        console.log(activeSchedule.days);
        const shiftTypesToFetch = new Set(activeSchedule.days.map(day => day.shiftTypeUUID));
        fetchAllShiftTypes(shiftTypesToFetch);
    }, [activeSchedule]);

    useEffect(() => {
        console.log("focused schedule day: " + focusedScheduleDay);
    }, [focusedScheduleDay]);

    return (
        <div className={"h-full w-full flex flex-col items-center"}>
            <div className={"h-full w-fit flex flex-col"}>
                <div className={"h-1/6 grow content-center"}>
                    <div className={"text-2xl"}>Schedule</div>
                    <div className={"text-xl"}>The records below represent each schedule day</div>
                </div>
                <div className={"h-5/6 fit"}>
                    {activeSchedule
                        ? <div className={"h-fit w-fit"}>
                            <div className={"h-8 w-fit flex flex-row bg-blue-600 rounded text-white font-bold"}>
                                <div className={"h-8 w-72 text-center content-center"}>
                                    START
                                </div>
                                <div className={"h-8 w-72 text-center content-center"}>
                                    END
                                </div>
                                <div className={"h-8 w-72 text-center content-center"}>
                                    SHIFT TYPE
                                </div>
                            </div>
                            <ul className={"space-y-2"}>
                                {shiftTypes && activeSchedule.days.map((day, key) => (
                                    <li className={"bg-gray-200 rounded"} key={key}>
                                        <div className={"h-10 w-fit flex flex-row cursor-pointer"}
                                             tabIndex={0}
                                            onFocus={() => setFocusedScheduleDay(key)}
                                            onBlur={() => setFocusedScheduleDay(undefined)}>
                                            <div className={"h-10 w-72 text-center content-center"}>
                                                {new Date(day.startDateTime).toLocaleString()}
                                            </div>
                                            <div className={"h-10 w-72 text-center content-center"}>
                                                {new Date(day.endDateTime).toLocaleString()}
                                            </div>
                                            <div className={"h-10 w-72 text-center content-center"}>
                                                {findShiftType(day).name}
                                            </div>
                                        </div>
                                        {focusedScheduleDay !== undefined && focusedScheduleDay === key &&
                                        <div className={"h-fit w-full flex flex-col bg-gray-300 py-2 rounded"}>
                                            {day.workBreaks.length > 0
                                                ? <div className={"h-8 w-full text-center content-center"}>Work
                                                    breaks</div>
                                                : <div className={"h-8 w-full text-center content-center"}>No work breaks for this schedule day</div>}
                                            <ul>
                                                {day.workBreaks.map(workBreak => (
                                                    <li>
                                                        <div className={"h-10 w-full flex flex-row"}>
                                                            <div className={"h-10 w-72 text-center content-center"}>
                                                                {new Date(workBreak.startDateTime).toLocaleString()}
                                                            </div>
                                                            <div className={"h-10 w-72 text-center content-center"}>
                                                                {new Date(workBreak.endDateTime).toLocaleString()}
                                                            </div>
                                                            <div className={"h-10 w-72 text-center content-center"}>
                                                                {workBreak.isPaid ? "paid" : "unpaid"}
                                                            </div>
                                                        </div>
                                                    </li>
                                                ))}
                                            </ul>
                                        </div>}
                                    </li>
                                ))}
                            </ul>
                        </div>
                        : <div>You have no active schedule</div>}
                </div>
            </div>
        </div>
    );
}