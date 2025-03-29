import React from 'react';
import { Row, Col } from 'react-bootstrap';
import CarCard from './CarCard';

const CarsList = ({ cars, onEdit, onDelete, onRefresh, imageVersion }) => {
  if (cars.length === 0) {
    return (
      <div className="text-center py-5">
        <h4>No hay autos registrados</h4>
        <p>Agrega tu primer auto usando el botón "+"</p>
      </div>
    );
  }

  return (
    <Row xs={1} md={2} lg={3} xl={4} className="g-4">
      {cars.map((car) => (
        <Col key={car.id}>
          <CarCard 
            car={car}
            onEdit={onEdit}
            onDelete={onDelete}
            onRefresh={onRefresh}
            imageVersion={imageVersion}
          />
        </Col>
      ))}
    </Row>
  );
};
  
export default CarsList;