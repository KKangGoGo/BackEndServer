import React, {useState} from 'react'
import TextField from '@mui/material/TextField'
import {Button} from '@mui/material'
import {useNavigate} from 'react-router-dom'
import {useDispatch} from 'react-redux'
import {addUser} from '../redux/actions'

const AddUser = () => {
    const [state, setState] = useState({
        name: '',
        email: '',
        phone: '',
        address: '',
        image: '',
    })
    const [error, setError] = useState('')

    let dispatch = useDispatch()
    let navigate = useNavigate()

    const {name, email, phone, address, image} = state

    const handleInputChange = e => {
        let {name, value} = e.target
        setState({...state, [name]: value})
    }
    const handleSubmit = e => {
        console.log(e)
        e.preventDefault()

        if (!name || !phone || !email || !address || !image) {
            setError('please input all input field')
        } else {
            dispatch(addUser(state))
            navigate('/')
            setError('')
        }
    }

    return (
        <div>
            <Button variant="contained" style={{background: 'red'}} type="submit" onClick={() => navigate('/')}>
                Go Back
            </Button>
            <h2>Add User</h2>
            {error && <h3>{error}</h3>}
            <form action="" onSubmit={handleSubmit}>
                {/* <TextField id="filled-basic" label="Name" variant="filled" value={name} type="text" /> */}
                <textarea placeholder="name" name="name" id="name" value={name} onChange={handleInputChange} />
                <br />
                {/* <TextField id="filled-basic" label="Email" variant="filled" value={email} type="email" onChange={e => console.log(e)} /> */}
                <textarea placeholder="email" name="email" id="email" value={email} onChange={handleInputChange} />
                <br />
                {/* <TextField id="filled-basic" label="Contact" variant="filled" value={contact} type="number" onChange={handleInputChange} /> */}
                <textarea placeholder="phone" name="phone" id="phone" value={phone} onChange={handleInputChange} />
                <br />
                {/* <TextField id="filled-basic" label="Address" variant="filled" value={address} type="text" onChange={handleInputChange} /> */}
                <textarea placeholder="address" name="address" id="address" value={address} onChange={handleInputChange} />
                <br />
                <textarea placeholder="image" name="image" id="image" value={image} onChange={handleInputChange} />
                <br />
                <button> add user</button>
            </form>
        </div>
    )
}

export default AddUser
