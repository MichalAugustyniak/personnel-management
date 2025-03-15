import {useState} from "react";

export default function SearchBar() {
    const [isSearching, setIsSearching] = useState(false);
    return (
        <div className={"flex flex-col h-10 w-full relative "}
             onFocus={() => setIsSearching(true)}
             onBlur={() => setIsSearching(false)}>
            <div className={"flex flex-row h-full w-full"}>
                <div className={"h-full w-1/6 border-blue-300 border-2 rounded-l-2xl"}></div>
                <input className={"w-full h-full border-2 border-gray-300 rounded-r-2xl"}></input>
            </div>
            {isSearching
                && <div className={"bg-amber-300 w-full h-10 top-10 absolute"}></div>}
        </div>
    )
}