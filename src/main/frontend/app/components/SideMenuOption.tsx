export interface Props {
    iconURL: string,
    label: string;
}

export default function SideMenuOption(props: Props) {
    return (
        <>
            <div className={"flex flex-row hover:text-blue-400 hover:font-bold space-x-1 cursor-pointer"}>
                <div className={"w-1/5"}>
                    <img src={props.iconURL} alt={"icon"} className={"scale-75"}/>
                </div>
                <div className={"w-4/5 content-center truncate"}>
                    <span>{props.label}</span>
                </div>
            </div>
        </>
    )
}
