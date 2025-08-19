import React, { useState, useEffect } from 'react';
import './StockPage.css';

function StockPage() {
  const [stockList, setStockList] = useState([]);
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [showAddForm, setShowAddForm] = useState(false);
  const [addStockForm, setAddStockForm] = useState({
    productId: '',
    currentStock: '',
    minimumStock: '',
    reorderPoint: '',
  });

  useEffect(() => {
    fetchStockData();
    fetchProducts();

    // 🔍 CORS test endpoint
    fetch('/test/cors', {
      method: 'GET',
      credentials: 'include',
    })
      .then((res) => res.text())
      .then((data) => console.log('CORS test:', data))
      .catch((err) => console.error('CORS test failed:', err));
  }, []);

  const fetchStockData = async () => {
    try {
      const response = await fetch('/list/stock', {
        method: 'GET',
        credentials: 'include',
        headers: { 'Content-Type': 'application/json' },
      });

      if (response.ok) {
        const data = await response.json();
        setStockList(data);
      } else {
        setError('Failed to load stock data');
      }
    } catch (error) {
      setError('Error loading stock data: ' + error.message);
    } finally {
      setLoading(false);
    }
  };

  const fetchProducts = async () => {
    try {
      const response = await fetch('/list/products', {
        method: 'GET',
        credentials: 'include',
        headers: { 'Content-Type': 'application/json' },
      });

      if (!response.ok) {
        throw new Error('Network response was not ok');
      }

      const data = await response.json();
      setProducts(data);
    } catch (error) {
      console.error('Error loading products:', error);
      setError('Error loading products: ' + error.message);
    }
  };

  const handleAddStock = async (e) => {
    e.preventDefault();

    const selectedProduct = products.find(
      (p) => p.id == addStockForm.productId
    );
    if (!selectedProduct) {
      setError('Please select a valid product');
      return;
    }

    try {
      const response = await fetch('/save/stock', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify({
          product: { id: selectedProduct.id, name: selectedProduct.name },
          currentStock: parseInt(addStockForm.currentStock),
          minimumStock: parseInt(addStockForm.minimumStock),
          reorderPoint: parseInt(addStockForm.reorderPoint),
        }),
      });

      if (response.ok) {
        setSuccess('Stock added successfully!');
        setAddStockForm({
          productId: '',
          currentStock: '',
          minimumStock: '',
          reorderPoint: '',
        });
        setShowAddForm(false);
        fetchStockData();
      } else {
        const errorText = await response.text();
        setError('Failed to add stock: ' + errorText);
      }
    } catch (error) {
      setError('Error adding stock: ' + error.message);
    }
  };

  const handleReduceStock = async (productId, quantity) => {
    try {
      const response = await fetch(
        `/reduce/stock?productId=${productId}&quantity=${quantity}`,
        {
          method: 'POST',
          credentials: 'include',
          headers: { 'Content-Type': 'application/json' },
        }
      );

      if (response.ok) {
        setSuccess('Stock reduced successfully!');
        fetchStockData();
      } else {
        const errorText = await response.text();
        setError('Failed to reduce stock: ' + errorText);
      }
    } catch (error) {
      setError('Error reducing stock: ' + error.message);
    }
  };

  const getStockStatus = (currentStock, minimumStock) => {
    if (currentStock <= minimumStock) {
      return 'Low Stock';
    } else if (currentStock <= minimumStock * 2) {
      return 'Medium Stock';
    } else {
      return 'Good Stock';
    }
  };

  const getStatusColor = (status) => {
    switch (status) {
      case 'Low Stock':
        return 'red';
      case 'Medium Stock':
        return 'orange';
      case 'Good Stock':
        return 'green';
      default:
        return 'black';
    }
  };

  if (loading) {
    return <div className="stock-page">Loading stock data...</div>;
  }

  return (
    <div className="stock-page">
      <h1>Stock Management</h1>

      {error && <div className="error-message">{error}</div>}
      {success && <div className="success-message">{success}</div>}

      <div className="stock-controls">
        <button
          onClick={() => setShowAddForm(!showAddForm)}
          className="add-stock-btn"
        >
          {showAddForm ? 'Cancel' : 'Add New Stock'}
        </button>
      </div>

      {showAddForm && (
        <div className="add-stock-form">
          <h3>Add New Stock</h3>
          <form onSubmit={handleAddStock}>
            <div className="form-group">
              <label>Product:</label>
              <select
                value={addStockForm.productId}
                onChange={(e) =>
                  setAddStockForm({ ...addStockForm, productId: e.target.value })
                }
                required
              >
                <option value="">Select a product</option>
                {products.map((product) => (
                  <option key={product.id} value={product.id}>
                    {product.name} - ${product.price}
                  </option>
                ))}
              </select>
            </div>

            <div className="form-group">
              <label>Current Stock:</label>
              <input
                type="number"
                value={addStockForm.currentStock}
                onChange={(e) =>
                  setAddStockForm({
                    ...addStockForm,
                    currentStock: e.target.value,
                  })
                }
                min="0"
                required
              />
            </div>

            <div className="form-group">
              <label>Minimum Stock:</label>
              <input
                type="number"
                value={addStockForm.minimumStock}
                onChange={(e) =>
                  setAddStockForm({
                    ...addStockForm,
                    minimumStock: e.target.value,
                  })
                }
                min="0"
                required
              />
            </div>

            <div className="form-group">
              <label>Reorder Point:</label>
              <input
                type="number"
                value={addStockForm.reorderPoint}
                onChange={(e) =>
                  setAddStockForm({
                    ...addStockForm,
                    reorderPoint: e.target.value,
                  })
                }
                min="0"
                required
              />
            </div>

            <button type="submit" className="submit-btn">
              Add Stock
            </button>
          </form>
        </div>
      )}

      <div className="stock-list">
        <h2>Current Stock</h2>
        {stockList.length === 0 ? (
          <p>No stock data available</p>
        ) : (
          <table className="stock-table">
            <thead>
              <tr>
                <th>Product</th>
                <th>Current Stock</th>
                <th>Minimum Stock</th>
                <th>Reorder Point</th>
                <th>Status</th>
                <th>Last Updated</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {stockList.map((stock) => {
                const status = getStockStatus(
                  stock.currentStock,
                  stock.minimumStock
                );
                return (
                  <tr key={stock.id}>
                    <td>{stock.productName}</td>
                    <td>{stock.currentStock}</td>
                    <td>{stock.minimumStock}</td>
                    <td>{stock.reorderPoint}</td>
                    <td style={{ color: getStatusColor(status) }}>
                      {status}
                    </td>
                    <td>{new Date(stock.lastUpdated).toLocaleString()}</td>
                    <td>
                      <button
                        onClick={() => handleReduceStock(stock.productId, 1)}
                        className="reduce-btn"
                        disabled={stock.currentStock <= 0}
                      >
                        Reduce by 1
                      </button>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}

export default StockPage;
