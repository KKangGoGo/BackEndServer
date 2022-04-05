import React, {useState} from 'react'
import {useDispatch} from 'react-redux'
import {loginUser} from '../_actions/userAction'
import {useNavigate} from 'react-router-dom'

function LoginPage(props) {
    const [state, setState] = useState({
        email: '',
        password: '',
    })

    const {email, password} = state

    const navigate = useNavigate()
    const dispatch = useDispatch()

    const handleInputChange = e => {
        let {name, value} = e.target
        setState({...state, [name]: value})
    }

    const onSubmitHandler = e => {
        e.preventDefault()

        try {
            dispatch(loginUser(state))
            navigate('/')
        } catch (error) {
            console.log(error)
        }
    }

    return (
        <div className="Login">
            <h2>Sign in</h2>
            <form
                onSubmit={onSubmitHandler}
                style={{display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center'}}
            >
                <input placeholder="email" name="email" value={email} onChange={handleInputChange} />
                <input placeholder="password" name="password" type="password" value={password} onChange={handleInputChange} />

                <button type="submit">LOGIN</button>
            </form>
        </div>
    )
}

export default LoginPage
