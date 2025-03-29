import React, { useState, useEffect } from 'react';
import { Button } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPlus } from '@fortawesome/free-solid-svg-icons';
import axios from 'axios';
import ServiceForm from '../components/ServiceForm';
import ServiceTable from '../components/ServiceTable';
import Alert from '../components/Alert';

const Services = () => {
  const [services, setServices] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [selectedService, setSelectedService] = useState(null);
  const [formKey, setFormKey] = useState(0);

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

  // Obtener servicios
  const fetchServices = async () => {
    try {
      const response = await axiosWithToken.get('/services');
      setServices(response.data);
    } catch (error) {
      Alert('Error', 'No se pudo obtener la lista de servicios', 'error');
    }
  };

  useEffect(() => {
    fetchServices();
  }, []);

  // Manejar submit del formulario
  const handleSubmit = async (data) => {
    try {
      // Convertir price a número
      const requestData = {
        ...data,
        price: parseFloat(data.price)
      };

      if (selectedService) {
        await axiosWithToken.put(`/services/${selectedService.id}`, requestData);
        Alert('Éxito', 'Servicio actualizado correctamente', 'success');
      } else {
        await axiosWithToken.post('/services', requestData);
        Alert('Éxito', 'Servicio agregado correctamente', 'success');
      }

      fetchServices();
      setSelectedService(null);
      setFormKey(prevKey => prevKey + 1);
      setShowForm(false);
    } catch (error) {
      Alert('Error', 'No se pudo guardar el servicio', 'error');
    }
  };

  // Eliminar servicio
  const handleDelete = async (id) => {
    try {
      const result = await Alert(
        'Confirmar Eliminación',
        '¿Estás seguro de eliminar este servicio?',
        'warning',
        true
      );

      if (result.isConfirmed) {
        await axiosWithToken.delete(`/services/${id}`);
        Alert('Éxito', 'Servicio eliminado correctamente', 'success');
        fetchServices();
      }
    } catch (error) {
      Alert('Error', 'No se pudo eliminar el servicio', 'error');
    }
  };

  return (
    <div>
      <h1>Gestión de Servicios</h1>
      <Button
        variant="dark"
        onClick={() => {
          setSelectedService(null);
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
        Añadir nuevo servicio <FontAwesomeIcon icon={faPlus} />
      </Button>
      <ServiceTable
        services={services}
        onEdit={(service) => {
          setSelectedService(service);
          setShowForm(true);
        }}
        onDelete={handleDelete}
      />
      <ServiceForm
        key={formKey}
        show={showForm}
        onHide={() => {
          setShowForm(false);
          setSelectedService(null);
        }}
        onSubmit={handleSubmit}
        initialData={selectedService}
      />
    </div>
  );
};

export default Services;