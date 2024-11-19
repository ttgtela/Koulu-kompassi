import React from 'react';
import {Button} from "react-bootstrap";
import MapComponent from '../components/Map/MapComponent';
import { useLocation } from 'react-router-dom';
import './Home.css';
import SearchBar from "../components/SearchBar/SearchBar.jsx";

const Home = (route, navigation) => {
    const location = useLocation();
    const {type} = location.state || {};

    return (
        <div className={"home"}>
            <h1 className={"title"}>Project Horizon</h1>
            <MapComponent type={type}/>
        </div>
    );
};

export default Home;