import React from 'react';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';

const MapComponent = () => {
    return (
        <MapContainer center={[61.45000766895691, 23.856790847309647]} zoom={13} style={{height: "100vh", width: "100vw"}} scrollWheelZoom={false}>
            <TileLayer
                attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
            />
            <Marker position={[61.45000766895691, 23.856790847309647]}>
                <Popup>
                    Tampereen yliopisto. <br /> Yliopiston laatu: 5/5.
                </Popup>
            </Marker>
        </MapContainer>
    );
};

export default MapComponent;