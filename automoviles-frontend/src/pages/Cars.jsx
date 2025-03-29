import React, { useState, useEffect } from 'react';
import { Button } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCar, faPlus } from '@fortawesome/free-solid-svg-icons';
import axios from 'axios';
import CarForm from '../components/CarForm';
import CarsList from '../components/CarsList';
import Alert from '../components/Alert';
import Swal from 'sweetalert2';


const Cars = () => {
  const [cars, setCars] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [selectedCar, setSelectedCar] = useState(null);
  const [brands, setBrands] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [imageVersion, setImageVersion] = useState(0);

  const axiosInstance = axios.create({
    baseURL: 'http://localhost:8080/api',
  });

  axiosInstance.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    } else {
      Alert('Sesión expirada', 'Por favor inicie sesión nuevamente', 'error');
    }
    return config;
  });

  const combineData = (carsData, brandsData) => {
    return carsData.map(car => ({
      ...car,
      brand: brandsData.find(brand => brand.id === (car.brand?.id || car.brandId)) || null,
      imageName: car.imageName,
      imageVersion: Date.now()
    }));
  };

  const fetchData = async () => {
    try {
      setIsLoading(true);
      const [carsResponse, brandsResponse] = await Promise.all([
        axiosInstance.get(`/cars?t=${Date.now()}`),
        axiosInstance.get('/brands')
      ]);
      const combinedCars = combineData(carsResponse.data, brandsResponse.data);
      setCars(combinedCars);
      setBrands(brandsResponse.data);
    } catch (error) {
      console.error('Error al cargar datos:', error);
      Alert('Error', 'No se pudieron cargar los datos', 'error');
    } finally {
      setIsLoading(false);
    }
  };

  const refreshData = async () => {
    try {
      const [carsResponse, brandsResponse] = await Promise.all([
        axiosInstance.get(`/cars?t=${Date.now()}`),
        axiosInstance.get('/brands')
      ]);
      
      const combinedCars = combineData(carsResponse.data, brandsResponse.data);
      setCars(combinedCars);
      setBrands(brandsResponse.data);
      setImageVersion(prev => prev + 1);
    } catch (error) {
      console.error('Error al refrescar datos:', error);
      Alert('Error', 'No se pudieron actualizar los datos', 'error');
    }
  };

  const handleRefresh = async () => {
    try {
      await refreshData();
    } catch (error) {
      console.error('Error al refrescar:', error);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  const handleSubmit = async (data) => {
    try {
      const method = selectedCar ? 'put' : 'post';
      const url = selectedCar ? `/cars/${selectedCar.id}` : '/cars';
      
      const loadingAlert = Swal.fire({
        title: 'Procesando...',
        html: `${selectedCar ? 'Actualizando' : 'Creando'} el auto`,
        allowOutsideClick: false,
        didOpen: () => {
          Swal.showLoading();
        }
      });

      const { data: savedCar } = await axiosInstance[method](url, data);
      
      if (data.imageFile) {
        const formData = new FormData();
        formData.append('file', data.imageFile);
        
        await axiosInstance.post(
          `/cars/${savedCar.id}/image`,
          formData,
          { headers: { 'Content-Type': 'multipart/form-data' } }
        );
      }
      
      await refreshData();
      setShowForm(false);
      
      loadingAlert.close();
      
      Alert(
        selectedCar ? 'Auto actualizado' : 'Auto creado',
        selectedCar ? 'El auto se ha actualizado correctamente' : 'El auto se ha creado correctamente',
        'success'
      );
      
      return savedCar;
    } catch (error) {
      console.error('Error al guardar:', error);
      Alert('Error', error.response?.data?.message || 'Error al guardar', 'error');
      throw error;
    }
  };

  const handleDelete = async (id) => {
    try {
      const result = await Alert(
        '¿Estás seguro?',
        '¡No podrás revertir esta acción!',
        'warning',
        true
      );

      if (result.isConfirmed) {
        const loadingAlert = Swal.fire({
          title: 'Eliminando...',
          html: 'Eliminando el auto',
          allowOutsideClick: false,
          didOpen: () => {
            Swal.showLoading();
          }
        });

        await axiosInstance.delete(`/cars/${id}`);
        await refreshData();
        
        loadingAlert.close();
        
        Alert('Eliminado', 'El auto ha sido eliminado', 'success');
      }
    } catch (error) {
      console.error('Error al eliminar:', error);
      Alert('Error', 'No se pudo eliminar el auto', 'error');
    }
  };

  if (isLoading) {
    return (
      <div className="text-center mt-5">
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Cargando...</span>
        </div>
        <p>Cargando catálogo de autos...</p>
      </div>
    );
  }

  return (
    <div className="container mt-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h1>Gestión de Autos</h1>
        <Button
          variant="dark"
          onClick={() => {
            setSelectedCar(null);
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
          Añadir nuevo automóvil  <FontAwesomeIcon icon={faCar} />
        </Button>
      </div>

      <CarsList 
        cars={cars}
        onEdit={(car) => {
          setSelectedCar(car);
          setShowForm(true);
        }}
        onDelete={handleDelete}
        onRefresh={handleRefresh}
        imageVersion={imageVersion}
      />

      <CarForm
        show={showForm}
        onHide={() => {
          setShowForm(false);
          setSelectedCar(null);
        }}
        onSubmit={handleSubmit}
        initialData={selectedCar}
        brands={brands}
        refreshData={refreshData}
      />
    </div>
  );
};

export default Cars;