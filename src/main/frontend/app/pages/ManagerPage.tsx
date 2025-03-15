import DashboardBase, {type OptionContainer} from "~/components/DashboardBase";
import UserSchedulePage from "~/pages/UserSchedulePage";
import AttendanceViewPage from "~/pages/AttendanceViewPage";
import AttendanceCreationPage from "~/pages/AttendanceCreationPage";
import UserProfilePage from "~/pages/UserProfilePage";
import ChangePasswordPage from "~/pages/ChangePasswordPage";
import TasksViewPage from "~/pages/TasksViewPage";
import TaskCreationPage from "~/pages/TaskCreationPage";
import TaskEventViewPage from "~/pages/TaskEventViewPage";
import TaskEventCreationPage from "~/pages/TaskEventCreationPage";
import {useContext, useEffect, useState} from "react";
import {LogoApiContext} from "~/context/context";

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
                    name: "my-schedule",
                    content: <UserSchedulePage/>
                }
            },
        ]
    },
    {
        name: "Attendances",
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
        ]
    },
    {
        name: "Tasks",
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
        name: "Task events",
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

];

const homeTab = <div className={"h-full w-full content-center text-center"}>
    <div className={"text-2xl"}>Welcome to the Manager Dashboard</div>
</div>

export default function ManagerPage() {

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

    return (
        <DashboardBase logoURL={logo} homeTab={homeTab} optionContainers={optionContainers}></DashboardBase>
    )
}
