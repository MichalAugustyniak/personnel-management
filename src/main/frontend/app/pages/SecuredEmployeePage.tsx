import {AuthFilter} from "~/auth/filter";
import EmployeePage from "~/pages/EmployeePage";

export default function SecuredEmployeePage() {
    return <AuthFilter roles={["Admin", "Employee"]} content={<EmployeePage/>}/>
}