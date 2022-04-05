import React, {useState} from 'react'
import {useDispatch} from 'react-redux'
import {registerUser} from '../_actions/userAction'

import {useNavigate} from 'react-router-dom'

function RegisterPage(props) {
    const [state, setState] = useState({
        name: '',
        email: '',
        password: '',
    })

    const {name, email, password} = state

    const [ConfirmPassword, setConfirmPassword] = useState('')

    const navigate = useNavigate()
    const dispatch = useDispatch()

    const handleInputChange = e => {
        let {name, value} = e.target
        setState({...state, [name]: value})
    }

    const onConfirmPasswordHandler = e => {
        setConfirmPassword(e.currentTarget.value)
    }

    const onSubmitHandler = e => {
        e.preventDefault()

        if (password !== ConfirmPassword) {
            return alert('비밀번호가 일치하지 않습니다.')
        }

        try {
            dispatch(registerUser(state))

            navigate('/login')
        } catch (error) {
            console.log(error)
        }
    }

    return (
        <form onSubmit={onSubmitHandler} style={{display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center'}}>
            <label htmlFor="">Name</label>
            <input type="text" placeholder="name" name="name" value={name} onChange={handleInputChange} />

            <label htmlFor="">Email</label>
            <input type="text" placeholder="email" name="email" value={email} onChange={handleInputChange} />

            <label htmlFor="">Password</label>
            <input type="password" placeholder="password" name="password" value={password} onChange={handleInputChange} />

            <label htmlFor="">ConfirmPassword</label>
            <input type="password" placeholder="confirm-password" value={ConfirmPassword} onChange={onConfirmPasswordHandler} />

            <button>sign up</button>
        </form>
    )
}

export default RegisterPage
