import {AuthFilter} from "~/auth/filter";
import AdminPage from "~/pages/AdminPage";

export default function SecuredAdminPage() {
    return <AuthFilter roles={["Admin"]} content={<AdminPage/>}/>
}