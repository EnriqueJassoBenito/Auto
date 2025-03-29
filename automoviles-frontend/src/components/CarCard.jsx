import React, { useState, useEffect } from 'react';
import { Card, Button, Badge } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faEdit, faTrash, faUserCheck, faHandshake, faInfoCircle } from '@fortawesome/free-solid-svg-icons';
import axios from 'axios';
import CarAssignmentModal from './CarAssignmentModal';
import CarDetailsModal from './CarDetailsModal';

const CarCard = ({ car, onEdit, onDelete, onRefresh, imageVersion }) => {
  const [showAssignmentModal, setShowAssignmentModal] = useState(false);
  const [showDetailsModal, setShowDetailsModal] = useState(false);
  const [actionType, setActionType] = useState('assign');
  const [customerDetails, setCustomerDetails] = useState(null);
  const [loadingDetails, setLoadingDetails] = useState(false);
  const [imageSrc, setImageSrc] = useState('');

  useEffect(() => {
    const loadImage = async () => {
      if (car.imageName) {
        try {
          const response = await axios.get(
            `http://localhost:8080/api/cars/${car.id}/image?t=${Date.now()}&v=${imageVersion}`,
            {
              responseType: 'blob',
              headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
            }
          );
          
          const reader = new FileReader();
          reader.onloadend = () => {
            setImageSrc(reader.result);
          };
          reader.readAsDataURL(response.data);
          
        } catch (error) {
          console.error('Error cargando imagen:', error);
          setImageSrc('');
        }
      } else {
        setImageSrc('');
      }
    };
    
    loadImage();
    
    return () => {
      if (imageSrc) {
        URL.revokeObjectURL(imageSrc);
      }
    };
  }, [car.id, car.imageName, imageVersion]);

  const getStatusVariant = () => {
    switch (car.status) {
      case 'DISPONIBLE': return 'success';
      case 'ASIGNADO': return 'warning';
      case 'VENDIDO': return 'danger';
      default: return 'secondary';
    }
  };

  const handleAssignClick = (e) => {
    e.stopPropagation();
    setActionType('assign');
    setShowAssignmentModal(true);
  };

  const handleSellClick = (e) => {
    e.stopPropagation();
    setActionType('sell');
    setShowAssignmentModal(true);
  };

  const handleShowDetails = async (e) => {
    if (e) e.stopPropagation();
    
    if (car.status !== 'VENDIDO') return;
    
    setLoadingDetails(true);
    try {
      const token = localStorage.getItem('token');
      const axiosInstance = axios.create({
        baseURL: 'http://localhost:8080/api',
        headers: { Authorization: `Bearer ${token}` }
      });

      const [customerResponse, carResponse] = await Promise.all([
        car.customerId ? axiosInstance.get(`/customers/${car.customerId}`) : Promise.resolve(null),
        axiosInstance.get(`/cars/${car.id}`)
      ]);

      setCustomerDetails({
        customer: customerResponse?.data || null,
        carDetails: carResponse.data
      });
      setShowDetailsModal(true);
    } catch (error) {
      console.error('Error al cargar detalles:', error);
    } finally {
      setLoadingDetails(false);
    }
  };

  const handleCardClick = (e) => {
    if (car.status === 'VENDIDO') {
      handleShowDetails(e);
    }
  };

  return (
    <>
      <Card 
        className="h-100 shadow-sm car-card"
        onClick={handleCardClick}
        style={{ cursor: car.status === 'VENDIDO' ? 'pointer' : 'default' }}
      >
        {imageSrc && (
          <Card.Img 
            variant="top" 
            src={imageSrc}
            alt={car.model}
            className="car-image"
            onError={(e) => {
              e.target.style.display = 'none';
            }}
          />
        )}
        
        <Card.Body className="d-flex flex-column">
          <div>
            <Card.Title className="car-title">{car.model}</Card.Title>
            <Card.Subtitle className="mb-2 text-muted">
              {car.brand?.name || 'Marca no especificada'}
            </Card.Subtitle>
          </div>
          
          <div className="mt-auto">
            <div className="mb-2">
              <Badge bg="secondary" className="me-2 color-badge">{car.color}</Badge>
              <Badge bg="info" className="price-badge">
                ${car.purchasePrice?.toLocaleString()}
              </Badge>
            </div>
            
            <div className="d-flex justify-content-between actions-container">
              <div>
                {car.status === 'DISPONIBLE' && (
                  <Button
                    variant="outline-primary"
                    size="sm"
                    onClick={handleAssignClick}
                    aria-label="Asignar"
                    className="me-2 assign-btn"
                  >
                    <FontAwesomeIcon icon={faUserCheck} />
                  </Button>
                )}
                
                {(car.status === 'DISPONIBLE' || car.status === 'ASIGNADO') && (
                  <Button
                    variant="outline-success"
                    size="sm"
                    onClick={handleSellClick}
                    aria-label="Vender"
                    className="me-2 sell-btn"
                  >
                    <FontAwesomeIcon icon={faHandshake} />
                  </Button>
                )}
                
                {car.status !== 'VENDIDO' && (
                  <Button
                    variant="outline-warning"
                    size="sm"
                    onClick={(e) => {
                      e.stopPropagation();
                      onEdit(car);
                    }}
                    aria-label="Editar"
                    className="me-2 edit-btn"
                  >
                    <FontAwesomeIcon icon={faEdit} />
                  </Button>
                )}
              </div>
              
              {car.status !== 'VENDIDO' ? (
                <Button
                  variant="outline-danger"
                  size="sm"
                  onClick={(e) => {
                    e.stopPropagation();
                    onDelete(car.id);
                  }}
                  aria-label="Eliminar"
                  className="delete-btn"
                >
                  <FontAwesomeIcon icon={faTrash} />
                </Button>
              ) : (
                <Button
                  variant="outline-info"
                  size="sm"
                  onClick={handleShowDetails}
                  aria-label="Ver detalles"
                  className="details-btn"
                >
                  <FontAwesomeIcon icon={faInfoCircle} />
                </Button>
              )}
            </div>
          </div>
        </Card.Body>
        
        <Card.Footer>
          <Badge pill bg={getStatusVariant()} className="status-badge">
            {car.status || 'SIN ESTADO'}
          </Badge>
        </Card.Footer>
      </Card>

      <CarAssignmentModal
        show={showAssignmentModal}
        onHide={() => setShowAssignmentModal(false)}
        car={car}
        refreshData={onRefresh}
        isSellingInitial={actionType === 'sell'}
      />

      {car.status === 'VENDIDO' && (
        <CarDetailsModal
          show={showDetailsModal}
          onHide={() => setShowDetailsModal(false)}
          car={car}
          customerDetails={customerDetails}
          loading={loadingDetails}
          statusVariant={getStatusVariant()}
        />
      )}

      <style jsx>{`
        .car-card {
          transition: transform 0.3s ease, box-shadow 0.3s ease;
          animation: fadeIn 0.5s ease-in;
        }
        .car-card:hover {
          transform: scale(1.02);
          box-shadow: 0 10px 20px rgba(0,0,0,0.1);
        }
        .car-image {
          height: 180px;
          object-fit: cover;
          transition: transform 0.3s ease;
        }
        .car-image:hover {
          transform: scale(1.05);
        }
        .car-title {
          transition: color 0.3s ease;
        }
        .car-title:hover {
          color: #0d6efd !important;
        }
        .color-badge, .price-badge, .status-badge {
          transition: all 0.3s ease;
        }
        .color-badge:hover, .price-badge:hover, .status-badge:hover {
          transform: scale(1.1);
          opacity: 0.9;
        }
        .assign-btn, .sell-btn, .edit-btn, 
        .delete-btn, .details-btn {
          transition: all 0.3s ease;
        }
        .assign-btn:hover, .sell-btn:hover, 
        .edit-btn:hover, .delete-btn:hover,
        .details-btn:hover {
          transform: scale(1.2);
        }
        .assign-btn:active, .sell-btn:active, 
        .edit-btn:active, .delete-btn:active,
        .details-btn:active {
          transform: scale(0.9);
        }
        @keyframes fadeIn {
          from { opacity: 0; }
          to { opacity: 1; }
        }
      `}</style>
    </>
  );
};

export default CarCard;