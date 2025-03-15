import {AuthFilter} from "~/auth/filter";
import TestPage from "~/pages/TestPage";

export default function SecuredTestPage() {
    return <AuthFilter roles={["Admin"]} content={<TestPage/>}/>
}