import SideNavMenuLine from "~/components/SideNavMenuLine";
import SideMenuOptionContainer, {type Option} from "~/components/SideMenuOptionContainer";
import SideMenuOption from "~/components/SideMenuOption";
import React, {createContext, useContext, useEffect, useState} from "react";
import {useSearchParams} from "react-router";
import {LogoApiContext} from "~/context/context";

export interface OptionContainer {
    name: string;
    options: Option[];
}

export interface Props {
    logoURL: string;
    optionContainers: OptionContainer[];
    homeOnClick(): void;
    homeId: number;
}

/*
export interface SelectedOption {
    state?: string;
    setState: React.Dispatch<React.SetStateAction<string | undefined>>;
}


export const SelectedOptionContext = createContext<SelectedOption | undefined>(undefined);

 */


export default function SideNavMenu(props: Props) {
    return (
        <>
            <div className={"h-screen border-r-2 border-gray-200  md:w-1/6 2xl:w-1/12 overflow-y-auto [&::-webkit-scrollbar]:w-1\n" +
                "  [&::-webkit-scrollbar-track]:bg-gray-100\n" +
                "  [&::-webkit-scrollbar-thumb]:bg-gray-300 [&::-webkit-scrollbar-track]:rounded-full [&::-webkit-scrollbar-thumb]:rounded-full"}>
                <div className={"flex flex-col items-center justify-center py-2 h-24"}>
                    <img src={props.logoURL} alt={"logo"} className={"scale-95"}/>
                </div>
                <SideNavMenuLine></SideNavMenuLine>
                <div className={"m-2"}>
                    <SideMenuOption iconURL={"home.png"}
                                    label={"Home"}
                                    onClick={() => props.homeOnClick()}
                                    isActive={true}
                                    id={props.homeId}
                    ></SideMenuOption>
                </div>
                <ul className={"list-none space-y-2"}>
                    {props.optionContainers.map((optionContainer, key) => <li key={key}><SideMenuOptionContainer
                        name={optionContainer.name} options={optionContainer.options}/></li>)}
                </ul>
            </div>
        </>
    );
}
