import React from 'react';

const RatingFilter = ({ ratings, selectedRatings, setSelectedRatings }) => {
    const handleCheckboxChange = (rating) => {
        if (selectedRatings.includes(rating)) {
            setSelectedRatings(selectedRatings.filter((f) => f !== rating));
        } else {
            setSelectedRatings([...selectedRatings, rating]);
        }
    };

    return (
        <div className="filter-panel">
            <h3>Filter by Rating</h3>
            {ratings.map((field) => (
                <div key={field}>
                    <input
                        type="checkbox"
                        id={field}
                        name={field}
                        value={field}
                        checked={selectedRatings.includes(field)}
                        onChange={() => handleCheckboxChange(field)}
                    />
                    <label htmlFor={field}>{field}</label>
                </div>
            ))}
        </div>
    );
};

export default RatingFilter;
