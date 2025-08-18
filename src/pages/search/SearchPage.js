import React, { useState, useEffect } from 'react';
import './SearchPage.css';

const SearchProducts = () => {
  const [searchTerm, setSearchTerm] = useState('');
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(false);

  const fetchAllProducts = async () => {
    setLoading(true);
    try {
      const response = await fetch('/list/products');
      if (!response.ok) {
        throw new Error(await response.text());
      }
      const data = await response.json();
      setResults(data);
    } catch (err) {
      console.error("Failed to fetch products", err);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      const url = searchTerm.trim()
        ? `/search/products?productName=${encodeURIComponent(searchTerm)}`
        : `/list/products`;

      const response = await fetch(url);
      if (!response.ok) {
        throw new Error(await response.text());
      }
      const data = await response.json();
      setResults(data);
    } catch (err) {
      console.error("Search failed", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAllProducts(); // Show all products by default
  }, []);

  return (
    <div className="search-products">
      <h2>Search Products</h2>
      <form className="search-form" onSubmit={handleSearch}>
        <input
          type="text"
          placeholder="Search by product name..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />
        <button type="submit">Search</button>
      </form>

      {loading ? (
        <p>Loading...</p>
      ) : results.length > 0 ? (
        <ul className="search-results">
          {results.map(product => (
            <li key={product.id}>
              <strong>{product.name}</strong>
            </li>
          ))}
        </ul>
      ) : (
        <p>No results found.</p>
      )}
    </div>
  );
};

export default SearchProducts;
