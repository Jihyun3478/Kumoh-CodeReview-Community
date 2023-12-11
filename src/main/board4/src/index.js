import React, { Fragment } from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter as Router } from 'react-router-dom';
import './index.css';
import App from './App';
import AuthProvider from './components/AuthProvider';

ReactDOM.render(
  <Router>
    <AuthProvider>
  <App />
  </AuthProvider>
</Router>
  ,
  document.getElementById('root')
)