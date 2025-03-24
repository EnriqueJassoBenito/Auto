import Swal from 'sweetalert2';

const Alert = (title, text, icon, showConfirmButton = false) => {
  if (showConfirmButton) {
    return Swal.fire({
      title,
      text,
      icon,
      showCancelButton: true,
      confirmButtonText: 'Eliminar',
      cancelButtonText: 'Cancelar',
    });
  } else {
    Swal.fire({
      title,
      text,
      icon,
      confirmButtonText: 'Aceptar',
    });
  }
};

export default Alert;