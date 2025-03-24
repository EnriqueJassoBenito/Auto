import React, { useState, useEffect } from 'react';
import { Button } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPlus } from '@fortawesome/free-solid-svg-icons'; // Icono de añadir
import axios from 'axios';
import CustomerForm from '../components/CustomerForm';
import CustomerTable from '../components/CustomerTable';
import Alert from '../components/Alert'; // Importar la función Alert

const Customers = () => {
  const [customers, setCustomers] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [selectedCustomer, setSelectedCustomer] = useState(null);

  // Obtener el token del localStorage
  const getToken = () => {
    const token = localStorage.getItem('token');
    if (!token) {
      Alert('Error', 'No se encontró el token de autenticación', 'error');
      return null;
    }
    return `Bearer ${token}`; // Agregar el prefijo "Bearer"
  };

  // Configurar axios para incluir el token en las solicitudes
  const axiosWithToken = axios.create({
    baseURL: 'http://localhost:8080/api',
    headers: {
      Authorization: getToken(), // Incluir el token en el header
    },
  });

  // Obtener la lista de clientes
  const fetchCustomers = async () => {
    try {
      const response = await axiosWithToken.get('/customers');
      setCustomers(response.data);
    } catch (error) {
      Alert('Error', 'No se pudo obtener la lista de clientes', 'error');
    }
  };

  useEffect(() => {
    fetchCustomers();
  }, []);

  // Función para extraer el id, username y rol del token
  const getEmployeeInfoFromToken = () => {
    const token = localStorage.getItem('token');
    if (!token) return null;

    // Dividir el token en partes
    const parts = token.split('.');
    if (parts.length >= 4) {
      return {
        id: parts[1],       // El id está en la segunda parte
        username: parts[2], // El username está en la tercera parte
        role: parts[3],     // El rol está en la cuarta parte
      };
    }
    return null;
  };

  // Agregar o editar un cliente
  const handleSubmit = async (data) => {
    try {
      const employeeInfo = getEmployeeInfoFromToken();
      if (!employeeInfo) {
        Alert('Error', 'No se pudo obtener la información del empleado desde el token', 'error');
        return;
      }

      // Agregar el employeeId al cuerpo de la solicitud
      const requestData = {
        ...data,
        employeeId: employeeInfo.id, // Usar el id extraído del token
      };

      if (selectedCustomer) {
        await axiosWithToken.put(`/customers/${selectedCustomer.id}`, requestData);
        Alert('Éxito', 'Cliente actualizado correctamente', 'success');
      } else {
        await axiosWithToken.post('/customers', requestData);
        Alert('Éxito', 'Cliente agregado correctamente', 'success');
      }
      fetchCustomers();
    } catch (error) {
      Alert('Error', 'No se pudo guardar el cliente', 'error');
    }
  };

  // Eliminar un cliente
  const handleDelete = async (id) => {
    try {
      const result = await Alert(
        'Confirmar Eliminación',
        '¿Estás seguro de que deseas eliminar este cliente?',
        'warning',
        true // Agregar un parámetro para mostrar botones de confirmación
      );

      if (result.isConfirmed) {
        await axiosWithToken.delete(`/customers/${id}`);
        Alert('Éxito', 'Cliente eliminado correctamente', 'success');
        fetchCustomers();
      }
    } catch (error) {
      Alert('Error', 'No se pudo eliminar el cliente', 'error');
    }
  };

  return (
    <div>
      <h1>Gestión de Clientes</h1>
      <Button
        variant="dark"
        onClick={() => setShowForm(true)}
        style={{
          marginBottom: '2px',
          marginTop: '10px',
          transition: 'transform 0.3s ease, background-color 0.3s ease',
        }}
        onMouseEnter={(e) => (e.currentTarget.style.transform = 'scale(1.5)')}
        onMouseLeave={(e) => (e.currentTarget.style.transform = 'scale(1)')}
        onMouseDown={(e) => (e.currentTarget.style.transform = 'scale(0.9)')}
        onMouseUp={(e) => (e.currentTarget.style.transform = 'scale(1.1)')}
      >
        <FontAwesomeIcon icon={faPlus} /> {/* Icono de añadir */}
      </Button>
      <CustomerTable
        customers={customers}
        onEdit={(customer) => {
          setSelectedCustomer(customer);
          setShowForm(true);
        }}
        onDelete={handleDelete} // Usar la función handleDelete
      />
      <CustomerForm
        show={showForm}
        onHide={() => {
          setShowForm(false);
          setSelectedCustomer(null);
        }}
        onSubmit={handleSubmit}
        initialData={selectedCustomer}
      />
    </div>
  );
};

export default Customers;