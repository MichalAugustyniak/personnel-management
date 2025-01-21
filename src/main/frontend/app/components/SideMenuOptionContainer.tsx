import SideMenuOption from "~/components/SideMenuOption";

export interface Option {
    iconURL: string,
    label: string;
}

export interface Props {
    name: string;
    options: Option[];
}

export default function SideMenuOptionContainer(props: Props) {
    let key = 0;
    return (
        <>
           <div className={"flex flex-col content-center"}>
               <div className={"font-bold ml-1 mb-1 truncate"}>
                   {props.name}
               </div>
               <div className={"content-center"}>
                   <ul className={"list-none space-y-2"}>
                       { props.options.map(option => <li key={key++}><SideMenuOption iconURL={option.iconURL} label={option.label}/></li>) }
                   </ul>
               </div>
           </div>
        </>
    )
}
