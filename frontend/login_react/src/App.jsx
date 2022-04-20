import React from 'react'
import './App.css'
import {Routes, Route} from 'react-router-dom'
import Home from './pages/Home'
// import LoginPage from './pages/LoginPage'
// import RegisterPage from './pages/RegisterPa ge'
import LoginRegisterPage from './pages/LoginRegisterPage'

function App() {
    return (
        <div className="App">
            <Routes>
                <Route path="/" element={<Home />} />
                {/* <Route path="/login" element={<LoginPage />} /> */}
                {/* <Route path="/register" element={<RegisterPage />} /> */}
                <Route path="/loginRegister" element={<LoginRegisterPage />} />
            </Routes>
        </div>
    )
}

export default App
