import React from 'react'
import './App.css'
import {Routes, Route} from 'react-router-dom'
import Home from './pages/Home'
import LoginRegisterPage from './pages/LoginRegisterPage'

function App() {
    return (
        <div className="App">
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/loginRegister" element={<LoginRegisterPage />} />
            </Routes>
        </div>
    )
}

export default App
