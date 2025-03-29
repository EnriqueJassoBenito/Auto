import React from 'react';
import { Container } from 'react-bootstrap';
import { Outlet } from 'react-router-dom'; // Importar Outlet para renderizar componentes hijos
import CustomNavbar from '../components/CustomNavbar'; // Importar el Navbar
import './Styles/Dashboard.css'; // Archivo CSS para animaciones y estilos

const Dashboard = () => {
  return (
    <div>
      <CustomNavbar /> {/* Incluir el Navbar */}
      <Container className="dashboard-container">
        
        {/* Outlet para renderizar los componentes hijos */}
        <Outlet />
      </Container>
    </div>
  );
};

export default Dashboard;