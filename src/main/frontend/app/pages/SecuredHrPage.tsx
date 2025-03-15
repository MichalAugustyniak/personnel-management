import {AuthFilter} from "~/auth/filter";
import {DefaultRoles} from "~/commons/commons";
import HrPage from "~/pages/HrPage";

export default function SecuredHrPage() {
    return <AuthFilter roles={[DefaultRoles.HR]} content={<HrPage/>}/>
}