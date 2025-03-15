import {LoginApiContext, useCurrentSessionContext, UserApiContext} from "~/context/context";
import {useContext, useEffect, useState} from "react";
import type {SimplifiedUser} from "~/api/user-api";
import {useNavigate} from "react-router";

export interface Props {

}

export default function DashboardHeader(props: Props) {
    const session = useCurrentSessionContext();
    const [user, setUser] = useState<SimplifiedUser | undefined>(undefined);
    const userApi = useContext(UserApiContext);
    const loginApi = useContext(LoginApiContext);
    const navigate = useNavigate();

    const fetchUser = async () => {
        if (!session.session) {
            throw new Error("Session is undefined");
        }
        const username = session.session.username;
        const response = await userApi.getSimplifiedUsers({
            like: username,
            pageNumber: 0,
            pageSize: 100
        });
        if (!response.raw.ok) {
            throw new Error("Something went wrong while fetching the user");
        }
        const result = response.body.users.find(u => u.username === username);
        if (!result) {
            throw new Error("User not found");
        }
        setUser(result);
    }

    const logout = async () => {
        const response = await loginApi.logout();
        if (!response.ok) {
            throw new Error("Something went wrong while logout");
        }
        navigate("/login");
    }

    useEffect(() => {
        if (user) {
            return;
        }
        fetchUser();
    }, []);

    return (
        <div className={"flex flex-row-reverse w-full h-16 border-b-2 border-gray-200 items-center"}>
            {user &&
                <div className={"h-full w-fit content-center px-5"}>
                    <div>Account:
                        <span className={"font-bold px-2"}>{user.firstName}{user.middleName ? " " + user.middleName + " " : " "}{user.lastName} ({user.username}){session.session && `(${session.session.roles[0]})`}</span>
                        <button className={"text-red-500 hover:text-red-600 hover:font-bold transition-all duration-200"}
                                onClick={logout}>Logout</button>
                    </div>
                </div>}
        </div>
    );
}
