import {useCurrentSessionContext, UserApiContext} from "~/context/context";
import {useContext, useEffect, useState} from "react";
import type {SimplifiedUser} from "~/api/user-api";

export default function UserProfilePage() {
    const session = useCurrentSessionContext();
    const userApi = useContext(UserApiContext);
    const [user, setUser] = useState<SimplifiedUser | undefined>(undefined);

    const fetchUser = async () => {
        if (!session.session) {
            throw new Error("Session is undefined");
        }
        const response = await userApi.getSimplifiedUsers({
            like: session.session.username,
            pageNumber: 0,
            pageSize: 100
        });
        if (!response.raw.ok) {
            throw new Error("Something went wrong while fetching the user");
        }
        for (const fetchedUser of response.body.users) {
            if (fetchedUser.username === session.session.username) {
                setUser(fetchedUser);
            }
        }
    }

    useEffect(() => {
        fetchUser();
    }, []);

    return (
        <div className={"h-full w-full flex flex-col justify-center items-center"}>
            <div className={"h-full w-fit p-5 flex flex-col"}>
                <div className={"h-1/6 w-full flex flex-col space-y-3 mb-3 justify-center"}>
                    <div>
                        <div className={"text-2xl"}>Profile</div>
                    </div>
                </div>
                {user &&
                    <div className={"h-fit w-96 flex flex-col space-y-2"}>
                        <div className={"h-fit w-full"}>
                            <div>
                                First name
                            </div>
                            <div className={"h-10 w-full bg-gray-200 rounded content-center px-1"}>
                                {user.firstName}
                            </div>
                        </div>
                        <div className={"h-fit w-full"}>
                            <div>
                                Middle name
                            </div>
                            <div className={"h-10 w-full bg-gray-200 rounded content-center px-1"}>
                                {user.middleName}
                            </div>
                        </div>
                        <div className={"h-fit w-full"}>
                            <div>
                                Last name
                            </div>
                            <div className={"h-10 w-full bg-gray-200 rounded content-center px-1"}>
                                {user.lastName}
                            </div>
                        </div>
                        <div className={"h-fit w-full"}>
                            <div>
                                Username
                            </div>
                            <div className={"h-10 w-full bg-gray-200 rounded content-center px-1"}>
                                {user.username}
                            </div>
                        </div>
                        <div className={"h-fit w-full"}>
                            <div>
                                UUID
                            </div>
                            <div className={"h-10 w-full bg-gray-200 rounded content-center px-1"}>
                                {user.uuid}
                            </div>
                        </div>
                    </div>}
            </div>
        </div>
    );
}