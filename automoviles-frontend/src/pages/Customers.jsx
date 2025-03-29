import React, { useState, useEffect } from 'react';
import { Button } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUserPlus } from '@fortawesome/free-solid-svg-icons';
import axios from 'axios';
import CustomerForm from '../components/CustomerForm';
import CustomerTable from '../components/CustomerTable';
import Alert from '../components/Alert';

const Customers = () => {
  const [customers, setCustomers] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [selectedCustomer, setSelectedCustomer] = useState(null);
  const [formKey, setFormKey] = useState(0); // Clave para resetear el formulario

  // Obtener el token del localStorage
  const getToken = () => {
    const token = localStorage.getItem('token');
    if (!token) {
      Alert('Error', 'No se encontró el token de autenticación', 'error');
      return null;
    }
    return `Bearer ${token}`;
  };

  // Configurar axios
  const axiosWithToken = axios.create({
    baseURL: 'http://localhost:8080/api',
    headers: {
      Authorization: getToken(),
    },
  });

  // Obtener clientes
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

  // Extraer info del token
  const getEmployeeInfoFromToken = () => {
    const token = localStorage.getItem('token');
    if (!token) return null;

    const cleanToken = token.replace('Bearer ', '').trim();
    const parts = cleanToken.split('.');
    
    if (parts.length === 3) {
      return {
        id: parts[0],
        username: parts[1],
        role: parts[2]
      };
    }
    return null;
  };

  // Manejar submit del formulario
  const handleSubmit = async (data) => {
    try {
      const employeeInfo = getEmployeeInfoFromToken();
      if (!employeeInfo) {
        Alert('Error', 'No se pudo obtener la información del empleado', 'error');
        return;
      }

      const requestData = {
        ...data,
        employeeId: employeeInfo.id,
      };

      if (selectedCustomer) {
        await axiosWithToken.put(`/customers/${selectedCustomer.id}`, requestData);
        Alert('Éxito', 'Cliente actualizado correctamente', 'success');
      } else {
        await axiosWithToken.post('/customers', requestData);
        Alert('Éxito', 'Cliente agregado correctamente', 'success');
      }

      fetchCustomers();
      setSelectedCustomer(null);
      setFormKey(prevKey => prevKey + 1); // Forzar reset del formulario
      setShowForm(false);
    } catch (error) {
      Alert('Error', 'No se pudo guardar el cliente', 'error');
    }
  };

  // Eliminar cliente
  const handleDelete = async (id) => {
    try {
      const result = await Alert(
        'Confirmar Eliminación',
        '¿Estás seguro de eliminar este cliente?',
        'warning',
        true
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
        onClick={() => {
          setSelectedCustomer(null);
          setShowForm(true);
        }}
        style={{
          marginBottom: '2px',
          marginTop: '10px',
          transition: 'transform 0.3s ease, background-color 0.3s ease',
        }}
        onMouseEnter={(e) => (e.currentTarget.style.transform = 'scale(1.2)')}
        onMouseLeave={(e) => (e.currentTarget.style.transform = 'scale(1)')}
        onMouseDown={(e) => (e.currentTarget.style.transform = 'scale(0.9)')}
        onMouseUp={(e) => (e.currentTarget.style.transform = 'scale(1.1)')}
      >
       Añadir nuevo cliente <FontAwesomeIcon icon={faUserPlus} />
      </Button>
      <CustomerTable
        customers={customers}
        onEdit={(customer) => {
          setSelectedCustomer(customer);
          setShowForm(true);
        }}
        onDelete={handleDelete}
      />
      <CustomerForm
        key={formKey} // Key para resetear el componente
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