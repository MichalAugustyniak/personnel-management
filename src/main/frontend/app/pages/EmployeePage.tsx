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
import UserProfilePage from "~/pages/UserProfilePage";
import UserSchedulePage from "~/pages/UserSchedulePage";
import ChangePasswordPage from "~/pages/ChangePasswordPage";
import {useContext, useEffect, useState} from "react";
import {LogoApiContext} from "~/context/context";

const homeTab = <div className={"h-full w-full content-center text-center"}>
    <div className={"text-2xl"}>Welcome to the Employee Dashboard</div>
</div>;

const optionContainers: OptionContainer[] = [
    {
        name: "Profile",
        options: [
            {
                iconURL: "profile.png",
                label: "My profile",
                tab: {
                    name: "profile",
                    content: <UserProfilePage/>
                }
            },
            {
                iconURL: "password.png",
                label: "Change password",
                tab: {
                    name: "change-password",
                    content: <ChangePasswordPage/>
                }
            },
        ]
    },
    {
        name: "Schedule",
        options: [
            {
                iconURL: "calendar.png",
                label: "My schedule",
                tab: {
                    name: "schedules",
                    content: <UserSchedulePage/>
                }
            },
        ]
    },
    {
        name: "Tasks",
        options: [
            {
                iconURL: "task.png",
                label: "My tasks",
                tab: {
                    name: "tasks",
                    content: <TasksViewPage/>
                }
            },
        ]
    },
    {
        name: "Task events",
        options: [
            {
                iconURL: "task2.png",
                label: "My task events",
                tab: {
                    name: "task-events",
                    content: <TaskEventViewPage/>
                }
            },
        ]
    },
    {
        name: "Attendances",
        options: [
            {
                iconURL: "attendance.png",
                label: "My attendances",
                tab: {
                    name: "attendances",
                    content: <AttendanceViewPage/>
                }
            },
        ]
    },
];

export default function EmployeePage() {
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