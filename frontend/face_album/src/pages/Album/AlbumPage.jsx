import React, {useEffect, useState} from 'react'

import styles from './AlbumPage.module.css'

import {HiOutlineSearch, HiOutlineUpload} from 'react-icons/hi'
import {IoAlbumsOutline} from 'react-icons/io5'
import AlbumUploadModal from './AlbumUpload/AlbumUploadModal'
import {useDispatch} from 'react-redux'
import {getAlbums} from '../../_actions/albumAction'

const AlbumPage = () => {
    const [isOpen, setIsOpen] = useState(false)
    const modalToggle = () => setIsOpen(!isOpen)

    const [Albums, setAlbums] = useState([])

    const dispatch = useDispatch()

    useEffect(() => {
        const Atoken = localStorage.getItem('access-token')
        const Rtoken = localStorage.getItem('refresh-token')
        dispatch(getAlbums(Atoken, Rtoken))
            .then(res => {
                console.log(res)
                setAlbums(res.payload.albumlist)
            })
            .catch(e => console.log(e.message))
    }, [dispatch])

    const RenderShortcut = Albums.map((album, index) => {
        return (
            <a key={index} href={`album/${album.albumId}`}>
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
                <ul>{RenderAlbum}</ul>
            </div>
        </div>
    )
}

export default AlbumPage
