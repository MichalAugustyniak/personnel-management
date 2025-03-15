import {AuthFilter} from "~/auth/filter";
import ManagerPage from "~/pages/ManagerPage";

export default function() {
    return (
        <AuthFilter roles={["Admin", "Manager"]} content={<ManagerPage/>}/>
    )
}