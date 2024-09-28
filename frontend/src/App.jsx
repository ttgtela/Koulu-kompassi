import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './pages/Home';
import Header from './components/Header/Header';
import './App.css'

function App() {
    return (
        <Router>
            <div className="App">
                <Header />
                <div className="content">
                    <Routes>
                        <Route path="/" element={<Home />} />
                    </Routes>
                </div>
            </div>
        </Router>
    );
}


export default App
