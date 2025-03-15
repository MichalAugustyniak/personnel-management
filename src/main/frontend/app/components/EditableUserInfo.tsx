import {type ReactNode, useContext, useEffect, useState} from "react";
import type {User, UserApi} from "~/api/user-api";
import {useCurrentSessionContext, UserApiContext} from "~/context/context";
import UserView from "~/components/UserView";
import UserInfoEditor from "~/components/UserInfoEditor";
import {DefaultRoles} from "~/commons/commons";

export interface Props {
    uuid: string;
}

export default function EditableUserInfo({ uuid }: Props) {
    const userApi: UserApi = useContext(UserApiContext);
    const session = useCurrentSessionContext();
    const [user, setUser] = useState<User | undefined>(undefined);
    const [currentTab, setCurrentTab] = useState<ReactNode | undefined>(undefined);
    const [editTab] = useState<ReactNode>(<UserInfoEditor uuid={uuid}/>);
    const [viewTab, setViewTab] = useState<ReactNode | undefined>(undefined);

    const handleTabOnClick = () => {
        if (currentTab === editTab) {
            setCurrentTab(viewTab);
        } else if (currentTab === viewTab) {
            setCurrentTab(editTab);
        }
    }

    useEffect(() => {
        const fetchUser = async () => {
            let response = await userApi.getUser({userUUID: uuid});
            if (!response.raw.ok) {
                throw new Error("Something went wrong while fetching the user");
            }
            if (!response.body) {
                throw new Error("Fetched user is undefined");
            }
            setUser(response.body);
            setViewTab(<UserView user={response.body}/>)
        }
        fetchUser();
    }, []);

    useEffect(() => {
        setCurrentTab(viewTab);
    }, [viewTab]);

    return (
        <>
            {user && viewTab
                ? <div className={"h-fit w-fit rounded bg-indigo-300 flex flex-col"}>
                    {session.session && session.session.roles.includes(DefaultRoles.ADMIN) && <div
                        className={"h-fit w-full my-1 text-center hover:text-white hover:font-bold hover:cursor-pointer"}
                        onClick={() => handleTabOnClick()}
                    >{currentTab === viewTab ? "Edit" : "Cancel"}</div>}
                    {currentTab}
                </div>
                : null}
        </>
    );
}