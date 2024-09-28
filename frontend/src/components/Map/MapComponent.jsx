import React from 'react';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import Universities from '../../data/PrototypeData';
import FieldFilter from '../Search/FieldFilter.jsx';
import RatingFilter from '../Search/RatingFilter.jsx';
import './MapComponent.css';

const MapComponent = () => {
    const [selectedFields, setSelectedFields] = React.useState([]);
    const [selectedRatings, setSelectedRatings] = React.useState([]);
    const [filteredUniversities, setFilteredUniversities] = React.useState(Universities);

    const allFields = Array.from(
        new Set(Universities.flatMap((uni) => uni.fields))
    );

    const allRatings = Array.from(
        new Set(Universities.flatMap((uni) => uni.starRating))
    );

    React.useEffect(() => {
        let filtered = Universities;

        if (selectedFields.length > 0) {
            filtered = filtered.filter((uni) =>
                selectedFields.some((field) => uni.fields.includes(field))
            );
        }

        if (selectedRatings.length > 0) {
            filtered = filtered.filter((uni) =>
                selectedRatings.includes(Math.floor(uni.starRating))
            );
        }

        setFilteredUniversities(filtered);
    }, [selectedFields, selectedRatings, Universities]);


    return (
        <div className="map-container" style={{display: 'flex'}}>
            <div className="filter-panel-container" style={{width: '20%', padding: '10px'}}>
                <FieldFilter
                    fields={allFields}
                    selectedFields={selectedFields}
                    setSelectedFields={setSelectedFields}
                />
                <RatingFilter
                    ratings={allRatings}
                    selectedRatings={selectedRatings}
                    setSelectedRatings={setSelectedRatings}
                />
            </div>

            <div className="map-wrapper" style={{ width: '80%' }}>
                <MapContainer center={[61.45000766895691, 23.856790847309647]} zoom={13}
                              style={{height: "100vh", width: "100vw"}} scrollWheelZoom={true}>
                    <TileLayer
                        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                    />
                    {filteredUniversities.map((uni) => (
                        <Marker key={uni.id} position={uni.position}>
                            <Popup>
                                <strong>{uni.name}</strong><br/>
                                Star Rating: {uni.starRating}<br/>
                                Number of Students: {uni.numberOfStudents}<br/>
                                Fields: {uni.fields.join(', ')}
                            </Popup>
                        </Marker>
                    ))}
                </MapContainer>
            </div>
        </div>
    );
};
export default MapComponent;