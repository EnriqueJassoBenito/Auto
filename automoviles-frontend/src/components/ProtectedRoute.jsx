import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';

const ProtectedRoute = ({ allowedRoles }) => {
  const isAuthenticated = localStorage.getItem('token');
  const userRole = localStorage.getItem('role'); // Supongamos que el rol se guarda en localStorage

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  if (allowedRoles && !allowedRoles.includes(userRole)) {
    return <Navigate to="/unauthorized" replace />; // Redirigir a una página de "No autorizado"
  }

  return <Outlet />;
};

export default ProtectedRoute;