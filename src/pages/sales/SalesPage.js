// src/pages/sales/SalesPage.js

import React, { useEffect, useState } from 'react';
import {
  LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, BarChart, Bar, ResponsiveContainer
} from 'recharts';
import './SalesPage.css';

function SalesPage() {
  const [daily, setDaily] = useState([]);
  const [weekly, setWeekly] = useState([]);
  const [monthly, setMonthly] = useState([]);
  const [yearly, setYearly] = useState([]);

  useEffect(() => {
    fetch('/sales/daily').then(res => res.json()).then(setDaily);
    fetch('/sales/weekly').then(res => res.json()).then(setWeekly);
    fetch('/sales/monthly').then(res => res.json()).then(setMonthly);
    fetch('/sales/yearly').then(res => res.json()).then(setYearly);
  }, []);

  const formatSalesData = (sales) =>
    sales.map(s => ({
      date: new Date(s.saleDate).toLocaleDateString(),
      total: s.totalPrice,
    }));

  const renderTable = (title, sales) => (
    <div className="sales-table-block">
      <h3>{title}</h3>
      <table className="sales-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Product ID</th>
            <th>Quantity</th>
            <th>Total Price</th>
            <th>Sale Date</th>
          </tr>
        </thead>
        <tbody>
          {sales.map(s => (
            <tr key={s.id}>
              <td>{s.id}</td>
              <td>{s.productId}</td>
              <td>{s.quantity}</td>
              <td>{s.totalPrice}</td>
              <td>{new Date(s.saleDate).toLocaleString()}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );

  return (
    <div className="sales-container">
      <h1>Sales Report Dashboard</h1>

      <div className="charts-row">
        <div className="chart-card">
          <h3>Daily Sales (Line Chart)</h3>
          <ResponsiveContainer width="100%" height={300}>
            <LineChart data={formatSalesData(daily)}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="date" />
              <YAxis />
              <Tooltip />
              <Legend />
              <Line type="monotone" dataKey="total" stroke="#007bff" />
            </LineChart>
          </ResponsiveContainer>
        </div>

        <div className="chart-card">
          <h3>Weekly Sales (Bar Chart)</h3>
          <ResponsiveContainer width="100%" height={300}>
            <BarChart data={formatSalesData(weekly)}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="date" />
              <YAxis />
              <Tooltip />
              <Legend />
              <Bar dataKey="total" fill="#28a745" />
            </BarChart>
          </ResponsiveContainer>
        </div>
      </div>

      <div className="charts-row">
        <div className="chart-card">
          <h3>Monthly Sales (Line Chart)</h3>
          <ResponsiveContainer width="100%" height={300}>
            <LineChart data={formatSalesData(monthly)}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="date" />
              <YAxis />
              <Tooltip />
              <Legend />
              <Line type="monotone" dataKey="total" stroke="#ffc107" />
            </LineChart>
          </ResponsiveContainer>
        </div>

        <div className="chart-card">
          <h3>Yearly Sales (Bar Chart)</h3>
          <ResponsiveContainer width="100%" height={300}>
            <BarChart data={formatSalesData(yearly)}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="date" />
              <YAxis />
              <Tooltip />
              <Legend />
              <Bar dataKey="total" fill="#dc3545" />
            </BarChart>
          </ResponsiveContainer>
        </div>
      </div>

      <div className="sales-tables">
        {renderTable('Daily Sales Table', daily)}
        {renderTable('Weekly Sales Table', weekly)}
        {renderTable('Monthly Sales Table', monthly)}
        {renderTable('Yearly Sales Table', yearly)}
      </div>
    </div>
  );
}

export default SalesPage;
