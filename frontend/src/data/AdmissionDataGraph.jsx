import React, { useEffect, useState } from 'react';
import {
    BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer
} from 'recharts';
import {AdmissionData} from "./AdmissionData.jsx";

const AdmissionChart = ({ name, field,admissionMethod }) => {
    const [data, setData] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            const result = await AdmissionData(name, field,admissionMethod);
            if (result) {
                const transformedData = Object.keys(result).map(year => ({
                    year: year,
                    requiredPoints: result[year].requiredPoints
                }));
                setData(transformedData);
            }
        };
        fetchData();
    }, [name, field,admissionMethod]);


    return (
        <ResponsiveContainer width="100%" height={400}>
            <BarChart data={data} margin={{ top: 20, right: 30, left: 20, bottom: 5 }}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="year" />
                <YAxis />
                <Tooltip />
                <Legend />
                <Bar dataKey="requiredPoints" fill="#8884d8" name="Required Points" />
            </BarChart>
        </ResponsiveContainer>
    );
};

export default AdmissionChart;