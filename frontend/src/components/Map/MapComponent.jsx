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
import starImage from "../../assets/star.png";
import emptyStarImage from "../../assets/emptyStar.png";
import Cookies from 'js-cookie';

const MapMarker = ({ item, togglePanel, toggleFavourite, favourites }) => {
    //const isStarColored = favourites.includes(item.schoolName);

    const [isStarColored, setIsStarColored] = React.useState(false);
    // Is star empty or filled
    React.useEffect(() => {
        setIsStarColored(favourites.includes(item.schoolName));
    }, [favourites, item.schoolName]);


    const handleStarClick = () => {
        toggleFavourite(item.schoolName);
    };


    const { lat, lon } = item.coord;

    return (
        <Marker position={[lat, lon]}>
            <Popup>
                <div style={{ display: 'flex', alignItems: 'center' }}>
                    <img
                        src={isStarColored ? starImage : emptyStarImage}
                        alt="star icon"
                        onClick={handleStarClick}
                        style={{ width: '24px', height: '24px', marginRight: '8px', cursor: 'pointer' }}
                    />
                    <Button onClick={() => togglePanel(item.schoolName)}>
                        <strong>{item.campusName}</strong>
                    </Button>
                </div>
            </Popup>
        </Marker>
    );
};

const saveFavourites = (newFavourite) => {
    let favourites = Cookies.get('favourites');
    if (favourites) {
        favourites = JSON.parse(favourites);
    } else {
        favourites = [];
    }

    if (favourites.length > 6) {
        favourites.shift();
    }

    if (!favourites.includes(newFavourite)) {
        favourites.push(newFavourite);
    }
    Cookies.set('favourites', JSON.stringify(favourites), { expires: 7 });
};

const removeFavourite = (removedFavourite) => {
    let favourites = Cookies.get('favourites');
    if (favourites) {
        favourites = JSON.parse(favourites);
    } else {
        favourites = [];
    }

    favourites = favourites.filter((favourite) => favourite !== removedFavourite);
    Cookies.set('favourites', JSON.stringify(favourites), { expires: 7 });
};

const getFavourites = () => {
    const favourites = Cookies.get('favourites');
    return JSON.parse(favourites);
};


const MapComponent = ({type}) => {
    const navigate = useNavigate();
    //const [selectedFields, setSelectedFields] = React.useState([]);
    //const [selectedRatings, setSelectedRatings] = React.useState([]);
    const [filteredData, setFilteredData] = React.useState([]);
    const [isPanelOpen, setIsPanelOpen]= React.useState(false);
    const [selectedSchool, setSelectedSchool] = React.useState(null);
    const [selectedTypes, setSelectedTypes] = React.useState([]);
    const [availableTypes, setAvailableTypes] = React.useState([]);
    const [favourites, setFavourites] = React.useState([]);

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

    useEffect(() => {
        setFavourites(getFavourites());
    }, [favourites]);

    const toggleFavourite = (schoolName) => {
        setFavourites((prevFavourites) => {
           if (prevFavourites.includes(schoolName)) {
               removeFavourite(schoolName);
           } else {
               saveFavourites(schoolName);
           }
           return getFavourites();
        });
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

                    <h3> Favourites </h3>
                    {favourites.map((schoolName) => (
                        <div style={{display: 'flex', alignItems: 'center'}}>
                            <img
                                src={favourites.includes(schoolName) ? starImage : emptyStarImage}
                                alt="star icon"
                                onClick={() => toggleFavourite(schoolName)}
                                style={{width: '24px', height: '24px', marginRight: '8px', cursor: 'pointer'}}
                            />
                            <Button onClick={() => togglePanel(schoolName)}>
                                {schoolName}
                            </Button>
                        </div>
                    ))
                    }
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

                        {filteredMarkers.map((item) => (
                            <MapMarker
                                key={`${item.schoolName}-${item.campusName}`}
                                item={item}
                                togglePanel={togglePanel}
                                toggleFavourite={toggleFavourite}
                                favourites={favourites}
                            />
                        ))
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