import React from 'react';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import PrototypeData from '../../data/PrototypeData';
import FieldFilter from '../Search/FieldFilter.jsx';
import RatingFilter from '../Search/RatingFilter.jsx';
import './MapComponent.css';

const MapComponent = () => {
    const [selectedFields, setSelectedFields] = React.useState([]);
    const [selectedRatings, setSelectedRatings] = React.useState([]);
    const [filteredUniversities, setFilteredUniversities] = React.useState(PrototypeData);

    const allFields = Array.from(
        new Set(PrototypeData.flatMap((data) => data.fields))
    );

    const allRatings = Array.from(
        new Set(PrototypeData.flatMap((data) => data.starRating))
    );

    React.useEffect(() => {
        let filtered = PrototypeData;

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

        setFilteredUniversities(filtered);
    }, [selectedFields, selectedRatings, PrototypeData]);


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
                    {filteredUniversities.map((data) => (
                        <Marker key={data.id} position={data.position}>
                            <Popup>
                                <strong>{data.name}</strong><br/>
                                Star Rating: {data.starRating}<br/>
                                Number of Students: {data.numberOfStudents}<br/>
                                Fields: {data.fields.join(', ')}
                            </Popup>
                        </Marker>
                    ))}
                </MapContainer>
            </div>
        </div>
    );
};
export default MapComponent;