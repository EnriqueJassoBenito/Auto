import React, { useState, useEffect } from 'react';
import { Modal, Button, Form } from 'react-bootstrap';

const EmployeeForm = ({ show, onHide, onSubmit, initialData, roles }) => {
  const [formData, setFormData] = useState({
    username: '',
    password: '',
    name: '',
    surname: '',
    roleId: '',
  });

  useEffect(() => {
    if (initialData) {
      setFormData({
        username: initialData.username,
        password: '', // No mostramos la contraseña en la edición
        name: initialData.name,
        surname: initialData.surname,
        roleId: initialData.roleId,
      });
    } else {
      setFormData({
        username: '',
        password: '',
        name: '',
        surname: '',
        roleId: '',
      });
    }
  }, [initialData, show]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    
    // Si estamos editando y no se cambió la contraseña, no la enviamos
    const dataToSubmit = initialData && !formData.password 
      ? { ...formData, password: undefined }
      : formData;
    
    onSubmit(dataToSubmit);
  };

  return (
    <Modal show={show} onHide={onHide}>
      <Modal.Header closeButton>
        <Modal.Title>{initialData ? 'Editar Empleado' : 'Agregar Empleado'}</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form onSubmit={handleSubmit}>
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
              placeholder={initialData ? "Dejar en blanco para no cambiar" : ""}
            />
          </Form.Group>

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
            <Form.Label>Rol</Form.Label>
            <Form.Select
              name="roleId"
              value={formData.roleId}
              onChange={handleChange}
              required
            >
              <option value="">Seleccione un rol</option>
              {roles.map(role => (
                <option key={role.id} value={role.id}>
                  {role.name}
                </option>
              ))}
            </Form.Select>
          </Form.Group>

          <Button type="submit" variant="dark">
            {initialData ? 'Guardar Cambios' : 'Agregar Empleado'}
          </Button>
        </Form>
      </Modal.Body>
    </Modal>
  );
};

export default EmployeeForm;