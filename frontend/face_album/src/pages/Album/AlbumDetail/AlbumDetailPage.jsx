import React, {useEffect, useState} from 'react'
import {useDispatch, useSelector} from 'react-redux'
import {useParams} from 'react-router-dom'
import {getImages} from '../../../_actions/albumAction'
import {HiOutlineSearch, HiOutlineUpload} from 'react-icons/hi'
import {AiOutlineShareAlt} from 'react-icons/ai'

import styles from './AlbumDetailPage.module.css'
import ImageUploadModal from '../ImageUpload/ImageUploadModal'
import axios from 'axios'

const AlbumDetailPage = () => {
    const [isOpen, setIsOpen] = useState(false)
    const modalToggle = () => setIsOpen(!isOpen)

    const dispatch = useDispatch()
    const {albumId} = useParams()

    const [Images, setImages] = useState([])

    const shareImg = () => {
        axios.post(`/api/user/album/${albumId}/share`).then(res => {
            console.log(res)
            alert('공유되었습니다.')
        })
    }

    useEffect(() => {
        const Atoken = localStorage.getItem('access-token')
        const Rtoken = localStorage.getItem('refresh-token')
        dispatch(getImages(albumId, Atoken, Rtoken))
            .then(res => {
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
                <button onClick={shareImg}>
                    <AiOutlineShareAlt />
                    사진 공유
                </button>
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
