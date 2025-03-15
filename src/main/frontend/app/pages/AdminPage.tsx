import DashboardBase, {type OptionContainer} from "~/components/DashboardBase";
import UserCreationPage from "~/pages/UserCreationPage";
import UsersViewPage from "~/pages/UsersViewPage";
import TasksViewPage from "~/pages/TasksViewPage";
import TaskCreationPage from "~/pages/TaskCreationPage";
import TaskEventCreationPage from "~/pages/TaskEventCreationPage";
import TaskEventViewPage from "~/pages/TaskEventViewPage";
import ScheduleCreationPage from "~/pages/ScheduleCreationPage";
import ShiftTypeCreationPage from "~/pages/ShiftTypeCreationPage";
import ShiftTypeViewPage from "~/pages/ShiftTypeViewPage";
import AttendanceStatusCreationPage from "~/pages/AttendanceStatusCreationPage";
import AttendanceStatusViewPage from "~/pages/AttendanceStatusViewPage";
import ScheduleViewPage from "~/pages/ScheduleViewPage";
import AttendanceCreationPage from "~/pages/AttendanceCreationPage";
import AttendanceViewPage from "~/pages/AttendanceViewPage";
import LogoUploadPage from "~/pages/LogoUploadPage";
import {useContext, useEffect, useState} from "react";
import {LogoApiContext} from "~/context/context";
import UserCreationPageV2 from "~/pages/UserCreationPageV2";

const homeTab = <div className={"h-full w-full content-center text-center"}>
    <div className={"text-2xl"}>Welcome to the Admin Dashboard</div>
</div>;

const optionContainers: OptionContainer[] = [
    {
        name: "User management",
        options: [
            {
                iconURL: "users.png",
                label: "All users",
                tab: {
                    name: "users",
                    content: <UsersViewPage/>
                }
            },
            {
                iconURL: "user-add.png",
                label: "Add user",
                tab: {
                    name: "add-user",
                    content: <UserCreationPageV2/>
                }
            },
        ]
    },
    {
        name: "Schedule management",
        options: [
            {
                iconURL: "calendar.png",
                label: "All schedules",
                tab: {
                    name: "schedules",
                    content: <ScheduleViewPage/>
                }
            },
            {
                iconURL: "add-circle.png",
                label: "New schedule",
                tab: {
                    name: "new-schedule",
                    content: <ScheduleCreationPage/>
                }
            },
            {
                iconURL: "calendar-clock.png",
                label: "All shift types",
                tab: {
                    name: "shift-types",
                    content: <ShiftTypeViewPage/>
                }
            },
            {
                iconURL: "add-circle.png",
                label: "New shift type",
                tab: {
                    name: "new-shift-type",
                    content: <ShiftTypeCreationPage/>
                }
            },
        ]
    },
    {
        name: "Task management",
        options: [
            {
                iconURL: "task.png",
                label: "All tasks",
                tab: {
                    name: "tasks",
                    content: <TasksViewPage/>
                }
            },
            {
                iconURL: "add-circle.png",
                label: "New task",
                tab: {
                    name: "new-task",
                    content: <TaskCreationPage/>
                }
            },
        ]
    },
    {
        name: "Task event management",
        options: [
            {
                iconURL: "task2.png",
                label: "All task events",
                tab: {
                    name: "task-events",
                    content: <TaskEventViewPage/>
                }
            },
            {
                iconURL: "add-circle.png",
                label: "New task event",
                tab: {
                    name: "new-task-event",
                    content: <TaskEventCreationPage/>
                }
            },
        ]
    },
    {
        name: "Attendance management",
        options: [
            {
                iconURL: "attendance.png",
                label: "All attendances",
                tab: {
                    name: "attendances",
                    content: <AttendanceViewPage/>
                }
            },
            {
                iconURL: "add-circle.png",
                label: "New attendance",
                tab: {
                    name: "new-attendance",
                    content: <AttendanceCreationPage/>
                }
            },
            {
                iconURL: "attendance.png",
                label: "All statuses",
                tab: {
                    name: "attendance-statuses",
                    content: <AttendanceStatusViewPage/>
                }
            },
            {
                iconURL: "add-circle.png",
                label: "New status",
                tab: {
                    name: "new-attendance-status",
                    content: <AttendanceStatusCreationPage/>
                }
            },

        ]
    },

    {
        name: "Settings",
        options: [
            {
                iconURL: "upload.png",
                label: "Upload logo",
                tab: {
                    name: "upload-logo",
                    content: <LogoUploadPage/>
                }
            },
        ]
    },
];

export default function AdminPage() {
    const logoApi = useContext(LogoApiContext);
    const [logo, setLogo] = useState("");

    const fetchLogo = async () => {
        const response = await logoApi.getLogo();
        if (!response.raw.ok) {
            throw new Error("Something went wrong while fetching the logo");
        }
        console.log(logoApi.getHost() + response.body.propertyValue);
        setLogo(logoApi.getHost() + response.body.propertyValue);
    }

    useEffect(() => {
        fetchLogo();
    }, []);

    return <DashboardBase logoURL={logo} homeTab={homeTab} optionContainers={optionContainers}/>
}