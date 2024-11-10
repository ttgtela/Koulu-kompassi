import React, {useEffect, useState} from "react";
import UniData from "../../data/UniData.jsx";
import {Button} from "react-bootstrap";

const SidePanel=({school, closePanel, type,isOpen}) =>{
    const [data, setData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [selectedSchool, setSelectedSchool] = useState(null);
    const [selectedField,setSelectedField]=useState(null);
    const [isFieldDataOpen,setIsFieldDataOpen]=useState(false);


    const toggleFieldData = (field) => {
        setSelectedField(field);
        setIsFieldDataOpen(!isFieldDataOpen);
    };

    const closeFieldData = () => {
        setIsFieldDataOpen(false);
        setSelectedField(null);
    }


    function FieldData({field,closeFieldData}){
        return (
            <div className={`field-data-${field} ${isFieldDataOpen ? "field-data-open" : ""}`}>
                <button onClick={closeFieldData} className="close-field-data-button">
                    Close
                </button>
                <h4>Admission Methods:</h4>
                <ul>
                    {field.admissionMethods?.map((method, methodIndex) => (
                        <li key={methodIndex}>
                            <p><strong>Method:</strong> {method.name}</p>
                            <p><strong>Required Points:</strong> {method.requiredPoints}
                            </p>
                        </li>
                    ))}
                </ul>
            </div>);
    }

    if (type === "high_school") {
        return (<div className={`side-panel ${isOpen ? "side-panel-open" : ""}`}>
                <button onClick={closePanel} className="close-panel-button">
                    &times; Close
                </button>
                <h2>{school}</h2>
            </div>
        )
    } else {
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

        return (
            <div className={`side-panel ${isOpen ? "side-panel-open" : ""}`}>
                <button onClick={closePanel} className="close-panel-button">
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
                                </div>
                            ) :
                            (
                                <p>{selectedField.name}</p>
                            )}

                        <div>

                            <ul>
                                {selectedField===null ?data.fields?.map((field, fieldIndex) => (
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
                        </div>
                    </>
                )
                }
            </div>
        );
    }
}
export default SidePanel;