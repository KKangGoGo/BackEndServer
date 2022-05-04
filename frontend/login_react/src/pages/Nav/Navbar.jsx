import React from 'react'
import {useDispatch, useSelector} from 'react-redux'
import {useNavigate} from 'react-router-dom'
import {logoutUser} from '../../_actions/userAction'
import Auth from '../Hoc/auth'
import styles from './Navbar.module.css'

import img from '../../img/Face Album.png'
import face from '../../img/face.jpg'

import {FiChevronRight} from 'react-icons/fi'
import {HiOutlineHome} from 'react-icons/hi'
import {HiOutlinePhotograph} from 'react-icons/hi'
import {HiOutlineUserGroup} from 'react-icons/hi'
import {HiOutlineUserCircle} from 'react-icons/hi'

function Navbar() {
    const navigate = useNavigate()
    const dispatch = useDispatch()

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

    const navHandler = () => {
        navigate('/user/album')
    }

    return (
        <div className={styles.container}>
            <div className={styles.logo}>
                <img src={img} alt="logo" />
            </div>

            <div className={styles.face}>
                <img src={face} alt="" />
            </div>
            <div className={styles.username}>KO JH</div>

            <ul>
                <li>
                    <button
                        className={styles.btn}
                        onClick={() => {
                            navigate('/user/main')
                        }}
                    >
                        <HiOutlineHome />
                        HOME
                        <FiChevronRight />
                    </button>
                </li>
                <li>
                    <button
                        className={styles.btn}
                        onClick={() => {
                            navigate('/user/album')
                        }}
                    >
                        <HiOutlinePhotograph />
                        ALBUM
                        <FiChevronRight />
                    </button>
                </li>
                <li>
                    <button className={styles.btn} onClick={navHandler}>
                        <HiOutlineUserGroup />
                        MEMBER
                        <FiChevronRight />
                    </button>
                </li>
                <li>
                    <button className={styles.btn} onClick={navHandler}>
                        <HiOutlineUserCircle />
                        MYPAGE
                        <FiChevronRight />
                    </button>
                </li>
            </ul>
            {user.userData ? (
                <div className={styles.logout}>
                    <button onClick={logoutHandler}>logout</button>
                </div>
            ) : (
                <div></div>
            )}
        </div>
    )
}

export default Auth(Navbar, null)
