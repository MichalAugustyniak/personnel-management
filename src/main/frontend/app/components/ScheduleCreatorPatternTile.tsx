import {compareDateRanges, Day, type Week, type WorkBreak} from "~/pages/ScheduleCreationPage";
import React, {type FocusEvent, useContext, useEffect, useState} from "react";
import type {ShiftType} from "~/api/shift-type-api";
import {ShiftTypeContext} from "~/context/context";
import {useForm} from "react-hook-form";
import {useNavigate} from "react-router";


interface Props {
    weeks: Week[];
    setter: React.Dispatch<React.SetStateAction<Week[]>>;
    weekKey: number;
    dayKey: number;
}

type FormData = {
    isPaid: "0" | "1";
    start: string;
    end: string;
}

export default function ScheduleCreatorPatternTile({weeks, setter, weekKey, dayKey}: Props) {
    const [isFocused, setIsFocused] = useState(false);
    const [shiftTypes, setShiftTypes] = useState<ShiftType[] | undefined>(undefined);
    const [currentPage, setCurrentPage] = useState<number | undefined>(undefined);
    const shiftTypeApi = useContext(ShiftTypeContext);
    const [hasMore, setHasMore] = useState<boolean | undefined>(undefined);
    const [totalElements, setTotalElements] = useState<number | undefined>(undefined);
    const [selectedShiftType, setSelectedShiftType] = useState<ShiftType | undefined>(undefined);
    const [tab, setTab] = useState<"shift-type" | "work-breaks">("shift-type");
    const [createMode, setCreateMode] = useState(false);
    const navigate = useNavigate();
    const [workBreaks, setWorkBreaks] = useState<WorkBreak[]>([]);
    const [day, setDay] = useState<Day | undefined>(undefined);
    const {
        register,
        handleSubmit,
        setError,
        reset,
        watch,
        formState: {isSubmitting, errors, isSubmitSuccessful}
    } = useForm<FormData>();
    const watchIsPaid = watch("isPaid");
    const watchStart = watch("start");
    const watchEnd = watch("end");

    const handleFocus = () => setIsFocused(true);
    const handleBlur = (e: FocusEvent<HTMLDivElement, Element>) => {
        // Jeśli focus zostaje na jednym z inputów, nie ustawiamy isFocused na false
        if (!e.currentTarget.contains(e.relatedTarget)) {
            setIsFocused(false);
        }
    };

    const copyWeeks = () => {
        const newWeeks: Week[] = [];
        weeks.forEach(week => newWeeks.push(week));
        return newWeeks;
    }

    const findDay = (weeks: Week[]) => {
        const currentWeek = weeks.find(week => week.key === weekKey);
        if (!currentWeek) {
            throw new Error(`Cannot find week of key ${weekKey}`);
        }
        let currentDay: Day | undefined = undefined;
        switch (dayKey) {
            case currentWeek.monday.key: {
                currentDay = currentWeek.monday;
                break;
            }
            case currentWeek.tuesday.key: {
                currentDay = currentWeek.tuesday;
                break;
            }
            case currentWeek.wednesday.key: {
                currentDay = currentWeek.wednesday;
                break;
            }
            case currentWeek.thursday.key: {
                currentDay = currentWeek.thursday;
                break;
            }
            case currentWeek.friday.key: {
                currentDay = currentWeek.friday;
                break;
            }
            case currentWeek.saturday.key: {
                currentDay = currentWeek.saturday;
                break;
            }
            case currentWeek.sunday.key: {
                currentDay = currentWeek.sunday;
                break;
            }
        }
        if (!currentDay) {
            throw new Error(`Cannot find day of key ${dayKey}`);
        }
        return currentDay;
        /*
        return new Day(
            currentDay.key,
            currentDay.startDateTime,
            currentDay.endDateTime,
            currentDay.shiftType,
            currentDay.workBreaks
        );

         */
    }

    const handleUpdateDay = () => {
        const weeksCopy = copyWeeks();
        const day = findDay(weeksCopy);
        setter(weeksCopy);
    }

    const onSubmit = (formData: FormData) => {
        //console.log(formData);
        const [startHours, startMinutes] = formData.start.split(":").map(Number);
        const [endHours, endMinutes] = formData.end.split(":").map(Number);
        const startDateTime = new Date();
        startDateTime.setMonth(0);
        startDateTime.setFullYear(2025);
        startDateTime.setDate(1);
        startDateTime.setHours(startHours);
        startDateTime.setMinutes(startMinutes);
        startDateTime.setSeconds(0);
        const endDateTime = new Date();
        endDateTime.setMonth(0);
        endDateTime.setFullYear(2025);
        endDateTime.setDate(1);
        endDateTime.setHours(endHours);
        endDateTime.setMinutes(endMinutes);
        endDateTime.setSeconds(0);
        if (startDateTime > endDateTime) {
            //console.log("nastepny dzien");
            endDateTime.setDate(endDateTime.getDate() + 1);
        }
        for (const wb of workBreaks) {
            try {
                compareDateRanges({start: startDateTime, end: endDateTime}, {start: wb.startDateTime, end: wb.endDateTime});
            } catch (error) {
                const message = "Work break collides with other work break";
                setError("root", {
                    message: message
                });
                throw new Error(message);
            }
        }
        const workBreak: WorkBreak = {
            key: Math.random() * 1000,
            startDateTime: startDateTime,
            endDateTime: endDateTime,
            isPaid: formData.isPaid === "1"
        };
        reset();
        setCreateMode(false);
        //console.log(workBreak);
        setWorkBreaks((workBreaks) => [...workBreaks, workBreak]);
    }

    /*
    useEffect(() => {
        console.log(`isSubmitSuccessful: ${isSubmitSuccessful}`);
        if (!isSubmitSuccessful) {
            return;
        }
        setCreateMode(false);
    }, [isSubmitSuccessful]);

     */

    const save = () => {
        //console.log("Shift type:");
        //console.log(selectedShiftType);
        //console.log("Work breaks:");
        //console.log(workBreaks);
        //console.log("Day:");
        //console.log(day);
        //console.log("Copying the weeks...");
        if (!selectedShiftType) {
            setTab("shift-type");
            return;
        }
        const copiedWeeks = copyWeeks();
        const foundDay = findDay(copiedWeeks);
        foundDay.shiftType = selectedShiftType;
        foundDay.workBreaks = [...workBreaks];
        //console.log(copiedWeeks);
        setter(copyWeeks);
    }

    const resetDay = () => {
        const copiedWeeks = copyWeeks();
        const foundDay = findDay(copiedWeeks);
        setSelectedShiftType(undefined);
        setWorkBreaks([]);
        foundDay.shiftType = undefined;
        foundDay.workBreaks = [];
        setDay(foundDay);
        //console.log(copiedWeeks);
        setter(copyWeeks);
    }


    useEffect(() => {
        const currentDay = findDay(weeks);
        setDay(currentDay);
        setWorkBreaks([...currentDay.workBreaks]);
        console.log(`currentDay.shiftType: ${currentDay.shiftType?.name}`);
        setSelectedShiftType(currentDay.shiftType);
    }, [weeks]);


    const fetchShiftTypes = async (pageNumber: number) => {
        const response = await shiftTypeApi.getShiftTypes({
            pageNumber: pageNumber,
            pageSize: 50
        });
        if (!response.raw.ok) {
            const message = "Something went wrong while fetching the shift types";
            setError("root", {
                message: message
            });
            throw new Error(message);
        }
        if (response.body.totalPages - 1 <= pageNumber) {
            setHasMore(false)
        } else {
            setHasMore(true);
        }
        const nextShiftTypes = shiftTypes || [];
        nextShiftTypes.push(...response.body.content);
        setShiftTypes(nextShiftTypes);
        setTotalElements(response.body.totalElements);
    }

    useEffect(() => {
        let pageNumber = currentPage || 0;
        fetchShiftTypes(pageNumber);
    }, [currentPage]);


    return (
        <div className={`outline-none ${(day && day.shiftType) ? "bg-indigo-500" : "bg-gray-500"} rounded`}
             tabIndex={0}
             onFocus={handleFocus}
             onBlur={handleBlur}>
            <div className={"w-full aspect-square content-center text-center text-wrap text-white font-bold cursor-pointer"}>
                { selectedShiftType &&
                    <div>
                        <div>{day ? day.shiftType?.name : "undef"}</div>
                        <div>{day ? workBreaks.length! : "undef"}</div>
                    </div>}
            </div>
            <div
                className={"h-96 w-full bg-gradient-to-bl from-blue-400 to-indigo-300 shadow-2xl rounded-xl border-2 border-gray-300 absolute bottom-full"}
                hidden={!isFocused}>
                <div className={"h-full w-full flex flex-row"}>
                    <div className={"h-full grow rounded"}>
                        {tab === "shift-type" &&
                            <div className={"h-full w-full flex flex-col"}>
                                {!selectedShiftType &&
                                    <div className={"h-1/6 text-center text-white font-bold content-center"}>Select
                                        shift type</div>}
                                {shiftTypes && !selectedShiftType &&
                                    <div className={"h-7 w-full flex flex-row"}>
                                        <div className={"w-8"}></div>
                                        <div className={"h-7 grow grid grid-cols-3 text-center"}>
                                            <div className={"text-white font-bold"}>
                                                NAME
                                            </div>
                                            <div className={"text-white font-bold"}>
                                                START
                                            </div>
                                            <div className={"text-white font-bold"}>
                                                END
                                            </div>
                                        </div>
                                    </div>}
                                {shiftTypes && !selectedShiftType &&
                                    <ul className={"overflow-auto"}>
                                        {shiftTypes.map(shiftType =>
                                            <li>
                                                <div className={"h-8 w-full flex flex-row"}>
                                                    <div
                                                        onClick={() => navigate(`?tab=shift-types&shift-type=${shiftType.uuid}`)}
                                                        className={"size-8 p-1 cursor-pointer"}>
                                                        <img src={"info-icon.png"} alt={"info"}/>
                                                    </div>
                                                    <div
                                                        onClick={() => setSelectedShiftType(shiftType)}
                                                        className={"h-8 grow grid grid-cols-3 text-center cursor-pointer hover:bg-indigo-400 rounded content-center"}>
                                                        <div className={"text-white font-bold"}>
                                                            {shiftType.name}
                                                        </div>
                                                        <div className={"text-white font-bold"}>
                                                            {shiftType.startTime}
                                                        </div>
                                                        <div className={"text-white font-bold"}>
                                                            {shiftType.endTime}
                                                        </div>
                                                    </div>
                                                </div>
                                            </li>
                                        )}
                                        {hasMore &&
                                            <button onClick={() => setCurrentPage(currentPage ? currentPage + 1 : 0)}
                                                    className={"h-7 w-full text-center text-white font-bold bg-indigo-500 rounded my-2"}>more...</button>}
                                    </ul>
                                }
                                { selectedShiftType &&
                                    <div className={"h-full w-full flex flex-col"}>
                                        <div
                                            className={"h-1/6 flex flex-row justify-center space-x-2 text-center text-white font-bold items-center"}>
                                            <div>Selected shift type</div>
                                            <div onClick={() => setSelectedShiftType(undefined)}
                                                    className="flex items-center justify-center w-6 h-6 bg-red-500 cursor-pointer text-white rounded-full hover:bg-red-600 focus:outline-none">
                                                <span className="text-xl">X</span>
                                            </div>
                                        </div>
                                        <div className={"h-fit justify-center"}>
                                            <div className={"h-8 w-full flex flex-row"}>
                                                <div
                                                    onClick={() => navigate(`?tab=shift-types&shift-type=${selectedShiftType.uuid}`)}
                                                    className={"size-8 p-1 cursor-pointer"}>
                                                    <img src={"info-icon.png"} alt={"info"}/>
                                                </div>
                                                <div
                                                    className={"h-8 grow grid grid-cols-3 text-center hover:bg-indigo-400 rounded content-center"}>
                                                    <div className={"text-white font-bold"}>
                                                        {selectedShiftType.name}
                                                    </div>
                                                    <div className={"text-white font-bold"}>
                                                        {selectedShiftType.startTime}
                                                    </div>
                                                    <div className={"text-white font-bold"}>
                                                        {selectedShiftType.endTime}
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>}
                            </div>
                        }
                        {tab === "work-breaks" && !createMode &&
                            <div className={"h-full w-full flex flex-col"}>
                                <div className={"h-1/6 w-full text-white text-center font-bold items-center justify-center flex flex-row"}>
                                    <div>Work breaks</div>
                                    <img onClick={() => setCreateMode(true)}
                                        className={"size-6 ml-2 cursor-pointer"} src={"add-icon.png"} alt={"+"}/>
                                </div>
                                <div className={"h-7 w-full grid grid-cols-3 text-center text-white font-bold"}>
                                    <div>PAID</div>
                                    <div>START</div>
                                    <div>END</div>
                                </div>
                                {workBreaks.length > 0 ?
                                    <ul className={"h-5/6 w-full overflow-auto"}>
                                        {workBreaks.map(workBreak =>
                                            <li className={"h-7 w-full grid grid-cols-3 text-center text-white font-bold"}>
                                                <div>{workBreak.isPaid ? "yes" : "no"}</div>
                                                <div>{workBreak.startDateTime.toLocaleTimeString()}</div>
                                                <div>{workBreak.endDateTime.toLocaleTimeString()}</div>
                                            </li>
                                        )}
                                    </ul>
                                    : <div className={"text-center text-white font-bold"}>Empty list</div>}
                            </div>
                        }
                        {tab === "work-breaks" && createMode &&
                            <div className={"h-full w-full flex flex-col items-center"}>
                                <div
                                    className={"h-1/6 flex flex-row justify-center space-x-2 text-center text-white font-bold items-center"}>
                                    <div>New work break</div>
                                    <div onClick={() => setCreateMode(false)}
                                         className="flex items-center justify-center w-6 h-6 bg-red-500 cursor-pointer text-white rounded-full hover:bg-red-600 focus:outline-none">
                                        <span className="text-xl">X</span>
                                    </div>
                                </div>
                                <form onSubmit={handleSubmit(onSubmit)}
                                      className={"h-fit w-56 flex flex-col py-5 space-y-2"}>
                                    <div className={`h-fit w-full flex flex-col rounded space-y-1`}>
                                        <div className={"text-white font-bold"}>START</div>
                                        <input type={"time"} {...register("start", {
                                            required: "Start time is required"
                                        })}
                                               className={`h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1`}/>
                                        {errors.start && <div className={"text-red-500"}>{errors.start.message}</div>}
                                    </div>
                                    <div className={`h-fit w-full flex flex-col rounded space-y-1`}>
                                        <div className={"text-white font-bold"}>END</div>
                                        <input type={"time"} {...register("end", {
                                            required: "End time is required"
                                        })}
                                               className={`h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1`}/>
                                        {errors.end && <div className={"text-red-500"}>{errors.end.message}</div>}
                                    </div>
                                    <div className={`h-fit w-full flex flex-col rounded space-y-1`}>
                                        <div className={"text-white font-bold"}>PAID</div>
                                        <select {...register("isPaid", {
                                            required: "Paid status is required"
                                        })}
                                                className={`h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1`}>
                                            <option value={"0"}>no</option>
                                            <option value={"1"}>yes</option>
                                        </select>
                                        {errors.isPaid && <div className={"text-red-500"}>{errors.isPaid.message}</div>}
                                    </div>
                                    <button type={"submit"}
                                            className={"w-full h-10 rounded bg-indigo-500 hover:bg-indigo-600"}>Save
                                    </button>
                                    {errors.root && <div className={"text-center text-red-500"}>{errors.root.message}</div>}
                                </form>
                            </div>
                        }
                    </div>
                    <div className={"h-full w-fit rounded border-l-2 border-gray-500"}>
                        <ul className={"h-full flex flex-col justify-around px-3"}>
                            <li>
                                <div onClick={() => {
                                    setTab("shift-type");
                                    setCurrentPage(undefined);
                                    setCreateMode(false);
                                }}
                                     className={`p-2 text-white text-center font-bold ${tab === "shift-type" && "bg-gradient-to-tr from-blue-500 to-indigo-500"} cursor-pointer rounded transition-all duration-500`}>Shift
                                    type
                                </div>
                            </li>
                            <li>
                                <div onClick={() => {
                                    setTab("work-breaks");
                                    setCurrentPage(undefined);
                                    setCreateMode(false);
                                }}
                                     className={`p-2 text-white text-center font-bold ${tab === "work-breaks" && "bg-gradient-to-tr from-blue-500 to-indigo-500"} cursor-pointer rounded transition-all duration-500`}>Work
                                    breaks
                                </div>
                            </li>
                            <li>
                                <div onClick={() => {
                                    if (!selectedShiftType) {
                                        throw new Error("Shift type not selected");
                                    }
                                    setCreateMode(false);
                                    setCurrentPage(undefined);
                                    setCreateMode(false);
                                    save();
                                    setIsFocused(false);
                                }}
                                     className={`p-2 ${selectedShiftType ? "text-white cursor-pointer" : "text-gray-600"} text-center font-bold rounded transition-all duration-500`}>Save
                                </div>
                            </li>
                            <li>
                                <div onClick={() => {
                                    setCreateMode(false);
                                    setCurrentPage(undefined);
                                    setCreateMode(false);
                                    resetDay();
                                    setIsFocused(false);
                                }}
                                     className={`p-2 text-white text-center font-bold cursor-pointer rounded transition-all duration-200 hover:bg-red-500`}>Reset
                                </div>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    );
}