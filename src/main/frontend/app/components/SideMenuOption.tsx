import {useContext, useEffect} from "react";
import {SelectedOptionIdContext} from "~/components/DashboardBase";

export interface Props {
    iconURL: string,
    label: string;
    onClick(): void;
    isActive: boolean;
    id: number;
}

export default function SideMenuOption(props: Props) {
    const selectedOptionIdContext = useContext(SelectedOptionIdContext);

    useEffect(() => {
        if (!selectedOptionIdContext) {
            throw new Error("Context is undefined");
        }
        //console.log(selectedOptionIdContext.state);
    }, [selectedOptionIdContext]);

    return (
        <>
            <div onClick={props.onClick}
                 className={`flex flex-row hover:text-blue-400 ${selectedOptionIdContext && props.id === selectedOptionIdContext.state && "text-blue-400 font-bold"} hover:font-bold space-x-1 cursor-pointer transition-all duration-200`}>
                <div className={"w-1/5"}>
                    <img src={props.iconURL}
                         alt={"icon"}
                         className={"scale-75"}/>
                </div>
                <div className={"w-4/5 content-center truncate"}>
                    <span>{props.label}</span>
                </div>
            </div>
        </>
    )
}
