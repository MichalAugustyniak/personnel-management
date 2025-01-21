import SideNavMenuLine from "~/components/SideNavMenuLine";
import SideMenuOptionContainer, {type Option} from "~/components/SideMenuOptionContainer";
import SideMenuOption from "~/components/SideMenuOption";

export interface OptionContainer {
    name: string;
    options: Option[];
}

export interface Props {
    logoURL: string;
    optionContainers: OptionContainer[];
}

export default function SideNavMenu(props: Props) {
    let key = 0;
    return (
        <>
            <div className={"h-screen border-r-2 border-gray-400 w-2/12 2xl:w-1/12 overflow-y-auto [&::-webkit-scrollbar]:w-1\n" +
                "  [&::-webkit-scrollbar-track]:bg-gray-100\n" +
                "  [&::-webkit-scrollbar-thumb]:bg-gray-300 [&::-webkit-scrollbar-track]:rounded-full [&::-webkit-scrollbar-thumb]:rounded-full"}>
                <div className={"h-24 content-center justify-items-center"}>
                    <img src={props.logoURL} alt={"logo"} className={"h-full"}/>
                </div>
                <SideNavMenuLine></SideNavMenuLine>
                <div className={"m-2"}>
                    <SideMenuOption iconURL={"/home-icon.jpg"} label={"Home"}></SideMenuOption>
                </div>
                <ul className={"list-none space-y-2"}>
                    {props.optionContainers.map(optionContainer => <li key={key++}><SideMenuOptionContainer
                        name={optionContainer.name} options={optionContainer.options}/></li>)}
                </ul>
            </div>
        </>
    )
}
