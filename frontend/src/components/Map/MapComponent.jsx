import React, {useEffect, useState} from 'react';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import getData from '../../data/NameAndCoord.jsx';
import FieldFilter from '../Search/FieldFilter.jsx';
import RatingFilter from '../Search/RatingFilter.jsx';
import TypeFilter from "../Search/TypeFilter.jsx";
import './MapComponent.css';
import {useNavigate} from "react-router-dom";
import {Button} from "react-bootstrap";
import UniData from "../../data/UniData.jsx";
import SidePanel from "../Sidepanel/SidePanel.jsx";

const MapComponent = ({type}) => {
    const navigate = useNavigate();
    const [selectedFields, setSelectedFields] = React.useState([]);
    const [selectedRatings, setSelectedRatings] = React.useState([]);
    const [filteredData, setFilteredData] = React.useState([]);
    const [isPanelOpen, setIsPanelOpen]=useState(false);
    const [selectedSchool, setSelectedSchool] = useState(null);
    const [selectedTypes, setSelectedTypes] = React.useState([]);
    const [availableTypes, setAvailableTypes] = React.useState([]);

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

            let flattenedData = [];

            const allSchoolTypes = Array.from(
                new Set(Object.values(data).flatMap((school) =>
                    Object.values(school).map((campusInfo) => campusInfo.type)))
            )

            for (const [schoolName, campuses] of Object.entries(data)) {
                for (const [campusName, campusInfo] of Object.entries(campuses)) {
                    flattenedData.push( {
                        schoolName,
                        campusName,
                        coord: campusInfo.coord,
                        type: campusInfo.type
                    });
                }
            }

            if (!Array.isArray(flattenedData)) {
                console.error("Data is not an array:", flattenedData);
                return;
            }

            setAvailableTypes(allSchoolTypes);
            setSelectedTypes(allSchoolTypes);
            setFilteredData(flattenedData);
        };
        fetchData();
    }, [type]);

    const filteredMarkers = filteredData.filter((campus) =>
        selectedTypes.includes(campus.type));

    const togglePanel = (school) => {
        setSelectedSchool(school);
        setIsPanelOpen(!isPanelOpen);
    };

    const closePanel = () => {
        setIsPanelOpen(false);
        setSelectedSchool(null);
    };



    return (
        <>
            <div className="map-container" style={{display: 'flex'}}>
                <div className="filter-panel-container" style={{width: '20%', padding: '10px'}}>
                    <TypeFilter
                        types={availableTypes}
                        selectedTypes={selectedTypes}
                        setSelectedTypes={setSelectedTypes}
                    />

                    <Button
                        variant="primary"
                        size="lg"
                        onClick={() => navigate("/")}
                        style={{marginTop: "20px"}}
                    >
                        Back to Title Screen
                    </Button>{' '}
                </div>

                <div className="map-wrapper" style={{width: '80%'}}>
                    <MapContainer center={[61.45000766895691, 23.856790847309647]} zoom={13}
                                  style={{height: "100vh", width: "100vw"}} scrollWheelZoom={true}>
                        <TileLayer
                            attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                        />

                        {filteredMarkers.map((item) => {
                            const {lat, lon} = item.coord;
                            return (
                                <Marker key={`${item.schoolName}-${item.campusName}`} position={[lat, lon]}>
                                    <Popup>
                                        <Button onClick={() => togglePanel(item.schoolName)}>
                                            <strong>{item.campusName}</strong>
                                        </Button>
                                    </Popup>
                                </Marker>
                            );
                        })
                        }

                    </MapContainer>
                </div>
            </div>
            {isPanelOpen && selectedSchool && (
                <SidePanel
                    school={selectedSchool}
                    closePanel={closePanel}
                    type={type}
                    isOpen={isPanelOpen}/>
            )}
        </>
    );

};
export default MapComponent;