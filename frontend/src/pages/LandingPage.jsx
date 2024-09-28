import React from 'react';
import {Button, Row, Col } from "react-bootstrap";
import {useNavigate} from "react-router-dom";
import '../LandingPage.css';


const LandingPage = () => {
    const navigate = useNavigate();
    return (
        <div className="landing-container" style={{padding: "50px"}}>
            <header className="text-center landing-title">
            <h1 className="landing-title">High School & College Ranking App</h1>
            <h1 className="landing-title"> Project Horizon</h1>
            </header>
            <p>Select an option to view rankings on the map:</p>
            <div className="d-flex justify-content-center align-items-center landing-buttons">
                <Row className="text-center">
                    <Col>
                        <Button variant="primary" size="lg" className="buttons" onClick={() => navigate('/home')}>
                            High Schools
                        </Button>
                        <Button variant="primary" size="lg" className="buttons" onClick={() => navigate('/home')}>
                            Colleges
                        </Button>
                    </Col>
                </Row>
            </div>
        </div>
    );
}
export default LandingPage;