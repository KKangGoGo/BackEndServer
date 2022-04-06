import React, {useEffect} from 'react'
import {useDispatch, useSelector} from 'react-redux'
import {useNavigate, Link} from 'react-router-dom'
import {logoutUser} from '../_actions/userAction'

const Home = () => {
    const navigate = useNavigate()
    const dispatch = useDispatch()

    const user = useSelector(state => state.user)
    console.log(user)

    const logoutHandler = e => {
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
            {user.loginSuccess ? (
                <button className="Btn" onClick={logoutHandler}>
                    LOGOUT
                </button>
            ) : (
                <div>
                    <Link to="/login">Sign In</Link>
                    <br />
                    <Link to="/register">Sign Up</Link>
                </div>
            )}
        </div>
    )
}

export default Home
