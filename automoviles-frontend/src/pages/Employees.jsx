import React, { useState, useEffect } from 'react';
import { Button } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUserPlus, faUserTie } from '@fortawesome/free-solid-svg-icons';
import axios from 'axios';
import EmployeeForm from '../components/EmployeeForm';
import EmployeeTable from '../components/EmployeeTable';
import Alert from '../components/Alert';

const Employees = () => {
  const [employees, setEmployees] = useState([]);
  const [roles, setRoles] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [selectedEmployee, setSelectedEmployee] = useState(null);
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

  // Obtener empleados
  const fetchEmployees = async () => {
    try {
      const response = await axiosWithToken.get('/employees');
      setEmployees(response.data);
    } catch (error) {
      Alert('Error', 'No se pudo obtener la lista de empleados', 'error');
    }
  };

  // Obtener roles
  const fetchRoles = async () => {
    try {
      const response = await axiosWithToken.get('/roles');
      setRoles(response.data);
    } catch (error) {
      Alert('Error', 'No se pudo obtener la lista de roles', 'error');
    }
  };

  useEffect(() => {
    fetchEmployees();
    fetchRoles();
  }, []);

  // Manejar submit del formulario
  const handleSubmit = async (data) => {
    try {
      if (selectedEmployee) {
        await axiosWithToken.put(`/employees/${selectedEmployee.id}`, data);
        Alert('Éxito', 'Empleado actualizado correctamente', 'success');
      } else {
        await axiosWithToken.post('/employees', data);
        Alert('Éxito', 'Empleado agregado correctamente', 'success');
      }

      fetchEmployees();
      setSelectedEmployee(null);
      setFormKey(prevKey => prevKey + 1);
      setShowForm(false);
    } catch (error) {
      Alert('Error', 'No se pudo guardar el empleado', 'error');
    }
  };

  // Eliminar empleado
  const handleDelete = async (id) => {
    try {
      const result = await Alert(
        'Confirmar Eliminación',
        '¿Estás seguro de eliminar este empleado?',
        'warning',
        true
      );

      if (result.isConfirmed) {
        await axiosWithToken.delete(`/employees/${id}`);
        Alert('Éxito', 'Empleado eliminado correctamente', 'success');
        fetchEmployees();
      }
    } catch (error) {
      Alert('Error', 'No se pudo eliminar el empleado', 'error');
    }
  };

  // Obtener nombre del rol por ID
  const getRoleNameById = (roleId) => {
    const role = roles.find(r => r.id === roleId);
    return role ? role.name : 'Desconocido';
  };

  return (
    <div>
      <h1>Gestión de Empleados</h1>
      <Button
        variant="dark"
        onClick={() => {
          setSelectedEmployee(null);
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
        Añadir nuevo empleado <FontAwesomeIcon icon={faUserTie} />
      </Button>
      <EmployeeTable
        employees={employees}
        roles={roles}
        getRoleNameById={getRoleNameById}
        onEdit={(employee) => {
          setSelectedEmployee(employee);
          setShowForm(true);
        }}
        onDelete={handleDelete}
      />
      <EmployeeForm
        key={formKey}
        show={showForm}
        onHide={() => {
          setShowForm(false);
          setSelectedEmployee(null);
        }}
        onSubmit={handleSubmit}
        initialData={selectedEmployee}
        roles={roles}
      />
    </div>
  );
};

export default Employees;