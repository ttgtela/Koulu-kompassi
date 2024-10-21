import {useEffect, useState} from "react";
import error from "eslint-plugin-react/lib/util/error.js";

const Universities = async () => {
    try {
        const response = await fetch("http://localhost:8080/api/coordinates");
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        const result = await response.json();
        return result;
    } catch (error) {
        console.error("Error fetching coordinates for universities:", error);
        return null;
    }
};

const HighSchools = [
    {
        id: 1,
        name: "Kalevan lukioo",
        position: [61.497528, 23.786389],
        starRating: 5.0,
        numberOfStudents: 0,
        fields: ["Music"],
    },
    {
        id: 2,
        name: "Tampereen teknillinen lukio",
        position: [61.457306, 23.853889],
        starRating: 4.0,
        numberOfStudents: 15000,
        fields: ["Science"],
    },
];

const getData = async (type) => {
    if (type === "college") {
        return await Universities();
    } else if (type === "high_school") {
        return HighSchools;
    } else {
        return null;
    }
};

export default getData;