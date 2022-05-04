import React, {useState} from 'react'
import {useDispatch} from 'react-redux'
import {loginUser} from '../../_actions/userAction'
import {useNavigate} from 'react-router-dom'

import styled from 'styled-components'

const Input = styled.input`
    line-height: 20px;
    border: none;
    border-bottom: 1px solid #848484;
    margin-bottom: 20px;
    &:focus {
        outline: none;
        border-bottom: 2px solid #704598;
    }
`

function LoginPage(props) {
    const [state, setState] = useState({
        username: '',
        password: '',
    })

    const {username, password} = state

    const navigate = useNavigate()
    const dispatch = useDispatch()

    const handleInputChange = e => {
        let {name, value} = e.target
        setState({...state, [name]: value})
    }

    const onSubmitHandler = e => {
        e.preventDefault()

        try {
            dispatch(loginUser(state)).then(res => {
                console.log(res)
                navigate('/user/main')
            })
        } catch (error) {
            console.log(error)
        }
    }

    return (
        <div className="Login" style={{display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center'}}>
            <form
                onSubmit={onSubmitHandler}
                style={{display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center'}}
            >
                <Input placeholder="username" name="username" value={username} onChange={handleInputChange} />
                <Input placeholder="password" name="password" type="password" value={password} onChange={handleInputChange} />

                <button type="submit">LOGIN</button>
            </form>
        </div>
    )
}

export default LoginPage
