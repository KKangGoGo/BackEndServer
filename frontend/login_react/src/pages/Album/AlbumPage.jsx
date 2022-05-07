import React from 'react'

import styles from './AlbumPage.module.css'

import {HiOutlineSearch, HiOutlineUpload} from 'react-icons/hi'
import {IoAlbumsOutline} from 'react-icons/io5'

const AlbumPage = () => {
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
                    <button>
                        <HiOutlineUpload />새 앨범
                    </button>
                </div>
            </div>
            <div className={styles.text}>앨범 바로가기</div>
            <div className={styles.shortcut}>
                <a href="">
                    <div>
                        <IoAlbumsOutline />
                        <span>가족 앨범</span>
                    </div>
                </a>
                <a href="">
                    <div>
                        <IoAlbumsOutline />
                        <span>추억 앨범</span>
                    </div>
                </a>
            </div>
            <div className={styles.main}>
                <div className={styles.text}>앨범</div>
                <ul>
                    <li>
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
                    </li>
                </ul>
            </div>
        </div>
    )
}

export default AlbumPage
