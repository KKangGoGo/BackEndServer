import React from 'react'
import {Route, Routes, useNavigate} from 'react-router-dom'
import AlbumPage from './Album/AlbumPage'
import Navbar from './Nav/Navbar'
import HomePage from './Home/HomePage'

import styles from './NestedRouting.module.css'
import img from '../img/Face Album.png'
import {useDispatch, useSelector} from 'react-redux'
import {logoutUser} from '../_actions/userAction'
import AlbumDetailPage from './Album/AlbumDetail/AlbumDetailPage'
import MemberPage from './Member/MemberPage'

function NestedRouting() {
    const dispatch = useDispatch()
    const navigate = useNavigate()
    const user = useSelector(state => state.user)

    const logoutHandler = e => {
        const Atoken = localStorage.getItem('access-token')
        const Rtoken = localStorage.getItem('refresh-token')
        localStorage.removeItem('access-token')
        localStorage.removeItem('refresh-token')
        try {
            dispatch(logoutUser(Atoken, Rtoken)).then(res => {
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
                    <a href="/user/album">
                        <img src="https://cdn-icons-png.flaticon.com/128/2005/2005140.png" alt="" />
                        {/* <img src={user.userData.photo} alt="" /> */}
                    </a>
                    <img src={img} alt="logo" />
                    {user.userData ? (
                        <div className={styles.user}>
                            <div className={styles.img}>
                                <img src={user.userData.photo} alt="" />
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
                    <Route path="home" element={<HomePage />} />
                    <Route path="album" element={<AlbumPage />} />
                    <Route path="album/:albumId" element={<AlbumDetailPage />} />
                    <Route path="member" element={<MemberPage />} />
                </Routes>
            </div>
        </div>
    )
}

export default NestedRouting
