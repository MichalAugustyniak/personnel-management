import {type SubmitHandler, useForm} from "react-hook-form";
import React, {useContext, useEffect, useState} from "react";
import type {ShiftType} from "~/api/shift-type-api";
import {ScheduleApiContext} from "~/context/context";
import ScheduleCreatorPatternTile from "~/components/ScheduleCreatorPatternTile";
import type {ScheduleCreationRequest, ScheduleDayCreationRequest, WorkBreakCreationRequest} from "~/api/schedule-api";

type FormData = {
    name: string;
    description: string;
    maxWorkingHoursPerDay: number;
}

export type DateRange = {
    start: Date,
    end: Date
}

export function compareDateRanges(date1: DateRange, date2: DateRange) {
    if (date1.start > date1.end) {
        throw new Error("date1: Start date time must be before or equal end date time");
    }
    if (date2.start > date2.end) {
        throw new Error("date2: Start date time must be before or equal end date time");
    }
    const isCovered = date1.start >= date2.start && date1.end <= date2.end;
    const leftSideCover = date1.start <= date2.start && date1.end > date2.start;
    const rightSideCover = date1.start < date2.end && date1.end >= date2.end;
    if (isCovered) {
        throw new Error("date1 is covered whole by date2");
    }
    if (leftSideCover) {
        throw new Error("date1 is covered by date2 from the right side");
    }
    if (rightSideCover) {
        throw new Error("Work break date is covered by the other work break from the left side");
    }
}

export type WorkBreak = {
    key: number,
    startDateTime: Date,
    endDateTime: Date,
    isPaid: boolean
}

export class Day {
    public readonly key;
    private _startDateTime?: Date;
    private _endDateTime?: Date;
    private _shiftType?: ShiftType;
    private _workBreaks: WorkBreak[];

    constructor(key: number, startDateTime: Date | undefined, endDateTime: Date | undefined, shiftType: ShiftType | undefined, workBreaks: WorkBreak[]) {
        this._startDateTime = startDateTime;
        this._endDateTime = endDateTime;
        this._shiftType = shiftType;
        this.key = key;
        this._workBreaks = workBreaks;
    }

    public append(workBreak: WorkBreak) {
        for (const wb of this._workBreaks) {
            compareDateRanges({start: workBreak.startDateTime, end: workBreak.endDateTime}, {
                start: wb.startDateTime,
                end: wb.endDateTime
            });
        }
        this._workBreaks.push(workBreak);
    }

    get startDateTime(): Date | undefined {
        return this._startDateTime;
    }

    set startDateTime(value: Date | undefined) {
        this._startDateTime = value;
    }

    get endDateTime(): Date | undefined {
        return this._endDateTime;
    }

    set endDateTime(value: Date | undefined) {
        this._endDateTime = value;
    }

    get shiftType(): ShiftType | undefined {
        return this._shiftType;
    }

    set shiftType(value: ShiftType | undefined) {
        this._shiftType = value;
    }

    get workBreaks(): WorkBreak[] {
        return this._workBreaks;
    }

    set workBreaks(value: WorkBreak[]) {
        this._workBreaks = value;
    }
}

export class Week {
    public readonly key: number;
    private _monday: Day;
    private _tuesday: Day;
    private _wednesday: Day;
    private _thursday: Day;
    private _friday: Day;
    private _saturday: Day;
    private _sunday: Day;


    constructor(key: number, monday: Day, tuesday: Day, wednesday: Day, thursday: Day, friday: Day, saturday: Day, sunday: Day) {
        this.key = key;
        this._monday = monday;
        this._tuesday = tuesday;
        this._wednesday = wednesday;
        this._thursday = thursday;
        this._friday = friday;
        this._saturday = saturday;
        this._sunday = sunday;
    }

    getDays() {
        return [this.monday, this.tuesday, this.wednesday, this.thursday, this.friday, this.saturday, this.sunday];
    }

    get monday(): Day {
        return this._monday;
    }

    set monday(value: Day) {
        this._monday = value;
    }

    get tuesday(): Day {
        return this._tuesday;
    }

    set tuesday(value: Day) {
        this._tuesday = value;
    }

    get wednesday(): Day {
        return this._wednesday;
    }

    set wednesday(value: Day) {
        this._wednesday = value;
    }

    get thursday(): Day {
        return this._thursday;
    }

