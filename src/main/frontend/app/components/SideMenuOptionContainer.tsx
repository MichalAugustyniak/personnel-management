import SideMenuOption from "~/components/SideMenuOption";

export interface Option {
    iconURL: string,
    label: string;
    onClick(): void;
    isActive: boolean;
    id: number;
}

export interface Props {
    name: string;
    options: Option[];
}

export default function SideMenuOptionContainer(props: Props) {
    return (
        <>
           <div className={"flex flex-col content-center"}>
               <div className={"font-bold ml-1 mb-1 truncate"}>
                   {props.name}
               </div>
               <div className={"content-center"}>
                   <ul className={"list-none space-y-2"}>
                       { props.options.map((option, key) => <li key={key}>
                           <SideMenuOption iconURL={option.iconURL}
                                           label={option.label}
                                           onClick={option.onClick}
                                           isActive={option.isActive}
                                           id={option.id}
                           />
                       </li>) }
                   </ul>
               </div>
           </div>
        </>
    )
}
