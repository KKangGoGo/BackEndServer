import React, {useState} from 'react'
import Modal from 'react-modal'
import {useDispatch, useSelector} from 'react-redux'

import {BiImageAdd} from 'react-icons/bi'

import styles from './ImageModal.module.css'
import {createImage} from '../../../_actions/albumAction'

const ImageUploadModal = ({isOpen, onRequestClose, onCancel, albumId}) => {
    const dispatch = useDispatch()

    const handleCancel = () => {
        onCancel()
    }

    const onSubmitHandler = async e => {
        e.preventDefault()
        const token = localStorage.getItem('login-token')

        let formData = new FormData()

        let files = e.target.image.files
        formData.append('images', files[0])

        dispatch(createImage(formData, albumId, token)).then(res => {
            console.log(res)
            onCancel()
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
