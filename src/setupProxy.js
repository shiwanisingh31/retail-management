const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function(app) {
  // Proxy auth server (e.g., Keycloak) to avoid browser CORS
  app.use(
    ['/auth', '/realms', '/protocol'],
    createProxyMiddleware({
      target: 'http://localhost:8444',
      changeOrigin: true,
      secure: false,
      cookieDomainRewrite: 'localhost',
      logLevel: 'silent'
    })
  );

  // Proxy application backend APIs
  app.use(
    [
      '/save',
      '/list',
      '/delete',
      '/search',
      '/login',
      '/logout',
      '/check-session',
      '/sales',
      '/stock',
      '/low-stock',
      '/update',
      '/reduce'
    ],
    createProxyMiddleware({
      target: 'http://localhost:8081',
      changeOrigin: true,
      secure: false,
      cookieDomainRewrite: 'localhost',
      logLevel: 'silent'
    })
  );
};

