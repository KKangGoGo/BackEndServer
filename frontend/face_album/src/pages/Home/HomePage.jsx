import React, {useEffect, useState} from 'react'
import {useDispatch, useSelector} from 'react-redux'
import Auth from '../Hoc/auth'

import styles from './HomePage.module.css'

import {BsPlusCircle} from 'react-icons/bs'
import {getAlbums} from '../../_actions/albumAction'

const HomePage = () => {
    const [albums, setAlbums] = useState([])
    const dispatch = useDispatch()
    const user = useSelector(state => state.user)

    useEffect(() => {
        const Atoken = localStorage.getItem('access-token')
        const Rtoken = localStorage.getItem('refresh-token')
        dispatch(getAlbums(Atoken, Rtoken))
            .then(res => {
                setAlbums(res.payload.albumlist)
            })
            .catch(e => console.log(e.message))
    }, [dispatch])

    const userInfo = (
        <>
            {user.userData && (
                <ul>
                    <li>
                        <img src={user.userData.photo} alt="" />
                    </li>
                    <li>
                        <div className={styles.username}>{user.userData.username}</div>
                        <div>{user.userData.email}</div>
                        <div>{user.userData.role}</div>
                    </li>
                </ul>
            )}
        </>
    )

    const albumsInfo = albums.map((album, index) => {
        if (index > 4) return null
        return (
            <li key={index}>
                <div>{album.title}</div>
                <div>2022. 05 .18 생성</div>
            </li>
        )
    })

    return (
        <div className={styles.container}>
            <div className={styles.user}>
                <div></div>
                <div className={styles.userData}>
                    {userInfo}

                    <div className={styles.alarm}>
                        <ul>
                            <li>Ko님의 사진을 공유하였습니다.</li>
                            <li>Ko님이 친구요청을 허락하였습니다.</li>
                            <li>Ji님이 친구요청을 허락하였습니다.</li>
                        </ul>
                    </div>
                </div>
            </div>

            <div className={styles.album}>
                <div>앨범</div>
                <ul>{albumsInfo}</ul>
            </div>

            <div className={styles.friend}>
                <div>친구 요청</div>
                <ul>
                    <li>
                        John(john@naver.com)
                        <BsPlusCircle />
                    </li>
                    <li>
                        Ko(ko@naver.com)
                        <BsPlusCircle />
                    </li>
                    <li>
                        KANG(kang@naver.com)
                        <BsPlusCircle />
                    </li>
                    <li>
                        jun(jun@naver.com)
                        <BsPlusCircle />
                    </li>
                </ul>
            </div>
        </div>
    )
}

export default Auth(HomePage, null)
