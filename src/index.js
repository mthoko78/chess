/**
=========================================================
* Material Kit 2 React - v2.1.0
=========================================================

* Product Page: https://www.creative-tim.com/product/material-kit-react
* Copyright 2023 Creative Tim (https://www.creative-tim.com)

Coded by www.creative-tim.com

 =========================================================

* The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
*/

import React from "react";
import * as ReactDOMClient from "react-dom/client";
import { BrowserRouter } from "react-router-dom";
import App from "App";
import './index.css';


// Import the functions you need from the SDKs you need
import {initializeApp} from "firebase/app";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
const firebaseConfig = {
  apiKey: "AIzaSyD9lDylJbFAjDtW0AaeGcK8Jpc2S44HY4Y",
  authDomain: "device-monitor-1579861656148.firebaseapp.com",
  databaseURL: "https://device-monitor-1579861656148.firebaseio.com",
  projectId: "device-monitor-1579861656148",
  storageBucket: "device-monitor-1579861656148.appspot.com",
  messagingSenderId: "949504494621",
  appId: "1:949504494621:web:710cfc29b2fbdb596bcaf4"
};

// Initialize Firebase
initializeApp(firebaseConfig);
const container = document.getElementById("root");

// Create a root.
const root = ReactDOMClient.createRoot(container);

root.render(
  <BrowserRouter>
    <App />
  </BrowserRouter>
);
