import React from 'react';
import { Table, Button } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faEdit, faTrash } from '@fortawesome/free-solid-svg-icons';
import './Styles/CustomerTable.css'; // Archivo CSS para animaciones y estilos

const CustomerTable = ({ customers, onEdit, onDelete }) => {
  return (
    <Table striped bordered hover className="customer-table">
      <thead>
        <tr>
          <th>Nombre</th>
          <th>Apellido</th>
          <th>Teléfono</th>
          <th>Correo Electrónico</th>
          <th>Acciones</th>
        </tr>
      </thead>
      <tbody>
        {customers.map((customer) => (
          <tr key={customer.id} className="customer-row">
            <td>{customer.name}</td>
            <td>{customer.surname}</td>
            <td>{customer.phone}</td>
            <td>{customer.email}</td>
            <td>
              <Button
                variant="dark"
                onClick={() => onEdit(customer)}
                className="action-button"
              >
                <FontAwesomeIcon icon={faEdit} /> {/* Icono de editar */}
              </Button>{' '}
              <Button
                variant="dark"
                onClick={() => onDelete(customer.id)}
                className="action-button"
              >
                <FontAwesomeIcon icon={faTrash} /> {/* Icono de eliminar */}
              </Button>
            </td>
          </tr>
        ))}
      </tbody>
    </Table>
  );
};

export default CustomerTable;