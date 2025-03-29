import React, { useState, useEffect } from 'react';
import { Button } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPlus, faTags } from '@fortawesome/free-solid-svg-icons';
import axios from 'axios';
import BrandForm from '../components/BrandForm';
import BrandTable from '../components/BrandTable';
import Alert from '../components/Alert';

const Brands = () => {
  const [brands, setBrands] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [selectedBrand, setSelectedBrand] = useState(null);
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

  // Obtener marcas
  const fetchBrands = async () => {
    try {
      const response = await axiosWithToken.get('/brands');
      setBrands(response.data);
    } catch (error) {
      Alert('Error', 'No se pudo obtener la lista de marcas', 'error');
    }
  };

  useEffect(() => {
    fetchBrands();
  }, []);

  // Manejar submit del formulario
  const handleSubmit = async (data) => {
    try {
      if (selectedBrand) {
        await axiosWithToken.put(`/brands/${selectedBrand.id}`, data);
        Alert('Éxito', 'Marca actualizada correctamente', 'success');
      } else {
        await axiosWithToken.post('/brands', data);
        Alert('Éxito', 'Marca agregada correctamente', 'success');
      }

      fetchBrands();
      setSelectedBrand(null);
      setFormKey(prevKey => prevKey + 1);
      setShowForm(false);
    } catch (error) {
      Alert('Error', 'No se pudo guardar la marca', 'error');
    }
  };

  // Eliminar marca
  const handleDelete = async (id) => {
    try {
      const result = await Alert(
        'Confirmar Eliminación',
        '¿Estás seguro de eliminar esta marca?',
        'warning',
        true
      );

      if (result.isConfirmed) {
        await axiosWithToken.delete(`/brands/${id}`);
        Alert('Éxito', 'Marca eliminada correctamente', 'success');
        fetchBrands();
      }
    } catch (error) {
      Alert('Error', 'No se pudo eliminar la marca', 'error');
    }
  };

  return (
    <div>
      <h1>Gestión de Marcas</h1>
      <Button
        variant="dark"
        onClick={() => {
          setSelectedBrand(null);
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
        Añadir nueva marca <FontAwesomeIcon icon={faTags} />
      </Button>
      <BrandTable
        brands={brands}
        onEdit={(brand) => {
          setSelectedBrand(brand);
          setShowForm(true);
        }}
        onDelete={handleDelete}
      />
      <BrandForm
        key={formKey}
        show={showForm}
        onHide={() => {
          setShowForm(false);
          setSelectedBrand(null);
        }}
        onSubmit={handleSubmit}
        initialData={selectedBrand}
      />
    </div>
  );
};

export default Brands;