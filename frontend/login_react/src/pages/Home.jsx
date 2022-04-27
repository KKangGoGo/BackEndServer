import React, {useEffect} from 'react'
import {useDispatch, useSelector} from 'react-redux'
import {useNavigate, Link} from 'react-router-dom'
import {logoutUser} from '../_actions/userAction'
import Auth from '../hoc/auth'

import './styles/Home.module.css'

const Home = () => {
    const navigate = useNavigate()
    const dispatch = useDispatch()

    const user = useSelector(state => state.user)
    console.log(user)

    const logoutHandler = e => {
        localStorage.removeItem('login-token')
        try {
            dispatch(logoutUser).then(res => {
                console.log(res)
                navigate('/login')
            })
        } catch (error) {
            console.log(error, '로그아웃 실패')
        }
    }

    return (
        <div>
            <h2>home</h2>
            {user.userData ? (
                <>
                    <div>{user.userData.username}</div>
                    <div>{user.userData.email}</div>
                    <button className="Btn" onClick={logoutHandler}>
                        LOGOUT
                    </button>
                </>
            ) : (
                <div>
                    <Link to="/loginRegister">Sign In</Link>
                    <button className="Btn" onClick={logoutHandler}>
                        LOGOUT
                    </button>
                </div>
            )}
        </div>
    )
}

export default Auth(Home, null)
