import React from 'react';
import { Navbar, Nav, Container } from 'react-bootstrap';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
  faHome,
  faTags,
  faCar,
  faUsers,
  faUserTie,
  faTools,
  faSignOutAlt,
} from '@fortawesome/free-solid-svg-icons';
import Swal from 'sweetalert2';
import './Styles/Navbar.css';

const CustomNavbar = () => {
  const navigate = useNavigate();
  const location = useLocation();

  // Función para verificar si la ruta coincide
  const isActive = (path) => {
    return location.pathname === path;
  };

  const handleLogout = () => {
    Swal.fire({
      title: '¿Estás seguro de cerrar sesión?',
      icon: 'question',
      showCancelButton: true,
      confirmButtonText: 'Sí, cerrar sesión',
      cancelButtonText: 'Cancelar',
    }).then((result) => {
      if (result.isConfirmed) {
        localStorage.removeItem('token');
        Swal.fire({
          icon: 'success',
          title: 'Sesión cerrada',
          text: 'Has cerrado sesión exitosamente',
          timer: 2000,
          showConfirmButton: false,
        }).then(() => {
          navigate('/login');
        });
      }
    });
  };

  return (
    <Navbar bg="dark" variant="dark" expand="lg" className="custom-navbar">
      <Container>
        <Navbar.Brand as={Link} to="/dashboard">Agency</Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="ms-auto">
            <Nav.Link 
              as={Link} 
              to="/dashboard/brands" 
              className={`nav-link ${isActive('/dashboard/brands') ? 'active' : ''}`}
            >
              <FontAwesomeIcon icon={faTags} />
              <span className="ms-2">Marcas</span>
            </Nav.Link>
            <Nav.Link 
              as={Link} 
              to="/dashboard/cars" 
              className={`nav-link ${isActive('/dashboard/cars') ? 'active' : ''}`}
            >
              <FontAwesomeIcon icon={faCar} />
              <span className="ms-2">Autos</span>
            </Nav.Link>
            <Nav.Link 
              as={Link} 
              to="/dashboard/customers" 
              className={`nav-link ${isActive('/dashboard/customers') ? 'active' : ''}`}
            >
              <FontAwesomeIcon icon={faUsers} />
              <span className="ms-2">Clientes</span>
            </Nav.Link>
            <Nav.Link 
              as={Link} 
              to="/dashboard/employees" 
              className={`nav-link ${isActive('/dashboard/employees') ? 'active' : ''}`}
            >
              <FontAwesomeIcon icon={faUserTie} />
              <span className="ms-2">Empleados</span>
            </Nav.Link>
            <Nav.Link 
              as={Link} 
              to="/dashboard/services" 
              className={`nav-link ${isActive('/dashboard/services') ? 'active' : ''}`}
            >
              <FontAwesomeIcon icon={faTools} />
              <span className="ms-2">Servicios</span>
            </Nav.Link>
            <Nav.Link onClick={handleLogout} className="nav-link" style={{ cursor: 'pointer' }}>
              <FontAwesomeIcon icon={faSignOutAlt} />
              <span className="ms-2">Cerrar Sesión</span>
            </Nav.Link>
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export default CustomNavbar;