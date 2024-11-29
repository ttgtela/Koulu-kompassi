export const FieldData = async () => {
    try {
        const response = await fetch('http://localhost:8080/api/points');
        if (!response.ok) {
            throw new Error(`Error: ${response.status} ${response.statusText}`);
        }
        const data = await response.json();
        return data.map(item => item.field);
    } catch (err) {
        console.error('Error fetching fields:', err);
    }
}
