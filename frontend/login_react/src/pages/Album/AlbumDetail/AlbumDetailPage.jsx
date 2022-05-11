import React, {useEffect, useState} from 'react'
import {useDispatch, useSelector} from 'react-redux'
import {useParams} from 'react-router-dom'
import {getImages} from '../../../_actions/albumAction'
import {HiOutlineSearch, HiOutlineUpload} from 'react-icons/hi'

import styles from './AlbumDetailPage.module.css'
import ImageUploadModal from '../ImageUpload/ImageUploadModal'

const AlbumDetailPage = () => {
    const [isOpen, setIsOpen] = useState(false)
    const modalToggle = () => setIsOpen(!isOpen)

    const dispatch = useDispatch()
    const {albumId} = useParams()

    const [Images, setImages] = useState([])

    useEffect(() => {
        const token = localStorage.getItem('login-token')
        dispatch(getImages(albumId, token))
            .then(res => {
                console.log(res.payload.images)
                setImages(res.payload.images)
            })
            .catch(e => console.log(e.message))
    }, [albumId, dispatch])

    const RenderImages = Images.map((image, index) => {
        return (
            <li key={index}>
                <img src={`${image}`} alt="" />
            </li>
        )
    })

    return (
        <div className={styles.container}>
            <div>
                <h1>앨범 &#62; 앨범이름</h1>
                <button onClick={modalToggle}>
                    <HiOutlineUpload />
                    사진 추가
                </button>
                <ImageUploadModal isOpen={isOpen} onRequestClose={modalToggle} onCancel={modalToggle} albumId={albumId} />
            </div>

            <ul>{RenderImages} </ul>
        </div>
    )
}

export default AlbumDetailPage
