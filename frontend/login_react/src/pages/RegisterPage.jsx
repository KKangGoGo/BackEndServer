import React, {useState} from 'react'
import {useDispatch} from 'react-redux'
import {registerUser} from '../_actions/userAction'
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

function RegisterPage(props) {
    const [state, setState] = useState({
        username: '',
        password: '',
        email: '',
    })

    const {username, email, password} = state

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

    const onSubmitHandler = async e => {
        e.preventDefault()
        let formData = new FormData()

        let files = e.target.image.files
        formData.append('photo', files[0])
        formData.append('signupInfo', new Blob([JSON.stringify(state)], {type: 'application/json'}))

        if (password !== ConfirmPassword) {
            return alert('비밀번호가 일치하지 않습니다.')
        }

        try {
            dispatch(registerUser(formData)).then(res => {
                console.log(res)
                window.location.replace('/loginRegister')
            })
        } catch (error) {
            console.log(error)
        }
    }

    return (
        <>
            <form
                onSubmit={onSubmitHandler}
                style={{display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center', color: '#848484'}}
            >
                <label htmlFor="">Username</label>
                <Input type="text" placeholder="username" name="username" value={username} onChange={handleInputChange} />

                <label htmlFor="">Password</label>
                <Input type="password" placeholder="password" name="password" value={password} onChange={handleInputChange} />

                <label htmlFor="">ConfirmPassword</label>
                <Input type="password" placeholder="confirm-password" value={ConfirmPassword} onChange={onConfirmPasswordHandler} />

                <label htmlFor="">Email</label>
                <Input type="text" placeholder="email" name="email" value={email} onChange={handleInputChange} />

                <label htmlFor="">Image</label>
                <Input type="file" name="image" multiple="multiple" />

                <button>sign up</button>
            </form>
        </>
    )
}

export default RegisterPage
