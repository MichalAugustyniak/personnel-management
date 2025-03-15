import type {Sex, User, UserApi, Users, UsersRequest} from "~/api/user-api";
import {type SubmitHandler, useForm} from "react-hook-form";
import {DefaultRoles} from "~/commons/commons";
import FoundUser from "~/components/FoundUser";
import {useContext, useEffect, useState} from "react";
import {UserApiContext} from "~/context/context";

type FormFields = {
    like: string;
    role: string;
    sex: Sex | "";
};

export interface Props {
    onClick?: (user: User) => void;
}

export default function UsersFinder({onClick}: Props) {
    const userApi: UserApi = useContext(UserApiContext);
    const {register, handleSubmit, watch} = useForm<FormFields>();
    const [canLoadMore, setCanLoadMore] = useState(false);
    const [users, setUsers] = useState<User[]>([]);
    const [currentPage, setCurrentPage] = useState<number | undefined>(undefined);
    const watchLike: string = watch("like");
    const watchRole: string = watch("role");
    const watchSex: Sex | "" = watch("sex");

    useEffect(() => {
        async function getUsers(request: UsersRequest): Promise<Users> {
            let response = await userApi.getUsers(request);
            if (!response.raw.ok) {
                throw new Error("Something went wrong while fetching users");
            }
            return response.body;
        }

        async function updateUsersList() {
            const request: UsersRequest = {
                role: watchRole === "" ? undefined : watchRole,
                addressUUID: undefined,
                sex: watchSex === "" ? undefined : watchSex,
                isActive: undefined,
                like: watchLike === "" ? undefined : watchLike,
                pageNumber: currentPage === undefined ? 0 : currentPage,
                pageSize: 50
            };
            console.log(request);
            let fetchedUsers = await getUsers(request);
            setCurrentPage(0);
            let nextUsers: User[] = [...fetchedUsers.users];
            setUsers(nextUsers);
            console.log(`fetchedUsers.totalPages=${fetchedUsers.totalPages}  currentPage=${currentPage!}`);
            if (fetchedUsers.totalPages - 1 <= (currentPage !== undefined ? currentPage : 0)) {
                setCanLoadMore(false);
            } else {
                setCanLoadMore(true);
            }
        }

        updateUsersList();
    }, [watchLike, watchRole, watchSex]);


    const onSubmit: SubmitHandler<FormFields> = async (data) => {
        async function getUsers(request: UsersRequest): Promise<Users> {
            let response = await userApi.getUsers(request);
            if (!response.raw.ok) {
                throw new Error("Something went wrong while fetching users");
            }
            return response.body;
        }

        async function updateUsersList() {
            const request: UsersRequest = {
                role: data.role === "" ? undefined : data.role,
                addressUUID: undefined,
                sex: data.sex === "" ? undefined : data.sex,
                isActive: undefined,
                like: data.like === "" ? undefined : data.like,
                pageNumber: currentPage === undefined ? 0 : currentPage,
                pageSize: 50
            };
            console.log(request);
            let fetchedUsers = await getUsers(request);
            setCurrentPage(currentPage === undefined ? 0 : currentPage + 1);
            let nextUsers: User[] = users;
            nextUsers.push(...fetchedUsers.users);
            setUsers(nextUsers);
            if (fetchedUsers.totalPages === fetchedUsers.number + 1) {
                setCanLoadMore(false);
            } else {
                setCanLoadMore(true);
            }
        }

        await updateUsersList();
    }

    return (
        <div className={"h-fit w-fit flex flex-col"}>
            <div
                className={"h-fit w-full bg-gray-200 border-2 border-gray-300 rounded-t-2xl flex content-center items-center"}>
                <form id={"form1"} className={"h-fit w-fit m-3 space-x-2 flex flex-row items-center"}
                      onSubmit={handleSubmit(onSubmit)}>
                    <input defaultValue={""} className={"w-60 rounded px-2"}
                           placeholder={"name"} {...register("like")}/>
                    <select className={"rounded"} defaultValue={""} {...register("role")}>
                        <option value={""}>Any</option>
                        <option value={DefaultRoles.ADMIN}>Admin</option>
                        <option value={DefaultRoles.EMPLOYEE}>Employee</option>
                        <option value={DefaultRoles.MANAGER}>Manager</option>
                        <option value={DefaultRoles.HR}>HR</option>
                        <option value={DefaultRoles.ACCOUNTANT}>Accountant</option>
                    </select>
                    <select className={"rounded"} defaultValue={""} {...register("sex")}>
                        <option value={""}>Any</option>
                        <option value={"MALE"}>Male</option>
                        <option value={"FEMALE"}>Female</option>
                    </select>
                </form>
            </div>
            <ul className={"h-96 w-full rounded-b-2xl bg-gray-200 space-y-2 overflow-auto"}>
                {users.map((user, key) => <li key={key} onClick={() => onClick ? onClick(user) : null}
                                       className={(onClick ? "hover:cursor-pointer" : "")}><FoundUser
                    firstName={user.firstName} middleName={user.middleName ? user.middleName : ""}
                    lastName={user.lastName} username={user.username} role={user.role}
                    sex={user.sex}/></li>)}
                {canLoadMore ?
                    <button form={"form1"} type={"submit"}
                            className={"w-full h-8 bg-blue-400 hover:bg-blue-500 text-center content-center rounded"}>
                        <span>more...</span>
                    </button>
                    : null
                }
            </ul>
        </div>
    )
}