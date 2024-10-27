import React, {useEffect, useState} from 'react';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import getData from '../../data/NameAndCoord.jsx';
import FieldFilter from '../Search/FieldFilter.jsx';
import RatingFilter from '../Search/RatingFilter.jsx';
import './MapComponent.css';
import {useNavigate} from "react-router-dom";
import {Button} from "react-bootstrap";
import UniData from "../../data/UniData.jsx";

const MapComponent = ({type}) => {
    const navigate = useNavigate();
    const [selectedFields, setSelectedFields] = React.useState([]);
    const [selectedRatings, setSelectedRatings] = React.useState([]);
    const [filteredData, setFilteredData] = React.useState([]);
    const [isPanelOpen, setIsPanelOpen]=useState(false);
    const [selectedSchool, setSelectedSchool] = useState(null);
    const [selectedField,setSelectedField]=useState(null);
    const [isFieldDataOpen,setIsFieldDataOpen]=useState(false);
    /*
    const allFields = Array.from(
        new Set(getData(type).flatMap((data) => data.fields))
    );

    const allRatings = Array.from(
        new Set(getData(type).flatMap((data) => data.starRating))
    );

    React.useEffect(() => {
        let filtered = getData(type);

        if (selectedFields.length > 0) {
            filtered = filtered.filter((data) =>
                selectedFields.some((field) => data.fields.includes(field))
            );
        }

        if (selectedRatings.length > 0) {
            filtered = filtered.filter((data) =>
                selectedRatings.includes(Math.floor(data.starRating))
            );
        }

        setFilteredData(filtered);
    }, [selectedFields, selectedRatings]);
     */
    useEffect(() => {
        const fetchData = async () => {
            const data = await getData(type);
            setFilteredData(data);
        };
        fetchData();
    }, [type]);

    const togglePanel = (school) => {
        setSelectedSchool(school);
        setIsPanelOpen(!isPanelOpen);
    };

    const closePanel = () => {
        setIsPanelOpen(false);
        setSelectedSchool(null);
    };

    const toggleFieldData = (field) => {
        setSelectedField(field);
        setIsFieldDataOpen(!isFieldDataOpen);
    };

    const closeFieldData = () => {
        setIsFieldDataOpen(false);
        setSelectedField(null);
    };

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

    function SidePanel({school, closePanel, type}) {
        const [data, setData] = useState(null);
        const [loading, setLoading] = useState(true);
        const [error, setError] = useState(null);

        if (type === "high_school") {
            return (<div className={`side-panel ${isPanelOpen ? "side-panel-open" : ""}`}>
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
            <div className={`side-panel ${isPanelOpen ? "side-panel-open" : ""}`}>
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
        )
    }
    }
    return (
        <>
            <div className="map-container" style={{display: 'flex'}}>

                <div className="map-wrapper" style={{width: '80%'}}>
                    <MapContainer center={[61.45000766895691, 23.856790847309647]} zoom={13}
                                  style={{height: "100vh", width: "100vw"}} scrollWheelZoom={true}>
                        <TileLayer
                            attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                        />

                        {Object.keys(filteredData).map((university) => {
                                return Object.keys(filteredData[university]).map((campus) => {
                                        const {lat, lon} = filteredData[university][campus];
                                        return (
                                            <Marker key={`${university}-${campus}`} position={[lat, lon]}>
                                                <Popup>
                                                    <Button onClick={() => togglePanel(university)}>
                                                        <strong>{campus}</strong>
                                                    </Button>
                                                </Popup>
                                            </Marker>
                                        );
                                    }
                                )
                            }
                        )
                        }
                    </MapContainer>
                </div>
            </div>
            {isPanelOpen && selectedSchool && (
                <SidePanel school={selectedSchool} closePanel={closePanel} type={type}/>
            )}
        </>
    );

};
export default MapComponent;