// src/App.js
import React, { useEffect, useState } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';

import Login from "./pages/login_page/LoginPage";
import ProductPage from './pages/product/ProductPage';
import CustomerPage from './pages/customer/CustomerPage';
import SalesPage from './pages/sales/SalesPage';
import SearchProducts from './pages/search/SearchPage';
import NavbarPage from './pages/navbar/NavbarPage';

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(null); // null = loading

  useEffect(() => {
    fetch('http://localhost:8081/check-session', {
      method: 'GET',
      credentials: 'include',
    })
      .then(res => setIsLoggedIn(res.ok))
      .catch(() => setIsLoggedIn(false));
  }, []);

  const handleLogout = async () => {
    try {
      const res = await fetch('http://localhost:8081/logout', {
        method: 'POST',
        credentials: 'include',
      });
      if (res.ok) {
        setIsLoggedIn(false);
        window.location.href = "/login";
      }
    } catch (err) {
      console.error("Logout failed", err);
    }
  };

  if (isLoggedIn === null) return <div>Loading...</div>; // optional loading UI

  return (
    <Router>
      {isLoggedIn && <NavbarPage isLoggedIn={isLoggedIn} onLogout={handleLogout} />}

      <Routes>
        <Route
          path="/login"
          element={
            isLoggedIn ? <Navigate to="/products" replace /> : <Login setIsLoggedIn={setIsLoggedIn} />
          }
        />
        <Route
          path="/products"
          element={
            isLoggedIn ? <ProductPage /> : <Navigate to="/login" replace />
          }
        />
        <Route
          path="/customers"
          element={
            isLoggedIn ? <CustomerPage /> : <Navigate to="/login" replace />
          }
        />
        <Route
          path="/sales"
          element={
            isLoggedIn ? <SalesPage /> : <Navigate to="/login" replace />
          }
        />
        <Route
          path="/search"
          element={
            isLoggedIn ? <SearchProducts /> : <Navigate to="/login" replace />
          }
        />
        <Route
          path="*"
          element={<Navigate to={isLoggedIn ? "/products" : "/login"} replace />}
        />
      </Routes>
    </Router>
  );
}

export default App;