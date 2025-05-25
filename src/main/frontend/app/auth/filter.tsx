import {type LoginApi, type SessionResponse} from "~/api/login-api";
import {type ReactNode, useContext, useEffect, useState} from "react";
import {CurrentSessionContext, LoginApiContext} from "~/context/context";
import NotAllowedPage from "~/pages/NotAllowedPage";
import {useNavigate} from "react-router";

export interface Props {
    roles: string[];
    content: ReactNode;
}

export function AuthFilter(props: Props) {
    const loginApi: LoginApi = useContext<LoginApi>(LoginApiContext);
    const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);
    const [isAuthorized, setIsAuthorized] = useState<boolean | undefined>(undefined);
    const [session, setSession] = useState<SessionResponse | undefined>(undefined);
    const navigate = useNavigate();
    const [content, setContent] = useState<ReactNode>(undefined);
    const [isSessionChecked, setIsSessionChecked] = useState(false);

    useEffect(() => {
        async function fetchSession() {
            const response = await loginApi.session();
            if (!response.ok) {
                navigate("/login")
            }
            if (response.ok) {
                const session = await response.json() as SessionResponse;
                setSession(session);
                setIsAuthenticated(true);
                if (props.roles.some((role) => session.roles.includes(role))) {
                    setIsAuthorized(true);
                } else {
                    setIsAuthorized(false);
                }
            } else {
                setIsAuthenticated(false);
            }
            setIsSessionChecked(true);
        }
        if (isSessionChecked) {
            return;
        }
        fetchSession();
    }, []);


    useEffect(() => {
        if (!isSessionChecked) {
            return;
        }
        if (isAuthenticated && isAuthorized) {
            if (isAuthorized) {
                return setContent(props.content);
            } else {
                setContent(<NotAllowedPage />);
            }
        }
        if (!isAuthenticated) {
            navigate("/login");
        }
    }, [isSessionChecked]);

    return <CurrentSessionContext.Provider value={{ session, setSession }}>
        <div>{content}</div>
    </CurrentSessionContext.Provider>;
}
