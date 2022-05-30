import React from 'react'
import Modal from 'react-modal'
import {useDispatch} from 'react-redux'

import {BiImageAdd} from 'react-icons/bi'

import styles from './ImageModal.module.css'
import {createImage} from '../../../_actions/albumAction'
import {useNavigate} from 'react-router-dom'

const ImageUploadModal = ({isOpen, onRequestClose, onCancel, albumId}) => {
    const dispatch = useDispatch()

    const handleCancel = () => {
        onCancel()
    }

    const onSubmitHandler = async e => {
        e.preventDefault()
        const Atoken = localStorage.getItem('access-token')
        const Rtoken = localStorage.getItem('refresh-token')

        let formData = new FormData()

        let files = e.target.image.files

        for (let i = 0; i < e.target.image.files.length; i++) {
            formData.append('images', files[i])
        }

        dispatch(createImage(formData, albumId, Atoken, Rtoken)).then(res => {
            console.log(res)
            onCancel()
            window.location.replace(`/user/album/${albumId}`)
        })
    }

    return (
        <Modal
            isOpen={isOpen}
            onRequestClose={onRequestClose}
            style={{
                overlay: {
                    position: 'fixed',
                    top: 0,
                    left: 0,
                    right: 0,
                    bottom: 0,
                    background: 'none',
                },
                content: {
                    position: 'absolute',
                    top: '15%',
                    left: '50%',
                    border: '1px solid #ccc',
                    background: '#fff',
                    overflow: 'auto',
                    WebkitOverflowScrolling: 'touch',
                    borderRadius: '10px',
                    outline: 'none',
                    padding: '20px',
                    width: '400px',
                    height: '200px',
                },
            }}
        >
            <form className={styles.form} onSubmit={onSubmitHandler}>
                <legend>사진 추가하기</legend>
                <label>
                    <div>
                        <BiImageAdd />
                    </div>
                    <label htmlFor="">Image</label>
                    <input type="file" name="image" multiple="multiple" />
                </label>
                <div>
                    <button type="submit">확인</button>
                    <button onClick={handleCancel}>취소</button>
                </div>
            </form>
        </Modal>
    )
}

export default ImageUploadModal
