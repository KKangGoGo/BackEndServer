import React, {useState} from 'react'
import {useDispatch, useSelector} from 'react-redux'
import {loginUser} from '../../../_actions/userAction'
import {useNavigate} from 'react-router-dom'

import styles from './LoginPage.module.css'

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
                <input className={styles.input} placeholder="username" name="username" value={username} onChange={handleInputChange} />
                <input
                    className={styles.input}
                    placeholder="password"
                    name="password"
                    type="password"
                    value={password}
                    onChange={handleInputChange}
                />

                <button type="submit">LOGIN</button>
            </form>
        </div>
    )
}

export default LoginPage
