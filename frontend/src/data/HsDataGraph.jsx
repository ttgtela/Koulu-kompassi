import React, { useEffect, useState } from 'react';
import {
    BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer
} from 'recharts';
import { HsExam } from './HsExam.jsx';

const HsChart = ({ school, year }) => {
    const [data, setData] = useState([]);

    const isDataEmpty = () => !data || data.length === 0;

    useEffect(() => {
        const fetchData = async () => {
            const result = await HsExam(school, year);
            let transformedData;
            if (result) {
                if (Object.keys(result).length===1){
                    transformedData=null
                    setData(transformedData);
                }
                else {
                    transformedData = Object.keys(result).map(grade => ({
                        grade: grade,
                        numberOfGrades: result[grade]
                    }));
                    setData(transformedData);
                }
            }
        };
        fetchData();
    }, [school, year]);
    if (isDataEmpty()){
        return null;
    }
    else {

        return (
            <ResponsiveContainer width="100%" height={400}>
                <BarChart data={data} margin={{top: 20, right: 30, left: 20, bottom: 5}}>
                    <CartesianGrid strokeDasharray="3 3"/>
                    <XAxis dataKey="grade"/>
                    <YAxis/>
                    <Tooltip/>
                    <Legend/>
                    <Bar dataKey="numberOfGrades" fill="#8884d8" name="Grades"/>
                </BarChart>
            </ResponsiveContainer>
        );
    }
};

export default HsChart;
