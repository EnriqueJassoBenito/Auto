import React, { useState, useEffect } from 'react';
import { Modal, Button, Form } from 'react-bootstrap';
import axios from 'axios';
import Alert from '../components/Alert';

const CarForm = ({ show, onHide, onSubmit, initialData, brands, refreshData }) => {
  const [formData, setFormData] = useState({
    model: '',
    brandId: '',
    color: '',
    purchasePrice: '',
  });

  const [selectedFile, setSelectedFile] = useState(null);
  const [previewImage, setPreviewImage] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const getToken = () => {
    const token = localStorage.getItem('token');
    if (!token) {
      Alert('Error', 'No se encontró el token de autenticación', 'error');
      return null;
    }
    return `Bearer ${token}`;
  };

  useEffect(() => {
    const fetchData = async () => {
      setIsLoading(true);
      try {
        const [brandsResponse, imageResponse] = await Promise.all([
          axios.get('http://localhost:8080/api/brands', {
            headers: { Authorization: getToken() }
          }),
          initialData?.imageName ? 
            axios.get(
              `http://localhost:8080/api/cars/${initialData.id}/image?t=${Date.now()}`,
              { 
                responseType: 'blob',
                headers: { Authorization: getToken() }
              }
            ) : Promise.resolve(null)
        ]);

        if (imageResponse?.data) {
          const imageUrl = URL.createObjectURL(imageResponse.data);
          setPreviewImage(imageUrl);
        }
      } catch (error) {
        if (error.response?.status === 404) {
          setPreviewImage('');
        } else {
          console.error('Error loading data:', error);
          Alert('Error', 'Error al cargar datos iniciales', 'error');
        }
      } finally {
        setIsLoading(false);
      }
    };

    if (show) fetchData();

    return () => {
      if (previewImage) {
        URL.revokeObjectURL(previewImage);
      }
    };
  }, [show, initialData]);

  useEffect(() => {
    if (initialData) {
      setFormData({
        model: initialData.model,
        brandId: initialData.brand?.id || '',
        color: initialData.color,
        purchasePrice: initialData.purchasePrice,
      });
    } else {
      setFormData({
        model: '',
        brandId: '',
        color: '',
        purchasePrice: '',
      });
    }
  }, [initialData]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      if (!file.type.startsWith('image/')) {
        Alert('Error', 'Solo se permiten archivos de imagen', 'error');
        return;
      }
      if (file.size > 5 * 1024 * 1024) {
        Alert('Error', 'El tamaño máximo permitido es 5MB', 'error');
        return;
      }
      
      setSelectedFile(file);
      const imageUrl = URL.createObjectURL(file);
      setPreviewImage(imageUrl);
    }
  };

  const handleRemoveImage = () => {
    setSelectedFile(null);
    setPreviewImage('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsSubmitting(true);
    
    try {
      const carData = { ...formData };
      if (selectedFile) {
        carData.imageFile = selectedFile;
      } else if (!previewImage && initialData?.imageName) {
        // Si se eliminó la imagen existente
        carData.imageName = null;
      }
      
      await onSubmit(carData);
      onHide();
      if (refreshData) refreshData();
    } catch (error) {
      console.error('Submission error:', error);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <Modal 
      show={show} 
      onHide={onHide} 
      size="lg" 
      onExited={() => {
        setSelectedFile(null);
        setPreviewImage('');
        setFormData({
          model: '',
          brandId: '',
          color: '',
          purchasePrice: '',
        });
      }}
    >
      <Modal.Header closeButton>
        <Modal.Title>{initialData ? 'Editar Auto' : 'Agregar Auto'}</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        {isLoading ? (
          <div className="text-center py-4">
            <div className="spinner-border text-primary" role="status">
              <span className="visually-hidden">Cargando...</span>
            </div>
          </div>
        ) : (
          <Form onSubmit={handleSubmit}>
            <Form.Group className="mb-3">
              <Form.Label>Modelo</Form.Label>
              <Form.Control
                type="text"
                name="model"
                value={formData.model}
                onChange={handleChange}
                required
                placeholder="Ej. Sentra, Civic, etc."
              />
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Marca</Form.Label>
              <Form.Select
                name="brandId"
                value={formData.brandId}
                onChange={handleChange}
                required
              >
                <option value="">Seleccione una marca</option>
                {brands.map((brand) => (
                  <option key={brand.id} value={brand.id}>
                    {brand.name}
                  </option>
                ))}
              </Form.Select>
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Color</Form.Label>
              <Form.Control
                type="text"
                name="color"
                value={formData.color}
                onChange={handleChange}
                required
                placeholder="Ej. Rojo, Azul, etc."
              />
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Precio de Compra</Form.Label>
              <Form.Control
                type="number"
                name="purchasePrice"
                value={formData.purchasePrice}
                onChange={handleChange}
                required
                min="0.01"
                step="0.01"
                placeholder="Ej. 150000.00"
              />
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Imagen del Auto</Form.Label>
              <Form.Control
                type="file"
                accept="image/*"
                onChange={handleFileChange}
                disabled={isSubmitting}
              />
              <Form.Text className="text-muted">
                Formatos aceptados: JPG, PNG, JPEG (Max 5MB)
              </Form.Text>
              
              {previewImage && (
                <div className="mt-3 d-flex align-items-center">
                  <img 
                    src={previewImage} 
                    alt="Vista previa" 
                    style={{ 
                      objectFit: 'cover',
                      border: '1px solid #ddd',
                      borderRadius: '4px',
                      padding: '5px',
                      maxWidth: '200px',
                      maxHeight: '200px'
                    }}
                  />
                  <Button 
                    variant="danger" 
                    size="sm" 
                    onClick={handleRemoveImage}
                    className="ms-2"
                    disabled={isSubmitting}
                  >
                    Eliminar imagen
                  </Button>
                </div>
              )}
            </Form.Group>

            <Button 
              type="submit" 
              variant="dark" 
              className="w-100"
              disabled={isSubmitting || !formData.model || !formData.brandId || !formData.color || !formData.purchasePrice}
            >
              {isSubmitting ? 'Guardando...' : (initialData ? 'Guardar Cambios' : 'Agregar Auto')}
            </Button>
          </Form>
        )}
      </Modal.Body>
    </Modal>
  );
};

export default CarForm;