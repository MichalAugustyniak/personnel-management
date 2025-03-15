import UsersFinder from "~/components/UserFinder";
import type {ReactNode} from "react";
import UserInfoPage from "~/pages/UserInfoPage";
import {useNavigate} from "react-router";

export default function UsersViewPage() {
    const params = new URLSearchParams(location.search);
    const uuid = params.get("uuid");
    const navigate = useNavigate();

    let content: ReactNode = undefined;

    if (uuid) {
        content = <UserInfoPage uuid={uuid}/>
    } else {
        const url = new URL(location.href);
        content = <UsersFinder onClick={(user) => {
            url.searchParams.append("uuid", user.uuid);
            navigate(url.search);
        }}/>
    }
    return (
        <div className={"h-full w-full flex flex-col items-center justify-center"}>
            {content}
        </div>
    )
}