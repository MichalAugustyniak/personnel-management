import type {User, UserApi} from "~/api/user-api";
import {useContext, useEffect, useState} from "react";
import {UserApiContext} from "~/context/context";
import EditableUserInfo from "~/components/EditableUserInfo";
import UserTasksView from "~/components/UserTasksView";
import {DefaultRoles} from "~/commons/commons";
import UserScheduleView from "~/components/UserScheduleView";

export interface Props {
    uuid: string;
}

export default function UserInfoPage({uuid}: Props) {
    const userApi: UserApi = useContext(UserApiContext);
    const [user, setUser] = useState<User | undefined>(undefined);
    const params = new URLSearchParams(location.search);
    let fetchedUUID = params.get("uuid") || uuid;



    useEffect(() => {
        const fetchUser = async () => {
            if (!fetchedUUID) {
                throw new Error("Null undefined uuid");
            }
            let response = await userApi.getUser({userUUID: fetchedUUID});
            if (response.raw.ok) {
                setUser(response.body);
            }
        }
        fetchUser();
    }, []);

    return (
        <div className={"h-full w-full p-5"}>
            <div className={"h-fit w-full text-5xl mt-7 mb-4"}>
                Profile
            </div>
            <div className={"space-y-5 py-3"}>
                <div className={"flex flex-row h-fit w-full justify-between"}>
                    <EditableUserInfo uuid={fetchedUUID}/>
                    <UserTasksView uuid={fetchedUUID}/>
                </div>
                {user && (user.role === DefaultRoles.MANAGER || user.role === DefaultRoles.EMPLOYEE) &&
                    <UserScheduleView user={user}/>
                }
            </div>
        </div>
    );
}