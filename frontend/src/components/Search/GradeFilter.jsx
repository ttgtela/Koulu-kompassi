// src/components/GradeFilter.jsx
import React, { useState, useEffect } from 'react';

const GradeFilter = () => {
    const subjects = [
        'Äidinkieli',
        'Biologia',
        'Filosofia',
        'Fysiikka',
        'Historia',
        'Kemia',
        'Kieli, pitkä',
        'Kieli, keskipitkä',
        'Kieli, lyhyt',
        'Maantiede',
        'Matematiikka, pitkä',
        'Matematiikka, lyhyt',
        'Psykologia',
        'Terveystieto',
        'Uskonto',
        'Elämänkatsomusoppi',
        'Yhteiskuntaoppi',
    ];

    const gradeOptions = [
        { value: '', label: '-' },
        { value: 'L', label: 'L' },
        { value: 'E', label: 'E' },
        { value: 'M', label: 'M' },
        { value: 'C', label: 'C' },
        { value: 'B', label: 'B' },
        { value: 'A', label: 'A' },
        { value: 'I', label: 'I' },
    ];

    const [selectedGrades, setSelectedGrades] = useState(
        subjects.reduce((acc, subject) => {
            acc[subject] = '';
            return acc;
        }, {})
    );

    const [fields, setFields] = useState([]);
    const [selectedField, setSelectedField] = useState('');

    useEffect(() => {
        const fetchFields = async () => {
            try {
                const response = await fetch('http://localhost:8080/api/points');
                if (!response.ok) {
                    throw new Error(`Error: ${response.status} ${response.statusText}`);
                }
                const data = await response.json();
                const fieldNames = data.map(item => item.field);
                setFields(fieldNames);
            } catch (err) {
                console.error('Error fetching fields:', err);
            }
        };

        fetchFields();
    }, []);

    // Handle grade selection change
    const handleGradeChange = (subject, value) => {
        setSelectedGrades(prevGrades => ({
            ...prevGrades,
            [subject]: value,
        }));
    };

    const handleFieldChange = (e) => {
        setSelectedField(e.target.value);
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        const submissionData = {
            selectedField,
            grades: selectedGrades,
        };
        console.log('Submission Data:', submissionData);
    };

    return (
        <div>
            <h2>Grades</h2>
            <form onSubmit={handleSubmit}>
                <div style={{ marginBottom: '20px'}}>
                    <label htmlFor="field" style={{ marginRight: '0px' }}>
                        Select Field:
                    </label>
                    <select
                            style={{marginLeft: '10px', width: '50%'}}
                            id="field"
                            value={selectedField}
                            onChange={handleFieldChange}
                        >
                            <option value="">-</option>
                            {fields.map((field, index) => (
                                <option key={index} value={field}>
                                    {field}
                                </option>
                            ))}
                    </select>
                </div>

                {subjects.map(subject => (
                    <div key={subject} style={{marginBottom: '10px' }}>
                        <label htmlFor={subject} style={{ marginRight: '10px' }}>
                            {subject}:
                        </label>
                        <select
                            id={subject}
                            value={selectedGrades[subject]}
                            onChange={(e) => handleGradeChange(subject, e.target.value)}
                        >
                            {gradeOptions.map(option => (
                                <option
                                    key={option.value || `${subject}-empty`}
                                    value={option.value}
                                >
                                    {option.label}
                                </option>
                            ))}
                        </select>
                    </div>
                ))}

                <button type="submit">Submit</button>
            </form>
        </div>
    );
};

export default GradeFilter;
