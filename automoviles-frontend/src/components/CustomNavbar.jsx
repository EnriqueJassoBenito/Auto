import React from 'react';
import { Navbar, Nav, Container } from 'react-bootstrap';
import { Link, useNavigate } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
  faHome,           // Icono de Inicio
  faTags,           // Icono de Marcas
  faCar,            // Icono de Autos
  faUsers,          // Icono de Clientes
  faUserTie,        // Icono de Empleados
  faTools,          // Icono de Servicios
  faSignOutAlt,     // Icono de Cerrar Sesión
} from '@fortawesome/free-solid-svg-icons';
import Swal from 'sweetalert2';
import './Styles/Navbar.css'; // Archivo CSS para animaciones y estilos

const CustomNavbar = () => {
  const navigate = useNavigate();

  const handleLogout = () => {
    // Mostrar alerta de confirmación
    Swal.fire({
      title: '¿Estás seguro de cerrar sesión?',
      icon: 'question',
      showCancelButton: true,
      confirmButtonText: 'Sí, cerrar sesión',
      cancelButtonText: 'Cancelar',
    }).then((result) => {
      if (result.isConfirmed) {
        // Eliminar el token del localStorage
        localStorage.removeItem('token');

        // Mostrar alerta de éxito
        Swal.fire({
          icon: 'success',
          title: 'Sesión cerrada',
          text: 'Has cerrado sesión exitosamente',
          timer: 2000, // Cierra la alerta después de 2 segundos
          showConfirmButton: false,
        }).then(() => {
          // Redirigir al usuario a la página de inicio de sesión
          navigate('/login');
        });
      }
    });
  };

  return (
    <Navbar bg="dark" variant="dark" expand="lg" className="custom-navbar">
      <Container>
        <Navbar.Brand as={Link} to="/dashboard">Mi App</Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="ms-auto">
            <Nav.Link as={Link} to="/dashboard" className="nav-link">
              <FontAwesomeIcon icon={faHome} /> {/* Icono de Inicio */}
              <span className="ms-2">Inicio</span>
            </Nav.Link>
            <Nav.Link as={Link} to="/dashboard/brands" className="nav-link">
              <FontAwesomeIcon icon={faTags} /> {/* Icono de Marcas */}
              <span className="ms-2">Marcas</span>
            </Nav.Link>
            <Nav.Link as={Link} to="/dashboard/cars" className="nav-link">
              <FontAwesomeIcon icon={faCar} /> {/* Icono de Autos */}
              <span className="ms-2">Autos</span>
            </Nav.Link>
            <Nav.Link as={Link} to="/dashboard/customers" className="nav-link">
              <FontAwesomeIcon icon={faUsers} /> {/* Icono de Clientes */}
              <span className="ms-2">Clientes</span>
            </Nav.Link>
            <Nav.Link as={Link} to="/dashboard/employees" className="nav-link">
              <FontAwesomeIcon icon={faUserTie} /> {/* Icono de Empleados */}
              <span className="ms-2">Empleados</span>
            </Nav.Link>
            <Nav.Link as={Link} to="/dashboard/services" className="nav-link">
              <FontAwesomeIcon icon={faTools} /> {/* Icono de Servicios */}
              <span className="ms-2">Servicios</span>
            </Nav.Link>
        
            <Nav.Link onClick={handleLogout} className="nav-link" style={{ cursor: 'pointer' }}>
              <FontAwesomeIcon icon={faSignOutAlt} /> {/* Icono de Cerrar Sesión */}
              <span className="ms-2">Cerrar Sesión</span>
            </Nav.Link>
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export default CustomNavbar;