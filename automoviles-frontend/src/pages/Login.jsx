import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { Container, Form, Button, Alert } from 'react-bootstrap';
import Swal from 'sweetalert2';
import './Styles/Login.css'; // Importar el archivo CSS

const Login = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();

    try {
      // Hacer la solicitud a la API de autenticación
      const response = await axios.post('http://localhost:8080/api/auth/login', {
        username,
        password,
      });

      // Verificar si la respuesta tiene el formato esperado
      if (response.data && response.data.data && response.data.status === 'ok') {
        // Extraer el token de la respuesta
        const token = response.data.data;

        // Guardar el token en el localStorage
        localStorage.setItem('token', token);

        // Mostrar alerta de éxito con SweetAlert2
        Swal.fire({
          icon: 'success',
          title: 'Acceso correcto',
          text: 'Has iniciado sesión exitosamente',
          timer: 2000, // Cierra la alerta después de 2 segundos
          showConfirmButton: false,
        }).then(() => {
          // Redirigir al usuario a la página principal
          navigate('/dashboard'); // Cambia '/dashboard' por la ruta que desees
        });
      } else {
        // Manejar el caso en que la respuesta no tiene el formato esperado
        setError('Respuesta del servidor inesperada');
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'Respuesta del servidor inesperada',
        });
      }
    } catch (err) {
      // Manejar errores de autenticación
      setError('Credenciales incorrectas');
      console.error('Error al iniciar sesión:', err);

      // Mostrar alerta de error con SweetAlert2
      Swal.fire({
        icon: 'error',
        title: 'Error',
        text: 'Credenciales incorrectas',
      });
    }
  };

  return (
    <Container fluid className="login-container">
      <div className="login-box">
        <h1 className="login-title">Automoviles</h1>
        {error && <Alert variant="danger">{error}</Alert>}
        <Form onSubmit={handleLogin}>
          <Form.Group className="mb-3" controlId="username">
            <Form.Label>Usuario:</Form.Label>
            <Form.Control
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
            />
          </Form.Group>
          <Form.Group className="mb-3" controlId="password">
            <Form.Label>Contraseña:</Form.Label>
            <Form.Control
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </Form.Group>
          <Button variant="dark" type="submit" className="login-button">
            Iniciar Sesión
          </Button>
        </Form>
      </div>
    </Container>
  );
};

export default Login;