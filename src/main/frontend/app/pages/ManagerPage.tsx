import SideNavMenu, {type OptionContainer} from "~/components/SideNavMenu";

const optionContainers: OptionContainer[] = [
    {
        name: "OptionGroup1",
        options: [
            {
                iconURL: "/account_circle-24px.png",
                label: "Option 1"
            },
            {
                iconURL: "/account_circle-24px.png",
                label: "Option 2"
            },
        ]
    },
    {
        name: "OptionGroup2",
        options: [
            {
                iconURL: "/account_circle-24px.png",
                label: "Option 1"
            },
            {
                iconURL: "/account_circle-24px.png",
                label: "Option 2"
            },
        ]
    },
    {
        name: "OptionGroup2",
        options: [
            {
                iconURL: "/account_circle-24px.png",
                label: "Option 1"
            },
            {
                iconURL: "/account_circle-24px.png",
                label: "Option 2"
            },
        ]
    },
    {
        name: "OptionGroup2",
        options: [
            {
                iconURL: "/account_circle-24px.png",
                label: "Option 1"
            },
            {
                iconURL: "/account_circle-24px.png",
                label: "Option 2"
            },
        ]
    },
    {
        name: "OptionGroup2",
        options: [
            {
                iconURL: "/account_circle-24px.png",
                label: "Option 1"
            },
            {
                iconURL: "/account_circle-24px.png",
                label: "Option 2"
            },
        ]
    }
];

const logoURL = "/public/LEGO_logo.svg.png";

export default function ManagerPage() {
    return (
        <>
            <SideNavMenu logoURL={logoURL} optionContainers={optionContainers}/>
        </>
    )
}
