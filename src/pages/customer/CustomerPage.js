// src/pages/customer/CustomerPage.js

import React, { useState, useEffect } from 'react';
import './CustomerPage.css';

function CustomerPage() {
  const [customers, setCustomers] = useState([]);
  const [form, setForm] = useState({
    name: '',
    phoneno: '',
    email: '',
    gender: '',
    age: ''
  });

  // Load customer list on component mount
  useEffect(() => {
    fetch('/list/customer', { credentials: 'include' })
      .then(res => res.json())
      .then(data => setCustomers(data))
      .catch(err => console.error("Failed to load customers", err));
  }, []);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const formBody = new URLSearchParams(form).toString();

    fetch('/save/customer', {
      method: 'POST',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      body: formBody
    })
      .then(() => fetch('/list/customer', { credentials: 'include' }))
      .then(res => res.json())
      .then(data => {
        setCustomers(data);
        setForm({ name: '', phoneno: '', email: '', gender: '', age: '' });
      })
      .catch(err => console.error("Failed to save customer", err));
  };

  const handleDelete = (id) => {
    fetch(`/delete/customer?id=${id}`, { credentials: 'include' })
      .then(() => setCustomers(customers.filter(c => c.id !== id)))
      .catch(err => console.error("Failed to delete customer", err));
  };

  return (
    <div className="customer-page">
      <h1>Customer Management</h1>

      <form onSubmit={handleSubmit} className="customer-form">
        <input type="text" name="name" placeholder="Name" value={form.name} onChange={handleChange} required />
        <input type="number" name="phoneno" placeholder="Phone No" value={form.phoneno} onChange={handleChange} required />
        <input type="email" name="email" placeholder="Email" value={form.email} onChange={handleChange} required />
        <input type="text" name="gender" placeholder="Gender" value={form.gender} onChange={handleChange} required />
        <input type="number" name="age" placeholder="Age" value={form.age} onChange={handleChange} required />
        <button type="submit">Save Customer</button>
      </form>

      <table className="customer-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Phone No</th>
            <th>Email</th>
            <th>Gender</th>
            <th>Age</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {customers.map(customer => (
            <tr key={customer.id}>
              <td>{customer.id}</td>
              <td>{customer.name}</td>
              <td>{customer.phoneno}</td>
              <td>{customer.email}</td>
              <td>{customer.gender}</td>
              <td>{customer.age}</td>
              <td>
                <button className="delete-btn" onClick={() => handleDelete(customer.id)}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default CustomerPage;
