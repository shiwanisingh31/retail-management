// src/pages/product/Product.js

import React, { useState, useEffect } from 'react';
import './ProductPage.css';

function ProductPage() {
  const [products, setProducts] = useState([]);
  const [form, setForm] = useState({
    name: '',
    category: '',
    stock: '',
    price: ''
  });

  useEffect(() => {
    fetch('/list/products')
      .then(async res => {
        if (!res.ok) {
          const text = await res.text();
          throw new Error(text || `HTTP ${res.status}`);
        }
        return res.json();
      })
      .then(data => setProducts(data))
      .catch(err => console.error("Failed to load products", err));
  }, []);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    fetch('/save/products', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    ...form,
    stock: Number(form.stock),
    price: Number(form.price)
  })
})
      .then(() => fetch('/list/products'))
      .then(async res => {
        if (!res.ok) {
          const text = await res.text();
          throw new Error(text || `HTTP ${res.status}`);
        }
        return res.json();
      })
      .then(data => {
        setProducts(data);
        setForm({ name: '', category: '', stock: '', price: '' });
      })
      .catch(err => console.error("Failed to save product", err));
  };

  const handleDelete = (id) => {
    fetch(`/delete/products?id=${id}`)
      .then(() => setProducts(products.filter(product => product.id !== id)))
      .catch(err => console.error("Failed to delete product", err));
  };

  return (
    <div className="product-page">
      <h1>Product Management</h1>

      <form onSubmit={handleSubmit} className="product-form">
        <input type="text" name="name" placeholder="Name" value={form.name} onChange={handleChange} required />
        <input type="text" name="category" placeholder="Category" value={form.category} onChange={handleChange} required />
        <input type="number" name="stock" placeholder="Stock" value={form.stock} onChange={handleChange} required />
        <input type="number" name="price" placeholder="Price" value={form.price} onChange={handleChange} required />
        <button type="submit">Save Product</button>
      </form>

      <table className="product-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Category</th>
            <th>Stock</th>
            <th>Price</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {products.map(prod => (
            <tr key={prod.id}>
              <td>{prod.id}</td>
              <td>{prod.name}</td>
              <td>{prod.category}</td>
              <td>{prod.stock}</td>
              <td>{prod.price}</td>
              <td>
                <button className="delete-btn" onClick={() => handleDelete(prod.id)}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default ProductPage;
