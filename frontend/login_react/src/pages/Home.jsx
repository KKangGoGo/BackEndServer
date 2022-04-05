import axios from 'axios'
import React from 'react'
import {useDispatch} from 'react-redux'
import {useNavigate} from 'react-router-dom'
import {logoutUser} from '../_actions/userAction'

const Home = () => {
    const navigate = useNavigate()
    const dispatch = useDispatch()

    const logoutHandler = () => {
        try {
            dispatch(logoutUser)
            navigate('/login')
        } catch (error) {
            console.log(error, '로그아웃 실패')
        }
    }
    return (
        <div>
            <h2>home</h2>
            <button className="Btn" onClick={logoutHandler}>
                LOGOUT
            </button>
        </div>
    )
}

export default Home
