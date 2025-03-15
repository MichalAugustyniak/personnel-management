import {useContext, useEffect, useState} from "react";
import {LogoApiContext} from "~/context/context";

export default function LogoUploadPage() {
    const [logo, setLogo] = useState<string>("");
    const [file, setFile] = useState<File | null>(null);

    const logoApi = useContext(LogoApiContext);

    const fetchLogo = async () => {
        const response = await logoApi.getLogo();
        if (!response.raw.ok) {
            throw new Error("Something went wrong while fetching the logo");
        }
        console.log(logoApi.getHost() + response.body.propertyValue);
        setLogo(logoApi.getHost() + response.body.propertyValue);
    }

    useEffect(() => {
        fetchLogo()
    }, []);

    const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        if (event.target.files && event.target.files[0]) {
            setFile(event.target.files[0]);
        }
    };

    const handleUpload = async () => {
        if (!file) return;
        try {
            await logoApi.uploadLogo(file);
            const res = await logoApi.getLogo();
            setLogo(res.body.propertyValue);
        } catch (error) {
            console.error("File upload error:", error);
        }
    };

    return (
        <div className="flex items-center justify-center min-h-screen bg-gray-100">
            <div className="bg-white p-6 rounded-lg shadow-lg text-center">
                <img src={logo} alt="Logo" className="w-40 h-40 mx-auto mb-4 border rounded-lg object-contain" />
                <input type="file" onChange={handleFileChange} className="block mx-auto mb-4" />
                <button
                    onClick={handleUpload}
                    className="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 transition"
                >
                    Upload logo
                </button>
            </div>
        </div>
    );
};
