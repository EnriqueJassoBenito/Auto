import React, { useState } from 'react';
import {
  useReactTable,
  getCoreRowModel,
  getFilteredRowModel,
  getPaginationRowModel,
  flexRender,
} from '@tanstack/react-table';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { 
  faEdit, 
  faTrash, 
  faSearch, 
  faAngleLeft, 
  faAngleRight,
  faAnglesLeft,
  faAnglesRight
} from '@fortawesome/free-solid-svg-icons';
import { Button, Form, InputGroup } from 'react-bootstrap';
import './Styles/CustomerTable.css';

const CustomerTable = ({ customers, onEdit, onDelete }) => {
  const [globalFilter, setGlobalFilter] = useState('');

  // Definición de columnas
  const columns = [
    {
      header: 'Nombre',
      accessorKey: 'name',
    },
    {
      header: 'Apellido',
      accessorKey: 'surname',
    },
    {
      header: 'Teléfono',
      accessorKey: 'phone',
    },
    {
      header: 'Correo Electrónico',
      accessorKey: 'email',
    },
    {
      header: 'Acciones',
      cell: ({ row }) => (
        <div className="actions-cell">
          <Button
            variant="dark"
            onClick={() => onEdit(row.original)}
            className="action-button"
            size="sm"
          >
            <FontAwesomeIcon icon={faEdit} />
          </Button>{' '}
          <Button
            variant="danger"
            onClick={() => onDelete(row.original.id)}
            className="action-button"
            size="sm"
          >
            <FontAwesomeIcon icon={faTrash} />
          </Button>
        </div>
      ),
    },
  ];

  // Configuración de la tabla
  const table = useReactTable({
    data: customers,
    columns,
    state: {
      globalFilter,
    },
    onGlobalFilterChange: setGlobalFilter,
    getCoreRowModel: getCoreRowModel(),
    getFilteredRowModel: getFilteredRowModel(),
    getPaginationRowModel: getPaginationRowModel(),
    initialState: {
      pagination: {
        pageSize: 10,
      },
    },
  });

  return (
    <div className="table-responsive-container">
      {/* Barra de búsqueda */}
      <div className="search-container mb-3">
        <InputGroup>
          <InputGroup.Text>
            <FontAwesomeIcon icon={faSearch} />
          </InputGroup.Text>
          <Form.Control
            type="text"
            placeholder="Buscar en todos los campos..."
            value={globalFilter ?? ''}
            onChange={(e) => setGlobalFilter(e.target.value)}
            className="search-input"
          />
        </InputGroup>
      </div>

      {/* Tabla */}
      <div className="table-container">
        <table className="customer-table">
          <thead>
            {table.getHeaderGroups().map(headerGroup => (
              <tr key={headerGroup.id}>
                {headerGroup.headers.map(header => (
                  <th key={header.id}>
                    {flexRender(
                      header.column.columnDef.header,
                      header.getContext()
                    )}
                  </th>
                ))}
              </tr>
            ))}
          </thead>
          <tbody>
            {table.getRowModel().rows.map(row => (
              <tr key={row.id} className="customer-row">
                {row.getVisibleCells().map(cell => (
                  <td key={cell.id}>
                    {flexRender(cell.column.columnDef.cell, cell.getContext())}
                  </td>
                ))}
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* Controles de paginación */}
      <div className="pagination-controls">
        <div className="pagination-buttons">
          <Button
            variant="outline-dark"
            onClick={() => table.setPageIndex(0)}
            disabled={!table.getCanPreviousPage()}
            title="Primera página"
          >
            <FontAwesomeIcon icon={faAnglesLeft} />
          </Button>
          <Button
            variant="outline-dark"
            onClick={() => table.previousPage()}
            disabled={!table.getCanPreviousPage()}
            title="Página anterior"
          >
            <FontAwesomeIcon icon={faAngleLeft} />
          </Button>
        </div>

        <div className="page-info">
          <span>
            Página{' '}
            <strong>
              {table.getState().pagination.pageIndex + 1} de{' '}
              {table.getPageCount()}
            </strong>
          </span>
          <span className="mx-2">|</span>
          <span>
            Registros: <strong>{customers.length}</strong>
          </span>
        </div>

        <div className="pagination-buttons">
          <Button
            variant="outline-dark"
            onClick={() => table.nextPage()}
            disabled={!table.getCanNextPage()}
            title="Página siguiente"
          >
            <FontAwesomeIcon icon={faAngleRight} />
          </Button>
          <Button
            variant="outline-dark"
            onClick={() => table.setPageIndex(table.getPageCount() - 1)}
            disabled={!table.getCanNextPage()}
            title="Última página"
          >
            <FontAwesomeIcon icon={faAnglesRight} />
          </Button>
        </div>

        <div className="page-size-selector">
          <Form.Select
            value={table.getState().pagination.pageSize}
            onChange={e => {
              table.setPageSize(Number(e.target.value));
            }}
            size="sm"
          >
            {[5, 10, 20, 30, 40, 50].map(pageSize => (
              <option key={pageSize} value={pageSize}>
                Mostrar {pageSize}
              </option>
            ))}
          </Form.Select>
        </div>
      </div>
    </div>
  );
};

export default CustomerTable;