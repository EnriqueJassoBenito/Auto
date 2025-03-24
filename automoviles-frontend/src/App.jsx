import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './pages/Login';
import Dashboard from './pages/Dashboard';
import Brands from './pages/Brands';
import Cars from './pages/Cars';
import Customers from './pages/Customers';
import Employees from './pages/Employees';
import Services from './pages/Services';
import ProtectedRoute from './components/ProtectedRoute';

function App() {
  return (
    <Router>
      <Routes>
        {/* Ruta pública para el login */}
        <Route path="/login" element={<Login />} />

        {/* Rutas protegidas para el dashboard y sus secciones */}
        <Route element={<ProtectedRoute />}>
          <Route path="/dashboard" element={<Dashboard />}>
            <Route path="brands" element={<Brands />} />
            <Route path="cars" element={<Cars />} />
            <Route path="customers" element={<Customers />} />
            <Route path="employees" element={<Employees />} />
            <Route path="services" element={<Services />} />
          </Route>
        </Route>

        {/* Ruta por defecto (redirige a /login) */}
        <Route path="/" element={<Login />} />
      </Routes>
    </Router>
  );
}

export default App;