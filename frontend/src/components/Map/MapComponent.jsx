import React, {useEffect} from 'react';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import getData from '../../data/PrototypeData';
import FieldFilter from '../Search/FieldFilter.jsx';
import RatingFilter from '../Search/RatingFilter.jsx';
import './MapComponent.css';
import {useNavigate} from "react-router-dom";
import {Button} from "react-bootstrap";

const MapComponent = ({type}) => {
    const navigate = useNavigate();
    const [selectedFields, setSelectedFields] = React.useState([]);
    const [selectedRatings, setSelectedRatings] = React.useState([]);
    const [filteredData, setFilteredData] = React.useState([]);

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

    return (
        <div className="map-container" style={{display: 'flex'}}>

            <div className="map-wrapper" style={{ width: '80%' }}>
                <MapContainer center={[61.45000766895691, 23.856790847309647]} zoom={13}
                              style={{height: "100vh", width: "100vw"}} scrollWheelZoom={true}>
                    <TileLayer
                        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                    />

                    {Object.keys(filteredData).map((school) => {
                        const { lat, lon } = filteredData[school];

                        return (
                            <Marker key={school} position={[lat, lon]}>
                                <Popup>
                                    <strong>{school}</strong>
                                </Popup>
                            </Marker>
                        );
                    })}
                </MapContainer>
            </div>
        </div>
    );
};
export default MapComponent;