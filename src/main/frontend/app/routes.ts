import {type RouteConfig, index, route} from "@react-router/dev/routes";

export default [
    index("routes/home.tsx"),
    route("test/ManagerPage.tsx", "pages/ManagerPage.tsx")
] satisfies RouteConfig;
