import React from 'react'
import {Route, Routes, useNavigate} from 'react-router-dom'
import AlbumPage from './Album/AlbumPage'
import MainPage from './Main/MainPage'
import Navbar from './Nav/Navbar'

import styles from './NestedRouting.module.css'
import img from '../img/Face Album.png'
import {useDispatch, useSelector} from 'react-redux'
import {logoutUser} from '../_actions/userAction'

function NestedRouting() {
    const dispatch = useDispatch()
    const navigate = useNavigate()
    const user = useSelector(state => state.user)

    const logoutHandler = e => {
        const token = localStorage.getItem('login-token')
        localStorage.removeItem('login-token')
        try {
            dispatch(logoutUser(token)).then(res => {
                navigate('/loginRegister')
            })
        } catch (error) {
            console.log(error, '로그아웃 실패')
        }
    }

    return (
        <div className={styles.container}>
            <div className={styles.header}>
                <div className={styles.logo}>
                    <img src="https://cdn-icons-png.flaticon.com/128/2793/2793754.png" alt="" />
                    <img src={img} alt="logo" />
                    {user.userData ? (
                        <div className={styles.user}>
                            <div className={styles.img}>
                                <img
                                    src="data:image/jpeg;base64,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"
                                    alt=""
                                />
                            </div>
                            <div className={styles.logout}>
                                <button onClick={logoutHandler}>logout</button>
                            </div>

                            <div className={styles.logout}>
                                <button onClick={logoutHandler}>logout</button>
                            </div>
                        </div>
                    ) : (
                        <div className={styles.user}>
                            <div className={styles.logout}>
                                <button
                                    onClick={() => {
                                        navigate('/loginRegister')
                                    }}
                                >
                                    Sign in
                                </button>
                            </div>
                        </div>
                    )}
                </div>
            </div>
            <div className={styles.leftSide}>
                <Navbar />
            </div>
            <div className={styles.rightSide}>
                <Routes>
                    <Route path="main" element={<MainPage />} />
                    <Route path="album" element={<AlbumPage />} />
                </Routes>
            </div>
        </div>
    )
}

export default NestedRouting
