import SideNavMenu from "~/components/SideNavMenu";
import DashboardHeader from "~/components/DashboardHeader";
import {createContext, type ReactNode, useEffect, useState} from "react";
import {type Params, useLocation, useNavigate, useParams, useSearchParams} from "react-router";
import {type OptionContainer as OptionContainer2} from "~/components/SideNavMenu";
import {type Option as Option2} from "~/components/SideMenuOptionContainer";
import {set} from "react-hook-form";

export interface Tab {
    name: string;
    content: ReactNode;
}

export interface Option {
    iconURL: string;
    label: string;
    tab: Tab;
}

export interface OptionContainer {
    name: string;
    options: Option[];
}

export interface Props {
    logoURL: string;
    homeTab: ReactNode;
    optionContainers: OptionContainer[];
}

function findTab(optionContainers: OptionContainer[], name: string): Tab | null {
    for (const optionContainer of optionContainers) {
        for (const option of optionContainer.options) {
            if (option.tab.name === name) {
                return option.tab;
            }
        }
    }
    return null;
}

function throwIfNull(tab: Tab | null): Tab {
    if (tab === null) {
        throw new Error();
    }
    return tab;
}

export interface SelectedOptionId {
    state?: number;
    setState: React.Dispatch<React.SetStateAction<number | undefined>>;
}


export const SelectedOptionIdContext = createContext<SelectedOptionId | undefined>(undefined);

export default function DashboardBase(props: Props) {
    const location = useLocation();
    const [searchParams, setSearchParams] = useSearchParams();
    const tabParam = searchParams.get("tab") || "home";
    let tab: Tab = tabParam === "home" ? {name: "home", content: props.homeTab} : throwIfNull(findTab(props.optionContainers, tabParam));
    const [currentTab, setCurrentTab] = useState<Tab>(tab);
    const navigate = useNavigate();
    const [isRendered, setIsRendered] = useState(false);
    const [selectedOptionId, setSelectedOptionId] = useState<number | undefined>(undefined);
    const [selectedOptionContextId, setSelectedOptionContextId] = useState<SelectedOptionId>({state: selectedOptionId, setState: setSelectedOptionId});
    const [currentTabSelectedOptionMap, setCurrentTabSelectedOptionMap] = useState<Map<Tab, number>>(new Map());
    const [optionContainers, setOptionContainers] = useState<OptionContainer2[] | undefined>(undefined);

    useEffect(() => {
        //console.log(`Tab name: ${currentTab.name}`);
        let optContainers: OptionContainer2[] = [];
        const tempCurrentTabSelectedOptionMap: Map<Tab, number> = new Map();
        let count = 1;
        for (const optionContainer of props.optionContainers) {
            const options: Option2[] = [];
            for (const option of optionContainer.options) {
                const opt: Option2 = {
                    iconURL: option.iconURL,
                    label: option.label,
                    onClick() {
                        setCurrentTab(option.tab);
                    },
                    id: count,
                    isActive: false
                };
                tempCurrentTabSelectedOptionMap.set(option.tab, opt.id);
                count++;
                options.push(opt);
            }
            optContainers.push(({
                name: optionContainer.name,
                options: options
            }));
            setOptionContainers(optContainers);
        }
        setCurrentTabSelectedOptionMap(tempCurrentTabSelectedOptionMap);
        //console.log(tempCurrentTabSelectedOptionMap.forEach((value, key) => console.log(`value: ${value}  key: ${key.name}`)));
        //console.log("optContainers.length = " + optContainers.length);
        const tabParam = searchParams.get("tab");
        //console.log(`tabParam = ${tabParam}`);
        let tab: Tab | undefined = tabParam === "home" ? {name: "home", content: props.homeTab} : undefined;
        //searchParams.forEach(param => console.log(param));
        for (const optionContainer of props.optionContainers) {
            for (const option of optionContainer.options) {
                if (option.tab.name === tabParam) {
                    tab =  option.tab;
                    break
                }
            }
            if (tab !== undefined) {
                setCurrentTab(tab);
                break;
            }
        }
        //console.log(`props.optionContainers.length: ${props.optionContainers.length}`);

        for (const optionContainer of props.optionContainers) {
            for (const option of optionContainer.options) {
                //console.log("tab " + currentTab.name);
                //console.log(`option.tab.name=${option.tab.name} currentTab.name=${currentTab.name}`);
                if (option.tab === tab ? tab : currentTab) {
                    //console.log("setting to: " + tempCurrentTabSelectedOptionMap.get(option.tab));
                    if (!tempCurrentTabSelectedOptionMap.get(option.tab)) {
                        //console.log("currentTabSelectedOptionMap.get(option.tab) is undefined but the map is...");
                        //console.log(`map length = ${tempCurrentTabSelectedOptionMap.size}`);
                    }
                    setSelectedOptionId(tempCurrentTabSelectedOptionMap.get(tab ? tab : currentTab));
                    break;
                }
            }
        }
        setIsRendered(true);
    }, [location.search]);

    useEffect(() => {
        if (!isRendered) {
            //console.log("rendered first time");
            setIsRendered(true);
            return;
        }
        //console.log("dalej");
        //console.log("not rendered first time");
        //console.log("redirecting to: " + "?tab=" + currentTab.name);
        navigate("?tab=" + currentTab.name);
    }, [currentTab]);

    useEffect(() => {
        setSelectedOptionContextId({state: selectedOptionId, setState: setSelectedOptionId});
        //console.log("selected option id: " + selectedOptionId);
        //console.log("selected option id from context: " + selectedOptionContextId.state);
    }, [selectedOptionId]);


    //const params = useParams();
    //let tab = params.tab || "home";
    //const [currentPageIndex, setCurrentPageIndex] = useState<number>();
    //const [currentPage, setCurrentPage] = useState<ReactNode>();
    /*
    if (!tab) {
        setCurrentPageIndex(0);
        setCurrentPage(props.pages[0]);
    }

     */


    //console.log("Rendering dashboard...");



    return (
        <SelectedOptionIdContext.Provider value={selectedOptionContextId}>
            <div className={"flex flex-row"}>
                {optionContainers
                    && <SideNavMenu logoURL={props.logoURL}
                                    optionContainers={optionContainers}
                                    homeOnClick={() => setCurrentTab({name: "home", content: props.homeTab})}
                                    homeId={0}
                    ></SideNavMenu>}
                <div className={"flex flex-col w-full h-screen"}>
                    <DashboardHeader></DashboardHeader>
                    <div className={"h-full w-full overflow-auto"}>{currentTab.content}</div>
                </div>
            </div>
        </SelectedOptionIdContext.Provider>
    );
}