    set thursday(value: Day) {
        this._thursday = value;
    }

    get friday(): Day {
        return this._friday;
    }

    set friday(value: Day) {
        this._friday = value;
    }

    get saturday(): Day {
        return this._saturday;
    }

    set saturday(value: Day) {
        this._saturday = value;
    }

    get sunday(): Day {
        return this._sunday;
    }

    set sunday(value: Day) {
        this._sunday = value;
    }
}

export const getWeek = () => {
    return new Week(
        Math.random() * 1000,
        new Day(
            Math.random() * 1000,
            undefined,
            undefined,
            undefined,
            []
        ),
        new Day(
            Math.random() * 1000,
            undefined,
            undefined,
            undefined,
            []
        ),
        new Day(
            Math.random() * 1000,
            undefined,
            undefined,
            undefined,
            []
        ),
        new Day(
            Math.random() * 1000,
            undefined,
            undefined,
            undefined,
            []
        ),
        new Day(
            Math.random() * 1000,
            undefined,
            undefined,
            undefined,
            []
        ),
        new Day(
            Math.random() * 1000,
            undefined,
            undefined,
            undefined,
            []
        ),
        new Day(
            Math.random() * 1000,
            undefined,
            undefined,
            undefined,
            []
        )
    );
}

const hourSequence: number[] = [];

for (let i = 1; i < 25; i++) {
    hourSequence.push(i);
}

function getWeekStart(dateString: string): string {
    const date = new Date(dateString);
    const day = date.getDay();
    const diff = day === 0 ? -6 : 1 - day;
    date.setDate(date.getDate() + diff);
    return date.toISOString().split('T')[0];
}

