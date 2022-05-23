import React from 'react'
import {useDispatch, useSelector} from 'react-redux'
import Auth from '../Hoc/auth'

import styles from './HomePage.module.css'

import {BsPlusCircle} from 'react-icons/bs'

const HomePage = () => {
    // const user = useSelector(state => state.user)
    // const album = useSelector(state => state.album)
    // const albumList = album.albums.albumlist

    // const albums = albumList.map((album, index) => {
    //     // if (index > 4) return null
    //     return (
    //         <li key={index}>
    //             <div>{album.title}</div>
    //             <div>2022. 05 .18 생성</div>
    //         </li>
    //     )
    // })

    return (
        <div className={styles.container}>
            <div className={styles.user}>
                <ul>
                    <li>
                        {/* <img src={user.userData.photo} alt="" /> */}
                        <img
                            src="https://images.generated.photos/xd44DoYoYi8LTkG8IhV-ta_lSjKYMqa3eRwjIIkshb4/rs:fit:256:256/czM6Ly9pY29uczgu/Z3Bob3Rvcy1wcm9k/LnBob3Rvcy92M18w/Mjg3ODAwLmpwZw.jpg"
                            alt=""
                        />
                    </li>
                    <li>
                        {/* <div className={styles.username}>{user.userData.username}</div> */}
                        <div className={styles.username}>KANG</div>
                        {/* <div>{user.userData.email}</div> */}
                        <div>Kang@naver.com</div>
                        {/* <div>{user.userData.role}</div> */}
                        <div>user</div>
                    </li>
                </ul>
            </div>

            <div className={styles.alarm}>
                <div>알림</div>
                <ul>
                    <li>Ko님의 사진을 공유하였습니다.</li>
                    <li>Ko님이 친구요청을 허락하였습니다.</li>
                    <li>Ji님이 친구요청을 허락하였습니다.</li>
                    <li>Dae님의 사진을 공유하였습니다.</li>
                </ul>
            </div>

            <div className={styles.album}>
                <div>앨범</div>
                {/* <ul>{albums}</ul> */}
                <ul>
                    <li>
                        <div>가족 앨범</div>
                        <div>2022. 05 .18 생성</div>
                    </li>
                    <li>
                        <div>연인 앨범</div>
                        <div>2022. 05 .18 생성</div>
                    </li>
                    <li>
                        <div>친구 앨범</div>
                        <div>2022. 05 .18 생성</div>
                    </li>
                    <li>
                        <div>여행 앨범</div>
                        <div>2022. 05 .18 생성</div>
                    </li>
                    <li>
                        <div>기타 앨범</div>
                        <div>2022. 05 .18 생성</div>
                    </li>
                </ul>
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
