import React, { useState, useEffect } from 'react';
import { Modal, Button, Form } from 'react-bootstrap';

const BrandForm = ({ show, onHide, onSubmit, initialData }) => {
  const [formData, setFormData] = useState({
    name: '',
  });

  useEffect(() => {
    if (initialData) {
      setFormData(initialData);
    } else {
      setFormData({
        name: '',
      });
    }
  }, [initialData, show]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit(formData);
  };

  return (
    <Modal show={show} onHide={onHide}>
      <Modal.Header closeButton>
        <Modal.Title>{initialData ? 'Editar Marca' : 'Agregar Marca'}</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form onSubmit={handleSubmit}>
          <Form.Group className="mb-3">
            <Form.Label>Nombre de la Marca</Form.Label>
            <Form.Control
              type="text"
              name="name"
              value={formData.name}
              onChange={handleChange}
              required
            />
          </Form.Group>
          <Button type="submit" variant="dark">
            {initialData ? 'Guardar Cambios' : 'Agregar Marca'}
          </Button>
        </Form>
      </Modal.Body>
    </Modal>
  );
};

export default BrandForm;