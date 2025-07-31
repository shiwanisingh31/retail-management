import React from 'react';
import { Link } from 'react-router-dom';
import './Navbar.css';

function NavbarPage({ isLoggedIn, onLogout }) {
  return (
    <nav className="navbar">
      <div className="navbar-logo">Retail Dashboard</div>

      <ul className="navbar-links">
        <li><Link to="/products">🛒 Product Management</Link></li>
        <li><Link to="/customers">👥 Customer Management</Link></li>
        <li><Link to="/sales">💰 Sales Management</Link></li>
        <li><Link to="/stock">📦 Stock Tracking</Link></li>
        <li><Link to="/search">🔍 Search Products</Link></li>
        
      </ul>

      <div className="navbar-auth">
        {isLoggedIn ? (
          <button onClick={onLogout}>Logout</button>
        ) : (
          <Link to="/login">
            <button>Login</button>
          </Link>
        )}
      </div>
    </nav>
  );
}

export default NavbarPage;

