import {useLocation, useNavigate} from "react-router";
import {useContext, useEffect, useState} from "react";
import {ScheduleApiContext, TaskApiContext, TaskEventApiContext, UserApiContext} from "~/context/context";
import type {Task} from "~/api/task-api";
import {useForm} from "react-hook-form";
import type {TaskEvent} from "~/api/task-event-api";
import type {Schedule} from "~/api/schedule-api";
import UserSchedulePage from "~/pages/UserSchedulePage";

type FormData = {
    name: string;
    description: string;
    /*
    maxWorkingHoursPerDay?: number;
    enableHolidayAssignments: "0" | "1";
    enableWorkingSaturdays: "0" | "1";
    enableWorkingSundays: "0" | "1";

     */
}

export default function ScheduleViewPage() {
    const navigate = useNavigate();

    const scheduleApi = useContext(ScheduleApiContext);
    const userApi = useContext(UserApiContext);
    const [hasMoreSchedules, setHasMoreSchedules] = useState<boolean | undefined>(undefined);
    const [scheduleList, setScheduleList] = useState<Schedule[] | undefined>(undefined);
    const { register, handleSubmit, setError, watch, formState: { isSubmitSuccessful, errors, isSubmitting } } = useForm<FormData>();
    const [currentPage, setCurrentPage] = useState<number | undefined>(undefined);
    const [schedule, setSchedule] = useState<Schedule | undefined>(undefined);
    const location = useLocation();
    const [editMode, setEditMode] = useState(false);
    const [user, setUser] = useState<string | undefined>(undefined);
    const [userErrorMessage, setUserErrorMessage] = useState<string | undefined>(undefined);
    const [isUserSuccessful, setIsUserSuccessful] = useState(false);
    const [scheduleDeleted, setScheduleDeleted] = useState<boolean | undefined>(undefined);
    //const nameLikeWatch = useWatch("nameLike");

    useEffect(() => {
        const checkTaskParam = async () => {
            console.log("checking the schedule param...");
            setScheduleList(undefined);
            const params = new URLSearchParams(location.search);
            const schedule = params.get("schedule");
            console.log("schedule param = " + schedule);
            if (schedule) {
                const response = await scheduleApi.getSchedule(schedule);
                if (!response.raw.ok) {
                    throw new Error("schedule param exists but something went wrong while fetching the schedule");
                }
                setSchedule(response.body);
                return true;
            }
            return false;
        }
        const fetchTasks = async () => {
            if (currentPage !== undefined || schedule) {
                return;
            }
            const response = await scheduleApi.getSchedules({pageSize: 50, pageNumber: 0});
            if (!response.raw.ok) {
                throw new Error("Something went wrong while fetching schedule");
            }
            if (response.body.totalPages > 1) {
                setHasMoreSchedules(true);
            } else {
                setHasMoreSchedules(false);
            }
            setScheduleList([...response.body.content]);
            setCurrentPage(0);
        };
        console.log("location changed");
        const init = async () => {
            if (await checkTaskParam()) {
                return;
            }
            await fetchTasks();
        }
        init();
    }, [currentPage, location]);

    const onSubmit = async (formData: FormData) => {
        const response = await scheduleApi.updateSchedule(schedule?.uuid!, {
            name: formData.name ? formData.name.trim() : undefined,
            description: formData.description ? formData.description.trim() : undefined,
            /*
            maxWorkingHoursPerDay: formData.maxWorkingHoursPerDay,
            enableHolidayAssignments: formData.enableHolidayAssignments === "1",
            enableWorkingSaturdays: formData.enableWorkingSaturdays === "1",
            enableWorkingSundays: formData.enableWorkingSundays === "1"

             */
        });
        if (!response.ok) {
            setError("root", {
                message: "Something went wrong white editing the schedule"
            });
            throw new Error("Something went wrong while editing the schedule");
        }
    }

    const redirectToCreationPage = () => {
        const params = new URLSearchParams(location.search);
        const url = location.pathname + "?tab=new-schedule";
        console.log(`Navigating to: ${url}...`);
        navigate("?tab=new-schedule");
    }

    const handleAddUser = async () => {
        //const response = await scheduleApi.
    }

    const deleteSchedule = async () => {
        if (!schedule) {
            throw new Error("Cannot delete the schedule: the schedule is undefined");
        }
        const response = await scheduleApi.deleteSchedule(schedule.uuid);
        if (!response.ok) {
            throw new Error("Something went wrong while deleting the schedule");
        }
        setScheduleDeleted(true);
    }

    return (
        <>
        <div className={"h-full w-full flex flex-col justify-center items-center"}>
            <div className={"h-full w-fit p-5 flex flex-col"}>
                <div className={"h-1/6 w-full flex flex-col space-y-3 mb-3 justify-center"}>
                    {scheduleList
                        && <div>
                            <div className={"text-2xl"}>Schedules</div>
                            <div className={"text-md"}>View all schedules</div>
                        </div>}

                    {scheduleList
                        && <div className={"h-fit w-full flex flex-row-reverse"}>
                            <button
                                onClick={() => redirectToCreationPage()}
                                className={"bg-indigo-400 rounded p-2 cursor-pointer hover:bg-indigo-500 text-white font-bold"}
                            >Create new
                            </button>
                        </div>}
                    {!scheduleList && schedule
                        && <div>
                            <div className={"text-2xl"}>Schedule info</div>
                            <div className={"text-md"}>Here you can inspect the schedule</div>
                        </div>}
                </div>
                <div className={"h-5/6 w-full"}>
                    <div className={"h-full w-fit flex flex-col"}>
                        {scheduleList
                            && <div className={"h-fit w-fit flex flex-col"}>
                                <div className={"h-fit w-full bg-blue-600 flex flex-row font-bold text-white rounded-t"}>
                                    <div className={"h-10 w-[576px] text-center flex flex-row items-center justify-center"}>
                                        NAME
                                    </div>
                                    <div className={"h-10 w-[576px] text-center flex flex-row items-center justify-center"}>
                                        DESCRIPTION
                                    </div>

                                </div>
                                <div className={"w-full bg-gray-100"}>
                                    {scheduleList && scheduleList.length > 0
                                        ? <ul className={"h-fit w-full flex flex-col"}>
                                            {scheduleList.map((schedule, key) => <li key={key}
                                                                             onClick={() => navigate("?tab=schedules&schedule=" + schedule.uuid)}
                                            >
                                                <div className={"h-16 w-full flex flex-row hover:bg-blue-100 cursor-pointer"}>
                                                    <div
                                                        className={"h-full w-[576px] text-center flex flex-row items-center justify-center text-wrap"}>
                                                        {schedule.name}
                                                    </div>
                                                    <div
                                                        className={"h-full w-[576px] text-center flex flex-row items-center justify-center text-wrap"}>
                                                        {schedule.description}
                                                    </div>
                                                </div>
                                            </li>)}
                                        </ul>
                                        : <div className={"h-fit w-full text-center"}>Empty list</div>}
                                    <div className={"h-fit w-full py-2"}>
                                        {hasMoreSchedules
                                            ? <button
                                                type={"submit"}
                                                className={"h-10 w-full bg-indigo-500 rounded"}
                                                disabled={!hasMoreSchedules}
                                            >
                                                more...
                                            </button>
                                            : null}
                                    </div>
                                </div>
                            </div> }
                        { !scheduleList && schedule && !editMode
                            && <div>
                                <div className={"h-fit w-full flex flex-row justify-between mb-2"}>
                                    <button
                                        className={"h-10 w-20 rounded bg-red-500 hover:bg-red-600 text-white font-bold"}
                                        onClick={() => {
                                            deleteSchedule();
                                            console.log("przekierowywanie");
                                            //navigate(0);
                                        }}>
                                        Delete
                                    </button>
                                    <button
                                        className={"h-10 w-20 rounded bg-indigo-500 hover:bg-indigo-600 text-white font-bold"}
                                        onClick={() => setEditMode(true)}>
                                        Edit
                                    </button>
                                </div>
                                {scheduleDeleted && <div className={"text-green-500 w-full text-center content-center"}>Schedule delete successfully</div>}
                                <div className={"h-fit w-96 flex flex-col space-y-2"}>
                                    <div className={"h-fit w-full"}>
                                        <div>
                                            Name
                                        </div>
                                        <div className={"h-10 w-full bg-gray-200 rounded content-center px-1"}>
                                            {schedule.name}
                                        </div>
                                    </div>
                                    <div className={"h-fit w-full"}>
                                        <div>
                                            Description
                                        </div>
                                        <div className={"h-10 w-full bg-gray-200 rounded content-center px-1"}>
                                            {schedule.description}
                                        </div>
                                    </div>
                                    <div className={"h-fit w-full"}>
                                        <div>
                                            Holiday assignments
                                        </div>
                                        <div className={"h-10 w-full bg-gray-200 rounded content-center px-1"}>
                                            {schedule.enableHolidayAssignments ? "enables" : "disabled"}
                                        </div>
                                    </div>
                                    <div className={"h-fit w-full"}>
                                        <div>
                                            Working saturdays
                                        </div>
                                        <div className={"h-10 w-full bg-gray-200 rounded content-center px-1"}>
                                            {schedule.enableWorkingSaturdays ? "enabled" : "disabled"}
                                        </div>
                                    </div>
                                    <div className={"h-fit w-full"}>
                                        <div>
                                            Working sundays
                                        </div>
                                        <div className={"h-10 w-full bg-gray-200 rounded content-center px-1"}>
                                            {schedule.enableWorkingSundays ? "enabled" : "disabled"}
                                        </div>
                                    </div>
                                    <div className={"h-fit w-full"}>
                                        <div>
                                            Hours per day limit
                                        </div>
                                        <div className={"h-10 w-full bg-gray-200 rounded content-center px-1"}>
                                            {schedule.maxWorkingHoursPerDay}
                                        </div>
                                    </div>
                                    <div className={"h-fit w-full"}>
                                        <div>
                                            UUID
                                        </div>
                                        <div className={"h-10 w-full bg-gray-200 rounded content-center px-1"}>
                                            {schedule.uuid}
                                        </div>
                                    </div>
                                </div>
                            </div>}
                        {!scheduleList && schedule && editMode
                            && <div>
                                <div className={"h-10 w-full flex flex-row-reverse"}>
                                    <button
                                        className={"h-10 w-20 rounded bg-indigo-500 hover:bg-indigo-600 text-white font-bold"}
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
                                                if (value.trim().length < 4 || value.trim().length > 30) {
                                                    return "Name must be between 4 and 30 characters";
                                                }
                                                return true;
                                            }
                                        })}
                                               className={"h-10 w-full rounded bg-gray-200 border-2 border-gray-200 focus:bg-white transition-all duration-200 outline-none px-1"}
                                               defaultValue={schedule.name}/>
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
                                               defaultValue={schedule.description}/>
                                        { errors.description && <div className={"text-red-500"}>{errors.description.message}</div> }
                                    </div>
                                    <button type={"submit"}
                                            className={"h-10 w-full rounded bg-indigo-500 hover:bg-indigo-600 text-center text-white font-bold"}>
                                        Save
                                    </button>
                                    { errors.root && <div className={"text-red-500 text-center"}>{errors.root.message}</div> }
                                    { isSubmitSuccessful && <div className={"text-green-500 text-center"}>Task event edited successfully</div> }
                                </form>
                            </div>}
                    </div>
                </div>
            </div>
        </div>
            {schedule && <UserSchedulePage scheduleUUID={schedule.uuid}/> }
        </>
    );
}