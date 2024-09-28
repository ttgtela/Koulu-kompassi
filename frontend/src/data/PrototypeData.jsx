const Universities = [
    {
        id: 1,
        name: "Tampere University of Technology",
        position: [61.45000766895691, 23.856790847309647],
        starRating: 5.0,
        numberOfStudents: 0,
        fields: ["Engineering", "Science"],
    },
    {
        id: 2,
        name: "Tampere University Central Campus",
        position: [61.49460932307555, 23.781481032491055],
        starRating: 4.0,
        numberOfStudents: 15000,
        fields: ["Business", "Science"],
    },
];

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

const getData = (type) => {
    if(type === "college") {
        return Universities;
    }
    else if(type === "high_school") {
        return HighSchools;
    }
    else {
        return null;
    }
};

export default getData;