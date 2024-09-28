import React from 'react';
import MapComponent from '../components/Map/MapComponent';
import './Home.css';

const Home = () => {
    return (
        <div className={"home"}>
            <h1 className={"title"}>Project Horizon</h1>
            <MapComponent/>
        </div>
    );
};

export default Home;