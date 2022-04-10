import './App.css'
import {Routes, Route} from 'react-router-dom'
import Home from './pages/Home'
import AddUser from './pages/AddUser'
import EditUser from './pages/EditUser'

function App() {
    return (
        <div>
            <Routes>
                <Route exact path="/" element={<Home />} />
                <Route exact path="/addUser" element={<AddUser />} />
                <Route exact path="/editUser/:id" element={<EditUser />} />
            </Routes>
        </div>
    )
}

export default App