export default function ScheduleCreationPage() {
    const scheduleApi = useContext(ScheduleApiContext);
    const {
        register,
        handleSubmit,
        setError,
        watch,
        formState: {isSubmitting, errors, isSubmitSuccessful}
    } = useForm<FormData>();
    const [weeks, setWeeks] = useState<Week[]>([getWeek()]);
    const [patternRepeats, setPatternRepeats] = useState(0);
    const [scheduleStartDate, setScheduleStartDate] = useState<string | undefined>(undefined);
    const [workingSaturdays, setWorkingSaturdays] = useState(false);
    const [workingSundays, setWorkingSundays] = useState(false);
    const [holidayAssignments, setHolidayAssignments] = useState(false);
    const [scheduleWeeks, setScheduleWeeks] = useState<Week[]>([]);


    const appendWeek = () => {
        setWeeks((prevWeeks) => [...prevWeeks, getWeek()]); // Tworzymy nową tablicę, zamiast mutować stary stan
    }

    const updateWorkBreakStartDateTime = (startDateTime: Date, date: string) => {
        const updated = new Date(date);
        updated.setHours(startDateTime.getHours());
        updated.setMinutes(startDateTime.getMinutes());
        updated.setSeconds(startDateTime.getSeconds());
        return updated;
    }

    const updateWorkBreakEndDateTime = (startDateTime: Date, endDateTime: Date, date: string) => {
        const updated = new Date(date);
        updated.setHours(endDateTime.getHours());
        updated.setMinutes(endDateTime.getMinutes());
        updated.setSeconds(endDateTime.getSeconds());
        if (endDateTime.getTime() < startDateTime.getTime()) {
            updated.setDate(updated.getDate() + 1);
        }
        return updated;
    }

    const updateDayStartDateTime = (date: string, startTime: string | undefined) => {
        if (!startTime) {
            return undefined;
        }
        const updated = new Date(date);
        const [startHours, startMinutes] = startTime.split(":").map(Number);
        updated.setHours(startHours + 1);
        updated.setMinutes(startMinutes);
        updated.setSeconds(0);
        return updated;
    }

    const updateDayEndDateTime = (date: string, startTime: string | undefined, endTime: string | undefined) => {
        if (!startTime || !endTime) {
            return undefined;
        }
        const updated = new Date(date);
        const [startHours] = startTime.split(":").map(Number);
        const [endHours, endMinutes] = endTime.split(":").map(Number);
        updated.setHours(endHours + 1);
        updated.setMinutes(endMinutes);
        updated.setSeconds(0);
        if (endHours < startHours) {
            updated.setDate(updated.getDate() + 1);
        }
        return updated;
    }

    useEffect(() => {
        if (!scheduleStartDate) {
            return;
        }
        const schedule: Week[] = [];
        let mondayStartDate = getWeekStart(scheduleStartDate);

        for (let i = 0; i < patternRepeats + 1; i++) {
            for (const week of weeks) {
                let tuesdayStartDate = new Date(new Date(mondayStartDate).setDate(new Date(mondayStartDate).getDate() + 1)).toDateString();
                let wednesdayStartDate = new Date(new Date(mondayStartDate).setDate(new Date(mondayStartDate).getDate() + 2)).toDateString();
                let thursdayStartDate = new Date(new Date(mondayStartDate).setDate(new Date(mondayStartDate).getDate() + 3)).toDateString();
                let fridayStartDate = new Date(new Date(mondayStartDate).setDate(new Date(mondayStartDate).getDate() + 4)).toDateString();
                let saturdayStartDate = new Date(new Date(mondayStartDate).setDate(new Date(mondayStartDate).getDate() + 5)).toDateString();
                let sundayStartDate = new Date(new Date(mondayStartDate).setDate(new Date(mondayStartDate).getDate() + 6)).toDateString();
                let mondayWorkBreaks: WorkBreak[] = [];
                let tuesdayWorkBreaks: WorkBreak[] = [];
                let wednesdayWorkBreaks: WorkBreak[] = [];
                let thursdayWorkBreaks: WorkBreak[] = [];
                let fridayWorkBreaks: WorkBreak[] = [];
                let saturdayWorkBreaks: WorkBreak[] = [];
                let sundayWorkBreaks: WorkBreak[] = [];
                for (const workBreak of week.monday.workBreaks) {
                    let copy: WorkBreak = {
                        key: Math.random() * 1000,
                        startDateTime: updateWorkBreakStartDateTime(workBreak.startDateTime, mondayStartDate),
                        endDateTime: updateWorkBreakEndDateTime(workBreak.startDateTime, workBreak.endDateTime, mondayStartDate),
                        isPaid: workBreak.isPaid
                    };
                    mondayWorkBreaks.push(copy);
                }
                for (const workBreak of week.tuesday.workBreaks) {
                    let copy: WorkBreak = {
                        key: Math.random() * 1000,
                        startDateTime: updateWorkBreakStartDateTime(workBreak.startDateTime, tuesdayStartDate),
                        endDateTime: updateWorkBreakEndDateTime(workBreak.startDateTime, workBreak.endDateTime, tuesdayStartDate),
                        isPaid: workBreak.isPaid
                    };
                    tuesdayWorkBreaks.push(copy);
                }
                for (const workBreak of week.wednesday.workBreaks) {
                    let copy: WorkBreak = {
                        key: Math.random() * 1000,
                        startDateTime: updateWorkBreakStartDateTime(workBreak.startDateTime, wednesdayStartDate),
                        endDateTime: updateWorkBreakEndDateTime(workBreak.startDateTime, workBreak.endDateTime, wednesdayStartDate),
                        isPaid: workBreak.isPaid
                    };
                    wednesdayWorkBreaks.push(copy);
                }
                for (const workBreak of week.thursday.workBreaks) {
                    let copy: WorkBreak = {
                        key: Math.random() * 1000,
                        startDateTime: updateWorkBreakStartDateTime(workBreak.startDateTime, thursdayStartDate),
                        endDateTime: updateWorkBreakEndDateTime(workBreak.startDateTime, workBreak.endDateTime, thursdayStartDate),
                        isPaid: workBreak.isPaid
                    };
                    thursdayWorkBreaks.push(copy);
                }
                for (const workBreak of week.friday.workBreaks) {
                    let copy: WorkBreak = {
                        key: Math.random() * 1000,
                        startDateTime: updateWorkBreakStartDateTime(workBreak.startDateTime, fridayStartDate),
                        endDateTime: updateWorkBreakEndDateTime(workBreak.startDateTime, workBreak.endDateTime, fridayStartDate),
                        isPaid: workBreak.isPaid
                    };
                    fridayWorkBreaks.push(copy);
                }
                for (const workBreak of week.saturday.workBreaks) {
                    let copy: WorkBreak = {
                        key: Math.random() * 1000,
                        startDateTime: updateWorkBreakStartDateTime(workBreak.startDateTime, saturdayStartDate),
                        endDateTime: updateWorkBreakEndDateTime(workBreak.startDateTime, workBreak.endDateTime, saturdayStartDate),
                        isPaid: workBreak.isPaid
                    };
                    saturdayWorkBreaks.push(copy);
                }
                for (const workBreak of week.sunday.workBreaks) {
                    let copy: WorkBreak = {
                        key: Math.random() * 1000,
                        startDateTime: updateWorkBreakStartDateTime(workBreak.startDateTime, sundayStartDate),
                        endDateTime: updateWorkBreakEndDateTime(workBreak.startDateTime, workBreak.endDateTime, sundayStartDate),
                        isPaid: workBreak.isPaid
                    };
                    sundayWorkBreaks.push(copy);
                }
                let copiedWeek = new Week(
                    Math.random() * 1000,
                    new Day(
                        Math.random() * 1000,
                        updateDayStartDateTime(mondayStartDate, week.monday.shiftType?.startTime),
                        updateDayEndDateTime(mondayStartDate, week.monday.shiftType?.startTime, week.monday.shiftType?.endTime),
                        week.monday.shiftType,
                        mondayWorkBreaks
                    ),
                    new Day(
                        Math.random() * 1000,
                        updateDayStartDateTime(tuesdayStartDate, week.tuesday.shiftType?.startTime),
                        updateDayEndDateTime(tuesdayStartDate, week.tuesday.shiftType?.startTime, week.tuesday.shiftType?.endTime),
                        week.tuesday.shiftType,
                        tuesdayWorkBreaks
                    ),
                    new Day(
                        Math.random() * 1000,
                        updateDayStartDateTime(wednesdayStartDate, week.wednesday.shiftType?.startTime),
                        updateDayEndDateTime(wednesdayStartDate, week.wednesday.shiftType?.startTime, week.wednesday.shiftType?.endTime),
                        week.wednesday.shiftType,
                        wednesdayWorkBreaks
                    ),
                    new Day(
                        Math.random() * 1000,
                        updateDayStartDateTime(thursdayStartDate, week.thursday.shiftType?.startTime),
                        updateDayEndDateTime(thursdayStartDate, week.thursday.shiftType?.startTime, week.thursday.shiftType?.endTime),
                        week.thursday.shiftType,
                        thursdayWorkBreaks
                    ),
                    new Day(
                        Math.random() * 1000,
                        updateDayStartDateTime(fridayStartDate, week.friday.shiftType?.startTime),
                        updateDayEndDateTime(fridayStartDate, week.friday.shiftType?.startTime, week.friday.shiftType?.endTime),
                        week.friday.shiftType,
                        fridayWorkBreaks
                    ),
                    new Day(
                        Math.random() * 1000,
                        updateDayStartDateTime(saturdayStartDate, week.saturday.shiftType?.startTime),
                        updateDayEndDateTime(saturdayStartDate, week.saturday.shiftType?.startTime, week.saturday.shiftType?.endTime),
                        week.saturday.shiftType,
                        saturdayWorkBreaks
                    ),
                    new Day(
                        Math.random() * 1000,
                        updateDayStartDateTime(sundayStartDate, week.sunday.shiftType?.startTime),
                        updateDayEndDateTime(sundayStartDate, week.sunday.shiftType?.startTime, week.sunday.shiftType?.endTime),
                        week.sunday.shiftType,
                        sundayWorkBreaks
                    )
                );
                schedule.push(copiedWeek);
                mondayStartDate = new Date(new Date(mondayStartDate).setDate(new Date(mondayStartDate).getDate() + 7)).toDateString();
            }
        }
        setScheduleWeeks(schedule);
    }, [weeks, patternRepeats, scheduleStartDate]);

    const onSubmit: SubmitHandler<FormData> = async (data: FormData) => {
        if (!scheduleStartDate) {
            const message = "Schedule start date is required";
            setError("root", {
                message: message
            });
            throw new Error(message);
        }
        if (scheduleWeeks.length === 0) {
            const message = "Schedule cannot be empty";
            setError("root", {
                message: message
            });
        }
        const scheduleDays: ScheduleDayCreationRequest[] = [];
        for (const week of scheduleWeeks) {
            for (const day of week.getDays()) {
                if (!day.startDateTime || !day.endDateTime || !day.shiftType || day.startDateTime < new Date(scheduleStartDate)) {
                    continue;
                }
                const workBreaks: WorkBreakCreationRequest[] = day.workBreaks.map<WorkBreakCreationRequest>(workBreak => ({
                    startDateTime: workBreak.startDateTime.toISOString(),
                    endDateTime: workBreak.endDateTime.toISOString(),
                    isPaid: workBreak.isPaid
                }));
                scheduleDays.push({
                    startDateTime: day.startDateTime.toISOString(),
                    endDateTime: day.endDateTime.toISOString(),
                    shiftTypeUUID: day.shiftType.uuid,
                    workBreaks: workBreaks
                });
            }
        }
        const request: ScheduleCreationRequest = {
            name: data.name,
            description: data.description,
            maxWorkingHoursPerDay: data.maxWorkingHoursPerDay,
            enableHolidayAssignments: holidayAssignments,
            enableWorkingSaturdays: workingSaturdays,
            enableWorkingSundays: workingSundays,
            users: [],
            scheduleDays: scheduleDays
        }
        const response = await scheduleApi.createSchedule(request);
        if (response.raw.status !== 201) {
            const message = "Something went wrong white creating a schedule";
            setError("root", {
                message: message
            });
            throw new Error(message);
        }
    }


    return (
        <div className={"h-full w-full flex flex-col items-center"}>
            <div className={"h-full"}>
                <form id={"schedule-form"}
                      onSubmit={handleSubmit(onSubmit)}
                      className={"h-fit w-full flex flex-col py-5 space-y-2"}>
                    <div>
                        <div className={"text-3xl"}>Create a new schedule</div>
                        <div className={"text-xl"}>
                            Fill the form below and click <span className={"font-bold"}>Create</span> button to create a
                            new
                            schedule
                        </div>
                    </div>
                    <div className={`h-fit w-full flex flex-col rounded space-y-1`}>
                        <div className={""}>Name</div>
                        <input {...register("name", {
                            required: "Name is required",
                            validate: value => {
                                if (value.trim().length < 4 || value.trim().length > 30) {
                                    return "Name must be between 4 and 30 characters";
                                }
                                return true;
                            },
                        })}
                               className={`h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1`}/>
                        {errors.name && <div className={"text-red-500"}>{errors.name.message}</div>}
                    </div>
                    <div className={`h-fit w-full flex flex-col rounded space-y-1`}>
                        <div className={""}>Description</div>
                        <input {...register("description", {
                            required: false,
                            validate: value => {
                                if (value.trim().length > 500) {
                                    return "Description must be shorter or equal 500 characters";
                                }
                                return true;
                            },
                        })}
                               className={`h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1`}/>
                        {errors.description && <div className={"text-red-500"}>{errors.description.message}</div>}
                    </div>
                    <div className={`h-fit w-full flex flex-col rounded space-y-1`}>
                        <div className={""}>Max working hours per day</div>
                        <select {...register("maxWorkingHoursPerDay", {
                            required: "Max working hours per day is required",
                        })}
                                className={`h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1`}
                        >
                            {hourSequence.map(hour => <option value={hour}>{hour}</option>)}
                        </select>
                        {errors.maxWorkingHoursPerDay &&
                            <div className={"text-red-500"}>{errors.maxWorkingHoursPerDay.message}</div>}
                    </div>
                    <div className={`h-fit w-full flex flex-col rounded space-y-1`}>
                        <div className={""}>Holiday assignments</div>
                        <select defaultValue={0}
                                onChange={value => setHolidayAssignments(value.target.value === "1")}
                                className={"h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1"}>
                            <option value={0}>disabled</option>
                            <option value={1}>enabled</option>
                        </select>
                    </div>
                    <div className={`h-fit w-full flex flex-col rounded space-y-1`}>
                        <div className={""}>Working saturdays assignments</div>
                        <select defaultValue={0}
                                onChange={value => setWorkingSaturdays(value.target.value === "1")}
                                className={"h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1"}>
                            <option value={0}>disabled</option>
                            <option value={1}>enabled</option>
                        </select>
                    </div>
                    <div className={`h-fit w-full flex flex-col rounded space-y-1`}>
                        <div className={""}>Working sundays assignments</div>
                        <select defaultValue={0}
                                onChange={value => setWorkingSundays(value.target.value === "1")}
                                className={"h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1"}>
                            <option value={0}>disabled</option>
                            <option value={1}>enabled</option>
                        </select>
                    </div>
                </form>
                <div className={`h-fit w-full flex flex-col rounded space-y-1 mb-2`}>
                    <div className={""}>Form the pattern</div>
                    <div className={"bg-gray-200 rounded"}>
                        <div
                            className={"h-8 w-full grid grid-cols-7 text-white font-bold text-center content-center bg-blue-600 rounded-t"}>
                            <div>Mon</div>
                            <div>Tue</div>
                            <div>Wen</div>
                            <div>Thu</div>
                            <div>Fri</div>
                            <div>Sat</div>
                            <div>Sun</div>
                        </div>
                        <ul>
                            {weeks.map(week =>
                                <li>
                                    <div
                                        className={"h-fit w-full rounded bg-gray-200 border-2 border-gray-200 grid grid-cols-7 gap-1 relative"}>
                                        <ScheduleCreatorPatternTile weeks={weeks} setter={setWeeks}
                                                                    weekKey={week.key} dayKey={week.monday.key}/>
                                        <ScheduleCreatorPatternTile weeks={weeks} setter={setWeeks}
                                                                    weekKey={week.key} dayKey={week.tuesday.key}/>
                                        <ScheduleCreatorPatternTile weeks={weeks} setter={setWeeks}
                                                                    weekKey={week.key} dayKey={week.wednesday.key}/>
                                        <ScheduleCreatorPatternTile weeks={weeks} setter={setWeeks}
                                                                    weekKey={week.key} dayKey={week.thursday.key}/>
                                        <ScheduleCreatorPatternTile weeks={weeks} setter={setWeeks}
                                                                    weekKey={week.key} dayKey={week.friday.key}/>
                                        {workingSaturdays
                                            ? <ScheduleCreatorPatternTile weeks={weeks} setter={setWeeks}
                                                                          weekKey={week.key}
                                                                          dayKey={week.saturday.key}/>
                                            : <div
                                                className={"h-full w-full aspect-square content-center text-center text-wrap text-gray-400 font-bold"}>Disabled</div>}
                                        {workingSundays
                                            ? <ScheduleCreatorPatternTile weeks={weeks} setter={setWeeks}
                                                                          weekKey={week.key}
                                                                          dayKey={week.sunday.key}/>
                                            : <div
                                                className={"h-full w-full aspect-square content-center text-center text-wrap text-gray-400 font-bold"}>Disabled</div>}
                                    </div>
                                </li>
                            )}
                        </ul>
                        <button onClick={() => appendWeek()}
                                className={"h-10 w-full bg-blue-600 mt-2 rounded text-center text-white font-bold"}>add
                            new
                            week
                        </button>
                    </div>
                </div>
                <div className={"space-y-1"}>
                    <div className={`h-fit w-full flex flex-col rounded space-y-1`}>
                        <div className={""}>Schedule start date</div>
                        <input type={"date"}
                               onChange={(date) => setScheduleStartDate(date.target.value)}
                               className={`h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1`}/>
                    </div>
                    <div className={`h-fit w-full flex flex-col rounded space-y-1`}>
                        <div className={""}>Pattern repeats</div>
                        <input type={"number"}
                               defaultValue={0}
                               onChange={(value) => setPatternRepeats(parseInt(value.target.value))}
                               className={`h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1`}/>
                    </div>
                </div>
                <div className={`h-fit w-full flex flex-col rounded space-y-1 my-2`}>
                    <div className={""}>Generated schedule</div>
                    <div className={"bg-gray-200"}>
                        <div
                            className={"h-8 w-full grid grid-cols-7 text-white font-bold text-center content-center bg-blue-600 rounded-t"}>
                            <div>Mon</div>
                            <div>Tue</div>
                            <div>Wen</div>
                            <div>Thu</div>
                            <div>Fri</div>
                            <div>Sat</div>
                            <div>Sun</div>
                        </div>
                        <ul className={"w-full"}>
                            { scheduleWeeks && scheduleWeeks.map(week =>
                                <li className={"w-full"}>
                                    <div
                                        className={"h-fit w-full rounded bg-gray-200 border-2 border-gray-200 grid grid-cols-7 gap-1 relative"}>
                                        <div className={"bg-gray-400 rounded"}>
                                            <div className={"w-full h-6 text-wrap text-center rounded truncate"}>
                                                {week.monday.startDateTime?.toLocaleDateString()}
                                            </div>
                                            <ScheduleCreatorPatternTile weeks={scheduleWeeks} setter={setScheduleWeeks}
                                                                        weekKey={week.key} dayKey={week.monday.key}/>
                                        </div>
                                        <div className={"bg-gray-400 rounded"}>
                                            <div
                                                className={"w-full h-6 text-wrap text-center rounded bg-gray-400 overflow-hidden"}>
                                                {week.tuesday.startDateTime?.toLocaleDateString()}
                                            </div>
                                            <ScheduleCreatorPatternTile weeks={scheduleWeeks} setter={setScheduleWeeks}
                                                                        weekKey={week.key} dayKey={week.tuesday.key}/>
                                        </div>
                                        <div className={"bg-gray-400 rounded"}>
                                            <div
                                                className={"w-full h-6 text-wrap text-center rounded bg-gray-400 overflow-hidden"}>
                                                {week.wednesday.startDateTime?.toLocaleDateString()}
                                            </div>
                                            <ScheduleCreatorPatternTile weeks={scheduleWeeks} setter={setScheduleWeeks}
                                                                        weekKey={week.key} dayKey={week.wednesday.key}/>
                                        </div>
                                        <div className={"bg-gray-400 rounded"}>
                                            <div
                                                className={"w-full h-6 text-wrap text-center rounded bg-gray-400 overflow-hidden"}>
                                                {week.thursday.startDateTime?.toLocaleDateString()}
                                            </div>
                                            <ScheduleCreatorPatternTile weeks={scheduleWeeks} setter={setScheduleWeeks}
                                                                        weekKey={week.key} dayKey={week.thursday.key}/>
                                        </div>
                                        <div className={"bg-gray-400 rounded"}>
                                            <div
                                                className={"w-full h-6 text-wrap text-center rounded bg-gray-400 overflow-hidden"}>
                                                {week.friday.startDateTime?.toLocaleDateString()}
                                            </div>
                                            <ScheduleCreatorPatternTile weeks={scheduleWeeks} setter={setScheduleWeeks}
                                                                        weekKey={week.key} dayKey={week.friday.key}/>
                                        </div>
                                            {workingSaturdays
                                                ? <div className={"bg-gray-400 rounded"}>
                                                    <div
                                                        className={"w-full h-6 text-wrap text-center rounded bg-gray-400 overflow-hidden"}>
                                                        {week.saturday.startDateTime?.toLocaleDateString()}
                                                    </div>
                                                    <ScheduleCreatorPatternTile weeks={scheduleWeeks}
                                                                                setter={setScheduleWeeks}
                                                                                weekKey={week.key}
                                                                                dayKey={week.saturday.key}/>
                                                </div>
                                                : <div
                                                    className={"h-full w-full aspect-square content-center text-center text-wrap text-gray-400 font-bold"}>Disabled</div>}
                                        {workingSundays
                                                ? <div className={"bg-gray-400 rounded"}>
                                                <div
                                                    className={"w-full h-6 text-wrap text-center rounded bg-gray-400 overflow-hidden"}>
                                                    {week.monday.startDateTime?.toLocaleDateString()}
                                                </div>
                                                <ScheduleCreatorPatternTile weeks={scheduleWeeks}
                                                                            setter={setScheduleWeeks}
                                                                            weekKey={week.key}
                                                                            dayKey={week.sunday.key}/>
                                            </div>
                                            : <div
                                                className={"h-full w-full aspect-square content-center text-center text-wrap text-gray-400 font-bold"}>Disabled</div>}
                                    </div>
                                </li>
                                )}
                        </ul>
                    </div>
                </div>
                <div className={"h-fit w-full py-2"}>
                    <button type={"submit"}
                            form={"schedule-form"}
                            className={"h-10 w-full bg-indigo-500 rounded text-center text-white font-bold hover:bg-indigo-600 transition-all duration-200"}>Create</button>
                </div>
                {errors.root && <div className={"h-fit w-full py-2 text-center text-red-500"}>{errors.root.message}</div>}
                {isSubmitSuccessful &&
                    <div className={"h-fit w-full py-2 text-center text-green-500"}>Schedule created successfully</div>}
            </div>
        </div>
    );
}