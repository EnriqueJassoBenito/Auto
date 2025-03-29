import React, { useState, useEffect } from 'react';
import { Modal, Button, Form, Alert as BootstrapAlert } from 'react-bootstrap';
import axios from 'axios';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUserCheck, faHandshake, faCheckCircle } from '@fortawesome/free-solid-svg-icons';

const CarAssignmentModal = ({ show, onHide, car, refreshData, isSellingInitial }) => {
  const [customers, setCustomers] = useState([]);
  const [selectedCustomerId, setSelectedCustomerId] = useState('');
  const [isSelling, setIsSelling] = useState(isSellingInitial || false);
  const [error, setError] = useState(null);
  const [isAlreadyAssigned, setIsAlreadyAssigned] = useState(false);
  const [isLoading, setIsLoading] = useState(false);

  const axiosInstance = axios.create({
    baseURL: 'http://localhost:8080/api',
    headers: {
      Authorization: `Bearer ${localStorage.getItem('token')}`
    }
  });

  useEffect(() => {
    const fetchCustomers = async () => {
      try {
        const response = await axiosInstance.get('/customers');
        setCustomers(response.data);
        
        if (car.customerId) {
          setIsAlreadyAssigned(true);
          setSelectedCustomerId(car.customerId);
        } else {
          setIsAlreadyAssigned(false);
          setSelectedCustomerId('');
        }
      } catch (err) {
        setError('Error al cargar clientes');
        console.error(err);
      }
    };

    if (show) {
      fetchCustomers();
      setIsSelling(isSellingInitial || false);
      setError(null);
    }
  }, [show, car]);

  const handleCompleteSale = async () => {
    setIsLoading(true);
    try {
      await axiosInstance.patch(`/cars/${car.id}/status`, {
        status: 'VENDIDO'
      });
      
      if (refreshData) await refreshData();
      onHide();
    } catch (err) {
      setError(err.response?.data?.message || 'Error al completar la venta');
      console.error(err);
    } finally {
      setIsLoading(false);
    }
  };

  const handleAssign = async () => {
    setIsLoading(true);
    try {
      if (isSelling) {
        if (!isAlreadyAssigned) {
          await axiosInstance.patch(`/cars/${car.id}/assign?customerId=${selectedCustomerId}`);
        }
        await axiosInstance.patch(`/cars/${car.id}/status`, {
          status: 'VENDIDO'
        });
      } else {
        await axiosInstance.patch(`/cars/${car.id}/assign?customerId=${selectedCustomerId}`);
      }
      
      if (refreshData) await refreshData();
      onHide();
    } catch (err) {
      setError(err.response?.data?.message || 'Error al procesar la operación');
      console.error(err);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <Modal show={show} onHide={onHide} centered>
      <Modal.Header closeButton>
        <Modal.Title>
          <FontAwesomeIcon 
            icon={isAlreadyAssigned ? faCheckCircle : (isSelling ? faHandshake : faUserCheck)} 
            className="me-2" 
          />
          {isAlreadyAssigned ? 'Completar Venta' : (isSelling ? 'Vender Auto' : 'Asignar Auto')}
        </Modal.Title>
      </Modal.Header>
      <Modal.Body>
        {error && <BootstrapAlert variant="danger">{error}</BootstrapAlert>}
        
        <Form.Group className="mb-3">
          <Form.Label>Auto a {isAlreadyAssigned ? 'vender' : (isSelling ? 'vender' : 'asignar')}</Form.Label>
          <Form.Control 
            type="text" 
            value={`${car.model} (${car.brand?.name})`} 
            readOnly 
          />
        </Form.Group>

        {isAlreadyAssigned ? (
          <Form.Group className="mb-3">
            <Form.Label>Cliente asignado</Form.Label>
            <Form.Control
              type="text"
              value={customers.find(c => c.id === car.customerId)?.name || 'Cliente no encontrado'}
              readOnly
            />
          </Form.Group>
        ) : (
          <Form.Group className="mb-3">
            <Form.Label>Seleccionar Cliente</Form.Label>
            <Form.Select
              value={selectedCustomerId}
              onChange={(e) => setSelectedCustomerId(e.target.value)}
              required={!isAlreadyAssigned}
              disabled={isAlreadyAssigned || isLoading}
            >
              <option value="">Seleccione un cliente</option>
              {customers.map(customer => (
                <option key={customer.id} value={customer.id}>
                  {customer.name} {customer.surname} - {customer.email}
                </option>
              ))}
            </Form.Select>
          </Form.Group>
        )}

        {!isAlreadyAssigned && (
          <Form.Check 
            type="switch"
            id="selling-switch"
            label="Marcar como venta"
            checked={isSelling}
            onChange={() => setIsSelling(!isSelling)}
            className="mb-3"
            disabled={isLoading}
          />
        )}
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={onHide} disabled={isLoading}>
          Cancelar
        </Button>
        {isAlreadyAssigned ? (
          <Button 
            variant="success" 
            onClick={handleCompleteSale}
            disabled={isLoading}
          >
            {isLoading ? 'Procesando...' : (
              <>
                <FontAwesomeIcon icon={faCheckCircle} className="me-2" />
                Marcar como Vendido
              </>
            )}
          </Button>
        ) : (
          <Button 
            variant={isSelling ? "success" : "primary"} 
            onClick={handleAssign}
            disabled={!selectedCustomerId || isLoading}
          >
            {isLoading ? 'Procesando...' : (
              isSelling ? 'Confirmar Venta' : 'Asignar Cliente'
            )}
          </Button>
        )}
      </Modal.Footer>
    </Modal>
  );
};

export default CarAssignmentModal;