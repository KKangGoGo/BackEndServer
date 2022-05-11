import React from 'react'
import './App.css'
import {Routes, Route} from 'react-router-dom'
import InitialPage from './pages/Initial/InitialPage'
import LoginRegisterPage from './pages/User/LoginRegisterPage'
import NestedRouting from './pages/NestedRouting'

function App() {
    return (
        <div className="App">
            <Routes>
                <Route path="/" element={<InitialPage />} />
                <Route path="/loginRegister" element={<LoginRegisterPage />} />
                <Route path="/user/*" element={<NestedRouting />} />
            </Routes>
        </div>
    )
}

export default App
