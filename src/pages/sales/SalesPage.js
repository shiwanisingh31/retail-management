// src/pages/sales/SalesPage.js

import React, { useState, useEffect } from 'react';
import {
  LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, BarChart, Bar, ResponsiveContainer, PieChart, Pie, Cell
} from 'recharts';
import './SalesPage.css';

function SalesPage() {
  const [sales, setSales] = useState([]);
  const [products, setProducts] = useState([]);
  const [customers, setCustomers] = useState([]);
  const [statistics, setStatistics] = useState({});
  const [topProducts, setTopProducts] = useState([]);
  const [topCustomers, setTopCustomers] = useState([]);
  const [dailySummary, setDailySummary] = useState([]);
  const [monthlySummary, setMonthlySummary] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  // Form state for creating new sales
  const [saleForm, setSaleForm] = useState({
    productId: '',
    customerId: '',
    quantity: '',
    paymentMethod: 'CASH',
    notes: ''
  });

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      setLoading(true);
      
      // Fetch all data in parallel
      const [
        salesRes, productsRes, customersRes, statsRes, 
        topProductsRes, topCustomersRes, dailyRes, monthlyRes
      ] = await Promise.all([
        fetch('/list/sales', { credentials: 'include' }),
        fetch('/list/products', { credentials: 'include' }),
        fetch('/list/customer', { credentials: 'include' }),
        fetch('/sales/statistics', { credentials: 'include' }),
        fetch('/sales/top-products', { credentials: 'include' }),
        fetch('/sales/top-customers', { credentials: 'include' }),
        fetch('/sales/daily-summary', { credentials: 'include' }),
        fetch('/sales/monthly-summary', { credentials: 'include' })
      ]);

      if (salesRes.ok) setSales(await salesRes.json());
      if (productsRes.ok) setProducts(await productsRes.json());
      if (customersRes.ok) setCustomers(await customersRes.json());
      if (statsRes.ok) setStatistics(await statsRes.json());
      if (topProductsRes.ok) setTopProducts(await topProductsRes.json());
      if (topCustomersRes.ok) setTopCustomers(await topCustomersRes.json());
      if (dailyRes.ok) setDailySummary(await dailyRes.json());
      if (monthlyRes.ok) setMonthlySummary(await monthlyRes.json());

    } catch (err) {
      setError('Failed to load sales data: ' + err.message);
    } finally {
      setLoading(false);
    }
  };

  const handleCreateSale = async (e) => {
    e.preventDefault();
    
    try {
      const params = new URLSearchParams({
        productId: saleForm.productId,
        customerId: saleForm.customerId,
        quantity: saleForm.quantity,
        paymentMethod: saleForm.paymentMethod,
        notes: saleForm.notes
      });

      const response = await fetch('/save/sale', {
        method: 'POST',
        credentials: 'include',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: params
      });

      if (response.ok) {
        setSuccess('Sale created successfully!');
        setSaleForm({ productId: '', customerId: '', quantity: '', paymentMethod: 'CASH', notes: '' });
        fetchData(); // Refresh data
      } else {
        const errorText = await response.text();
        setError('Failed to create sale: ' + errorText);
      }
    } catch (err) {
      setError('Error creating sale: ' + err.message);
    }
  };

  const handleDeleteSale = async (saleId) => {
    if (!window.confirm('Are you sure you want to delete this sale?')) return;

    try {
      const response = await fetch(`/delete/sale/${saleId}`, {
        method: 'DELETE',
        credentials: 'include'
      });

      if (response.ok) {
        setSuccess('Sale deleted successfully!');
        fetchData(); // Refresh data
      } else {
        const errorText = await response.text();
        setError('Failed to delete sale: ' + errorText);
      }
    } catch (err) {
      setError('Error deleting sale: ' + err.message);
    }
  };

  const formatSalesData = (sales) =>
    sales.map(s => ({
      date: new Date(s.saleDate).toLocaleDateString(),
      total: s.totalAmount,
      quantity: s.quantity,
      product: s.product?.name || 'Unknown',
      customer: s.customer?.name || 'Unknown'
    }));

  const formatSummaryData = (summary) =>
    summary.map(item => ({
      date: item[0] ? new Date(item[0]).toLocaleDateString() : 'Unknown',
      total: item[2] || 0,
      count: item[1] || 0
    }));

  const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042', '#8884D8'];

  if (loading) return <div className="sales-container">Loading sales data...</div>;

  return (
    <div className="sales-container">
      <h1>Sales Management & Analytics</h1>

      {error && <div className="error-message">{error}</div>}
      {success && <div className="success-message">{success}</div>}

      {/* Create Sale Form */}
      <div className="create-sale-section">
        <h2>Create New Sale</h2>
        <form onSubmit={handleCreateSale} className="sale-form">
          <div className="form-row">
            <select
              value={saleForm.productId}
              onChange={(e) => setSaleForm({...saleForm, productId: e.target.value})}
              required
            >
              <option value="">Select Product</option>
              {products.map(product => (
                <option key={product.id} value={product.id}>
                  {product.name} - ${product.price}
                </option>
              ))}
            </select>

            <select
              value={saleForm.customerId}
              onChange={(e) => setSaleForm({...saleForm, customerId: e.target.value})}
              required
            >
              <option value="">Select Customer</option>
              {customers.map(customer => (
                <option key={customer.id} value={customer.id}>
                  {customer.name} - {customer.phoneno}
                </option>
              ))}
            </select>
          </div>

          <div className="form-row">
            <input
              type="number"
              placeholder="Quantity"
              value={saleForm.quantity}
              onChange={(e) => setSaleForm({...saleForm, quantity: e.target.value})}
              min="1"
              required
            />

            <select
              value={saleForm.paymentMethod}
              onChange={(e) => setSaleForm({...saleForm, paymentMethod: e.target.value})}
            >
              <option value="CASH">Cash</option>
              <option value="CARD">Card</option>
              <option value="UPI">UPI</option>
              <option value="BANK_TRANSFER">Bank Transfer</option>
            </select>
          </div>

          <div className="form-row">
            <input
              type="text"
              placeholder="Notes (optional)"
              value={saleForm.notes}
              onChange={(e) => setSaleForm({...saleForm, notes: e.target.value})}
            />
            <button type="submit">Create Sale</button>
          </div>
        </form>
      </div>

      {/* Statistics Cards */}
      <div className="stats-cards">
        <div className="stat-card">
          <h3>Total Sales</h3>
          <p>{statistics.totalSales || 0}</p>
        </div>
        <div className="stat-card">
          <h3>Total Amount</h3>
          <p>${statistics.totalAmount || 0}</p>
        </div>
        <div className="stat-card">
          <h3>Average Sale</h3>
          <p>${statistics.averageAmount || 0}</p>
        </div>
        <div className="stat-card">
          <h3>Max Sale</h3>
          <p>${statistics.maxAmount || 0}</p>
        </div>
      </div>

      {/* Charts */}
      <div className="charts-section">
        <div className="charts-row">
          <div className="chart-card">
            <h3>Daily Sales Summary</h3>
            <ResponsiveContainer width="100%" height={300}>
              <BarChart data={formatSummaryData(dailySummary)}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="date" />
                <YAxis />
                <Tooltip />
                <Legend />
                <Bar dataKey="total" fill="#007bff" />
              </BarChart>
            </ResponsiveContainer>
          </div>

          <div className="chart-card">
            <h3>Monthly Sales Summary</h3>
            <ResponsiveContainer width="100%" height={300}>
              <LineChart data={formatSummaryData(monthlySummary)}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="date" />
                <YAxis />
                <Tooltip />
                <Legend />
                <Line type="monotone" dataKey="total" stroke="#28a745" />
              </LineChart>
            </ResponsiveContainer>
          </div>
        </div>

        <div className="charts-row">
          <div className="chart-card">
            <h3>Top Selling Products</h3>
            <ResponsiveContainer width="100%" height={300}>
              <PieChart>
                <Pie
                  data={topProducts.map((item, index) => ({
                    name: item[1] || 'Unknown',
                    value: item[2] || 0
                  }))}
                  cx="50%"
                  cy="50%"
                  outerRadius={80}
                  fill="#8884d8"
                  dataKey="value"
                  label={({name, value}) => `${name}: ${value}`}
                >
                  {topProducts.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                  ))}
                </Pie>
                <Tooltip />
              </PieChart>
            </ResponsiveContainer>
          </div>

          <div className="chart-card">
            <h3>Top Customers</h3>
            <ResponsiveContainer width="100%" height={300}>
              <BarChart data={topCustomers.map(item => ({
                name: item[1] || 'Unknown',
                total: item[2] || 0
              }))}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="name" />
                <YAxis />
                <Tooltip />
                <Legend />
                <Bar dataKey="total" fill="#ffc107" />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </div>
      </div>

      {/* Sales Table */}
      <div className="sales-table-section">
        <h2>All Sales</h2>
        <table className="sales-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Product</th>
              <th>Customer</th>
              <th>Quantity</th>
              <th>Unit Price</th>
              <th>Total Amount</th>
              <th>Payment Method</th>
              <th>Date</th>
              <th>Status</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {sales.map(sale => (
              <tr key={sale.id}>
                <td>{sale.id}</td>
                <td>{sale.product?.name || 'Unknown'}</td>
                <td>{sale.customer?.name || 'Unknown'}</td>
                <td>{sale.quantity}</td>
                <td>${sale.unitPrice}</td>
                <td>${sale.totalAmount}</td>
                <td>{sale.paymentMethod}</td>
                <td>{new Date(sale.saleDate).toLocaleDateString()}</td>
                <td>{sale.status}</td>
                <td>
                  <button 
                    onClick={() => handleDeleteSale(sale.id)}
                    className="delete-btn"
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default SalesPage;
