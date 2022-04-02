import './App.css'
import {Routes, Route, BrowserRouter} from 'react-router-dom'
import Home from './pages/Home'
import Login from './pages/Login'
import Register from './pages/Register'

import {useDispatch} from 'react-redux'
import {useEffect} from 'react'
import {auth} from './firebase'
import {setUser} from './_actions/actions'

function App() {
    const dispatch = useDispatch()

    useEffect(() => {
        auth.onAuthStateChanged(authUser => {
            console.log(authUser)
            if (authUser) {
                dispatch(setUser(authUser))
            } else {
                dispatch(setUser(null))
            }
        })
    }, [dispatch])

    return (
        <BrowserRouter>
            <div className="App">
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/login" element={<Login />} />
                    <Route path="/register" element={<Register />} />
                </Routes>
            </div>
        </BrowserRouter>
    )
}

export default App
