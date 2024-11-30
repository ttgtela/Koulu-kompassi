import stringSimilarity, {compareTwoStrings} from 'string-similarity';

export const CombinedData = async (topField) => {
    try {
        const response = await fetch('http://localhost:8080/api/combined');
        if (!response.ok) {
            throw new Error(`Error: ${response.status} ${response.statusText}`);
        }
        const data = await response.json();
        let combinedData = [];
        try {
            data.forEach(function(university) {
                if(compareTwoStrings(university.topField, topField) < 0.5) return;
                console.log("data: " + university.topField);
                console.log("combinedData: " + topField);
                university.universityPoints.forEach(function(pointEntry) {
                    let fieldsPoints = pointEntry;
                    combinedData.push(fieldsPoints);
                });
                throw new Error('This is a filthy hack, I am sorry :( ');
            });
        }
        catch {
            return combinedData
        }

    } catch (err) {
        console.error('Error fetching fields:', err);
    }
}