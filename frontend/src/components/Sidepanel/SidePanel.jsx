import React, {useEffect, useState} from "react";
import UniData from "../../data/UniData.jsx";
import {Button} from "react-bootstrap";
import AdmissionChart from "../../data/AdmissionDataGraph.jsx";
import {AdmissionData} from "../../data/AdmissionData.jsx";
import './SidePanel.css';
import HsChart from "../../data/HsDataGraph.jsx";
import HsStudentInfo from "../../data/HsStudentInfo.jsx";

const SidePanel=({school, closePanel, type,isOpen}) =>{
    const [data, setData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [selectedSchool, setSelectedSchool] = useState(null);
    const [selectedField,setSelectedField]=useState(null);
    const [isFieldDataOpen,setIsFieldDataOpen]=useState(false);
    const [isGraphOpen, setIsGraphOpen] = useState(false);
    const [selectedMethod, setSelectedMethod] = useState(null);
    const [searchTerm, setSearchTerm] = useState("");


    const toggleFieldData = (field) => {
        setSelectedField(field);
        setIsFieldDataOpen(!isFieldDataOpen);
    };

    const closeFieldData = () => {
        setIsFieldDataOpen(false);
        setSelectedField(null);
    }

    const showGraph = (method) => {
        setSelectedMethod(method);
        setIsGraphOpen(true);
    };

    const closeGraph = () => {
        setIsGraphOpen(false);
        setSelectedMethod(null);
    };

    const isGraphAvailable = async(method) => {
        try {
            const result = await AdmissionData(data.name, selectedField.name, method.name);
            return Object.keys(result).length!==1;
        } catch (error) {
            return false;
        }

    };


    function FieldData({field,closeFieldData}){
        const [availability, setAvailability] = useState({});

        useEffect(() => {
            const checkAvailability = async () => {
                const availabilityMap = {};
                await Promise.all(
                    (field.admissionMethods || []).map(async (method) => {
                        const available = await isGraphAvailable(method);
                        availabilityMap[method.name] = available;
                    })
                );
                setAvailability(availabilityMap);
            };

            checkAvailability();
        }, [field]);

        return (
            <div className={`field-data-${field.name || ''} ${isFieldDataOpen ? "field-data-open" : ""}`}>
                <button onClick={() => {
                    closeFieldData();
                    closeGraph();
                }} className="close-field-data-button">
                    Close
                </button>
                <h4>Admission Methods:</h4>
                <ul>
                    {field.admissionMethods?.map((method, methodIndex) => (
                        <li key={methodIndex}>
                            <p><strong>Method:</strong> {method.name || 'N/A'}</p>
                            <p><strong>Required Points:</strong> {method.requiredPoints || 'N/A'}</p>
                            {availability[method.name] ? (
                                <Button onClick={() => showGraph(method)}>View Chart</Button>
                            ) : (
                                <div></div>
                            )}
                        </li>
                    ))}
                </ul>
            </div>);
    }

    if (type === "high_school") {
        return (
            <div className={`side-panel ${isOpen ? "side-panel-open" : ""}`}>
                <button onClick={() => closePanel()} className="close-panel-button">
                    &times; Close
                </button>
                <h2>{school}</h2>
                <HsStudentInfo school={school} year={2024} />
                <HsChart school={school} year={2024} />
            </div>
        );
    }
    else {
        useEffect(() => {
            const fetchSchoolData = async () => {
                try {
                    setLoading(true);
                    const result = await UniData(school); // Fetch school data based on name
                    setData(result);
                } catch (err) {
                    setError("Error fetching school data");
                } finally {
                    setLoading(false);
                }
            };

            if (school) fetchSchoolData();
        }, [school]);

        if (loading) return <div className="side-panel">Loading...</div>;
        if (error) return <div className="side-panel">Error: {error}</div>;

        if (isGraphOpen && data && selectedField && selectedMethod){
            return(
                <div className={`side-panel ${isOpen ? "side-panel-open" : ""}`}>
                    <button onClick={() => {
                        closePanel();
                        closeGraph();
                    }} className="close-panel-button">
                        &times; Close
                    </button>
                    <div className="graph-panel">
                        <h2>{data.name}</h2>
                        <h3>{selectedField.name}</h3>
                        <button onClick={closeGraph} className="close-graph-button">
                            &times; Close chart
                        </button>
                        <h4>{selectedMethod.name}</h4>
                        <AdmissionChart name={data.name} field={selectedField.name}
                                        admissionMethod={selectedMethod.name}/>
                    </div>
                </div>
            )
        }

        const filteredFields =
            data?.fields?.filter((field) =>
                field.name.toLowerCase().includes(searchTerm.toLowerCase())
            ) || [];
        return (
        <div className={`side-panel ${isOpen ? "side-panel-open" : ""}`}>
            <button onClick={() => {
                closePanel();
                closeGraph();
            }} className="close-panel-button">
                &times; Close
            </button>
            <h2>{school}</h2>
            {data && (
                <>
                        {selectedField === null ?
                            (
                                <div>
                                    <p><strong>University Name:</strong>{data.name}</p>
                                    <h3>Fields of Study:</h3>
                                    <input
                                        type="text"
                                        placeholder="Search fields of study"
                                        value={searchTerm}
                                        onChange={(e) => setSearchTerm(e.target.value)}
                                        className="search-input"
                                    />
                                </div>
                            ) :
                            (
                                <p>{selectedField.name}</p>
                            )}
                    <div>
                        <ul>
                            {selectedField === null ? filteredFields?.map((field, fieldIndex) => (
                                    <li key={fieldIndex}>
                                            <Button onClick={() =>toggleFieldData(field)}>
                                                <p>{field.name}</p>
                                            </Button>
                                        </li>
                                    ))
                                    : (<li>
                                        <FieldData field={selectedField} closeFieldData={closeFieldData} />
                                    </li>)}
                            </ul>
                            {isGraphOpen && selectedMethod && (
                                <div className="graph-panel">
                                    <button onClick={closeGraph} className="close-graph-button">
                                        &times; Close chart
                                    </button>
                                    <AdmissionChart name={data.name} field={selectedField.name} admissionMethod={selectedMethod.name}/>
                                </div>
                            )}
                        </div>
                    </>
                )
                }
            </div>
        );
    }
}
export default SidePanel;