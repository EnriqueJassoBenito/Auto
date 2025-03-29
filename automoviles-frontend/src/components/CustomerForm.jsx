import React, { useState, useEffect } from 'react';
import { Modal, Button, Form } from 'react-bootstrap';

const CustomerForm = ({ show, onHide, onSubmit, initialData }) => {
  const [formData, setFormData] = useState({
    name: '',
    surname: '',
    phone: '',
    email: '',
    username: '',
    password: '',
  });

  // Resetear o cargar datos al cambiar initialData
  useEffect(() => {
    if (initialData) {
      setFormData(initialData);
    } else {
      setFormData({
        name: '',
        surname: '',
        phone: '',
        email: '',
        username: '',
        password: '',
      });
    }
  }, [initialData, show]); // Agregamos show como dependencia

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  // En el handleSubmit del formulario, no incluyas customerId si no es necesario
const handleSubmit = (e) => {
  e.preventDefault();
  const dataToSubmit = {
      ...formData,
      customerId: undefined // No enviar el campo si no tiene valor
  };
  onSubmit(dataToSubmit);
  onHide();
};

  return (
    <Modal show={show} onHide={onHide}>
      <Modal.Header closeButton>
        <Modal.Title>{initialData ? 'Editar Cliente' : 'Agregar Cliente'}</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form onSubmit={handleSubmit}>
          <Form.Group className="mb-3">
            <Form.Label>Nombre</Form.Label>
            <Form.Control
              type="text"
              name="name"
              value={formData.name}
              onChange={handleChange}
              required
            />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Apellido</Form.Label>
            <Form.Control
              type="text"
              name="surname"
              value={formData.surname}
              onChange={handleChange}
              required
            />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Teléfono</Form.Label>
            <Form.Control
              type="text"
              name="phone"
              value={formData.phone}
              onChange={handleChange}
              required
            />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Correo Electrónico</Form.Label>
            <Form.Control
              type="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              required
            />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Nombre de Usuario</Form.Label>
            <Form.Control
              type="text"
              name="username"
              value={formData.username}
              onChange={handleChange}
              required
            />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Contraseña</Form.Label>
            <Form.Control
              type="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              required={!initialData} // No requerido para edición
            />
          </Form.Group>
          <Button type="submit" variant="dark">
            {initialData ? 'Guardar Cambios' : 'Agregar Cliente'}
          </Button>
        </Form>
      </Modal.Body>
    </Modal>
  );
};

export default CustomerForm;