import React, { useState, useEffect } from 'react';
import './Login.css';

function LoginPage({ setIsLoggedIn }) {
  const [isSignup, setIsSignup] = useState(false);
  const [formData, setFormData] = useState({
    name: '',
    phoneno: '',
    password: '',
  });

  // Check session on mount
  useEffect(() => {
    const checkSession = async () => {
      try {
        const response = await fetch('/check-session', {
          credentials: 'include',
        });

        if (response.ok) {
          setIsLoggedIn(true);
        }
      } catch {
        console.log('No active session');
      }
    };

    checkSession();
  }, [setIsLoggedIn]);

  const toggleForm = () => {
    setIsSignup((prev) => !prev);
    setFormData({ name: '', phoneno: '', password: '' });
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const url = isSignup
      ? '/save/user'
      : '/login';

    const body = isSignup
      ? { ...formData, phoneno: Number(formData.phoneno) }
      : { phoneno: Number(formData.phoneno), password: formData.password };

    try {
      const response = await fetch(url, {
        method: 'POST',
        credentials: 'include',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body),
      });

      if (response.ok) {
        alert(`${isSignup ? 'Signup' : 'Login'} successful!`);
        setFormData({ name: '', phoneno: '', password: '' });
        if (!isSignup) setIsLoggedIn(true);
      } else {
        const errorText = await response.text();
        alert(`${isSignup ? 'Signup' : 'Login'} failed: ${errorText}`);
      }
    } catch (err) {
      console.error('Error:', err);
      alert('Something went wrong. Try again.');
    }
  };

  return (
    <div className="auth-container">
      <form onSubmit={handleSubmit} className="auth-form">
        <h2>{isSignup ? 'Sign Up' : 'Login'}</h2>

        {isSignup && (
          <input
            type="text"
            name="name"
            placeholder="Full Name"
            value={formData.name}
            onChange={handleChange}
            required
          />
        )}

        <input
          type="text"
          name="phoneno"
          placeholder="Phone Number"
          value={formData.phoneno}
          onChange={handleChange}
          required
        />

        <input
          type="password"
          name="password"
          placeholder="Password"
          value={formData.password}
          onChange={handleChange}
          required
        />

        <button type="submit">
          {isSignup ? 'Sign Up' : 'Login'}
        </button>

        <p className="toggle-link">
          {isSignup
            ? 'Already have an account?'
            : "Don't have an account?"}{' '}
          <span onClick={toggleForm}>
            {isSignup ? 'Login here' : 'Sign up here'}
          </span>
        </p>
      </form>
    </div>
  );
}

export default LoginPage;
