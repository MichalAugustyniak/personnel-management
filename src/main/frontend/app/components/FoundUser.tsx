import type {Sex} from "~/api/user-api";

export interface Props {
    firstName: string;
    middleName: string;
    lastName: string;
    username: string;
    role: string;
    sex: Sex;
}

export default function FoundUser(props: Props) {
    return (
        <div className={"w-fit h-12 bg-white/30 backdrop-blur-sm border-2 border-white/30 rounded flex flex-row text-center text-wrap"}>
            <div className={"h-full w-96 content-center"}>
                <span>{`${props.firstName} ${props.middleName} ${props.lastName} (${props.username})`}</span>
            </div>
            <div className={"h-full w-24 content-center"}>
                <span>{props.role}</span>
            </div>
            <div className={"h-full w-24 content-center"}>
                <span>{props.sex}</span>
            </div>
        </div>
    )
}