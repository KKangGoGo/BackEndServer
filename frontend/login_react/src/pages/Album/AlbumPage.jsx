import React, {useEffect, useState} from 'react'

import styles from './AlbumPage.module.css'

import {HiOutlineSearch, HiOutlineUpload} from 'react-icons/hi'
import {IoAlbumsOutline} from 'react-icons/io5'
import AlbumUploadModal from './AlbumUpload/AlbumUploadModal'
import {useDispatch} from 'react-redux'
import {useNavigate} from 'react-router-dom'
import {getAlbums} from '../../_actions/albumAction'

const AlbumPage = () => {
    const [isOpen, setIsOpen] = useState(false)
    const modalToggle = () => setIsOpen(!isOpen)

    const [Albums, setAlbums] = useState([])

    const dispatch = useDispatch()
    const navigate = useNavigate()

    useEffect(() => {
        const token = localStorage.getItem('login-token')
        dispatch(getAlbums(token))
            .then(res => {
                console.log(res)
                setAlbums(res.payload.albumlist)
            })
            .catch(e => console.log(e.message))
    }, [])

    const RenderShortcut = Albums.map((album, index) => {
        return (
            <a key={index} href="">
                <div>
                    <IoAlbumsOutline />
                    <span>{album.title}</span>
                </div>
            </a>
        )
    })

    const RenderAlbum = Albums.map((album, index) => {
        return (
            <li key={index}>
                <a href={`album/${album.albumId}`}>
                    <img src={`${album.image}`} alt="" />
                    <div>{album.title}</div>
                    <div>2022.05.08 생성</div>
                </a>
            </li>
        )
    })

    return (
        <div className={styles.container}>
            <div className={styles.header}>
                <div className={styles.search}>
                    <input type="text" placeholder="앨범 검색" />
                    <button>
                        <HiOutlineSearch />
                    </button>
                </div>
                <div className={styles.createAlbum}>
                    <button onClick={modalToggle}>
                        <HiOutlineUpload />새 앨범
                    </button>
                    <AlbumUploadModal isOpen={isOpen} onRequestClose={modalToggle} onSubmit={modalToggle} onCancel={modalToggle} />
                </div>
            </div>
            <div className={styles.text}>앨범 바로가기</div>

            <div className={styles.shortcut}>{RenderShortcut}</div>

            <div className={styles.main}>
                <div className={styles.text}>앨범</div>
                <ul>
                    {RenderAlbum}
                    {/* <li>
                        <a href="">
                            <img
                                src="https://images.generated.photos/HGBpoZ-Lt2kjqYM-Fbt84-HIVsX6wIGosKWKQ3RAx_w/rs:fit:256:256/czM6Ly9pY29uczgu/Z3Bob3Rvcy1wcm9k/LnBob3Rvcy92M18w/ODU0NDU4LmpwZw.jpg"
                                alt=""
                            />
                            <div>가족 앨범</div>
                            <div>2022.05.08 생성</div>
                        </a>
                    </li>
                    <li>
                        <a href="">
                            <img
                                src="https://images.generated.photos/LgRTka3d_IuqVbfykX-uZ79LDo5ja5AaRVwRsTOKnN4/rs:fit:256:256/czM6Ly9pY29uczgu/Z3Bob3Rvcy1wcm9k/LnBob3Rvcy92M18w/OTMyODE3LmpwZw.jpg"
                                alt=""
                            />
                            <div>추억 앨범</div>
                            <div>2022.05.08 생성</div>
                        </a>
                    </li>
                    <li>
                        <a href="">
                            <img
                                src="https://images.generated.photos/S1ITjw8eVG_pfismlnVp_alsmeymnlENOY_fHva7TD0/rs:fit:256:256/czM6Ly9pY29uczgu/Z3Bob3Rvcy1wcm9k/LnBob3Rvcy92M18w/Njc0NjMzLmpwZw.jpg"
                                alt=""
                            />
                            <div>연인 앨범</div>
                            <div>2022.05.08 생성</div>
                        </a>
                    </li>
                    <li>
                        <a href="">
                            <img
                                src="https://images.generated.photos/V0z89lqApCatKP9xzxSKA6lYAr2b8i-l5aT-T6lXeQw/rs:fit:256:256/czM6Ly9pY29uczgu/Z3Bob3Rvcy1wcm9k/LnBob3Rvcy92M18w/MzQwMTA2LmpwZw.jpg"
                                alt=""
                            />
                            <div>친구 앨범</div>
                            <div>2022.05.08 생성</div>
                        </a>
                    </li>
                    <li>
                        <a href="">
                            <img
                                src="https://images.generated.photos/xd44DoYoYi8LTkG8IhV-ta_lSjKYMqa3eRwjIIkshb4/rs:fit:256:256/czM6Ly9pY29uczgu/Z3Bob3Rvcy1wcm9k/LnBob3Rvcy92M18w/Mjg3ODAwLmpwZw.jpg"
                                alt=""
                            />
                            <div>기타 앨범</div>
                            <div>2022.05.08 생성</div>
                        </a>
                    </li> */}
                </ul>
            </div>
        </div>
    )
}

export default AlbumPage
