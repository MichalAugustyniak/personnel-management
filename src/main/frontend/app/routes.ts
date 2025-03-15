import {type RouteConfig, index, route} from "@react-router/dev/routes";

export default [
    route("manager", "pages/SecuredManagerPage.tsx"),
    route("login", "pages/LoginPage.tsx"),
    route("admin", "pages/SecuredAdminPage.tsx"),
    route("employee", "pages/SecuredEmployeePage.tsx"),
    route("hr", "pages/SecuredHrPage.tsx"),
] satisfies RouteConfig;
