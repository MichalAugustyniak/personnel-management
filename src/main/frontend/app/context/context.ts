import {type LoginApi, LoginApiV1, type SessionResponse} from "~/api/login-api";
import React, {createContext, useContext, useState} from "react";
import {type UserApi, UserApiV1} from "~/api/user-api";
import {type AddressApi, AddressApiV1} from "~/api/address-api";
import {type TaskApi, TaskApiV1} from "~/api/task-api";
import {type TaskEventApi, TaskEventApiV1} from "~/api/task-event-api";
import {type ShiftTypeApi, ShiftTypeApiV1} from "~/api/shift-type-api";
import {type ScheduleApi, ScheduleApiV1} from "~/api/schedule-api";
import {type AttendanceStatusApi, AttendanceStatusApiV1} from "~/api/attendance-status-api";
import {type AttendanceApi, AttendanceApiV1} from "~/api/attendance-api";
import {type LogoApi, LogoApiV1} from "~/api/logo-api";
import * as process from "node:process";

const url = import.meta.env.VITE_API_URL;

export const loginApi: LoginApi = new LoginApiV1(url);

export const LoginApiContext = createContext<LoginApi>(loginApi);

export const userApi: UserApi = new UserApiV1(url);

export const UserApiContext = createContext<UserApi>(userApi);

export const addressApi: AddressApi = new AddressApiV1(url);

export const AddressApiContext = createContext<AddressApi>(addressApi);

export const taskApi: TaskApi = new TaskApiV1(url);

export const TaskApiContext = createContext<TaskApi>(taskApi);

export const taskEventApi: TaskEventApi = new TaskEventApiV1(url);

export const TaskEventApiContext = createContext<TaskEventApi>(taskEventApi);

export const shiftTypeApi: ShiftTypeApi = new ShiftTypeApiV1(url);

export const ShiftTypeContext = createContext(shiftTypeApi);

export const scheduleApi: ScheduleApi = new ScheduleApiV1(url);

export const ScheduleApiContext = createContext(scheduleApi);

export const attendanceStatusApi: AttendanceStatusApi = new AttendanceStatusApiV1(url);

export const AttendanceStatusApiContext = createContext(attendanceStatusApi);

export const attendanceApi: AttendanceApi = new AttendanceApiV1(url);

export const AttendanceApiContext = createContext(attendanceApi);

export const logoApi: LogoApi = new LogoApiV1(url);

export const LogoApiContext = createContext(logoApi);

interface CurrentSession {
    session: SessionResponse | undefined;
    setSession: React.Dispatch<React.SetStateAction<SessionResponse | undefined>>;
}

export const CurrentSessionContext = createContext<CurrentSession | undefined>(undefined);

export const useCurrentSessionContext = () => {
    const context = useContext(CurrentSessionContext);
    if (!context) {
        throw new Error('useCurrentSessionContext must be used within a CurrentSessionContextProvider');
    }
    return context;
};
