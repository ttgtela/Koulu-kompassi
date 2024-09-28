import React from 'react';

const FieldFilter = ({ fields, selectedFields, setSelectedFields }) => {
    const handleCheckboxChange = (field) => {
        if (selectedFields.includes(field)) {
            setSelectedFields(selectedFields.filter((f) => f !== field));
        } else {
            setSelectedFields([...selectedFields, field]);
        }
    };

    return (
        <div className="filter-panel">
            <h3>Filter by Field</h3>
            {fields.map((field) => (
                <div key={field}>
                    <input
                        type="checkbox"
                        id={field}
                        name={field}
                        value={field}
                        checked={selectedFields.includes(field)}
                        onChange={() => handleCheckboxChange(field)}
                    />
                    <label htmlFor={field}>{field}</label>
                </div>
            ))}
        </div>
    );
};

export default FieldFilter;
