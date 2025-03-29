import React from 'react';
import { Modal, Button, Badge } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faInfoCircle } from '@fortawesome/free-solid-svg-icons';

const CarDetailsModal = ({ show, onHide, car, customerDetails, loading, statusVariant }) => {
  return (
    <Modal show={show} onHide={onHide} centered>
      <Modal.Header closeButton>
        <Modal.Title>
          <FontAwesomeIcon icon={faInfoCircle} className="me-2" />
          Detalles de Venta
        </Modal.Title>
      </Modal.Header>
      <Modal.Body>
        {loading ? (
          <div className="text-center py-4">
            <div className="spinner-border text-primary" role="status">
              <span className="visually-hidden">Cargando...</span>
            </div>
            <p>Cargando detalles...</p>
          </div>
        ) : (
          <>
            <div className="mb-4">
              <h5>Información del Auto</h5>
              <p><strong>Modelo:</strong> {car.model}</p>
              <p><strong>Marca:</strong> {car.brand?.name || 'No especificada'}</p>
              <p><strong>Color:</strong> {car.color}</p>
              <p><strong>Precio:</strong> ${car.purchasePrice?.toLocaleString()}</p>
              <p><strong>Estado:</strong> <Badge pill bg={statusVariant}>{car.status}</Badge></p>
            </div>

            {customerDetails?.customer && (
              <div>
                <h5>Vendido a:</h5>
                <p><strong>Nombre:</strong> {customerDetails.customer.name} {customerDetails.customer.surname}</p>
                <p><strong>Email:</strong> {customerDetails.customer.email}</p>
                <p><strong>Teléfono:</strong> {customerDetails.customer.phone || 'No especificado'}</p>
               
              </div>
            )}
          </>
        )}
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={onHide}>
          Cerrar
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default CarDetailsModal;