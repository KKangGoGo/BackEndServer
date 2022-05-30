import React, {useState} from 'react'
import Modal from 'react-modal'
import {useDispatch, useSelector} from 'react-redux'
import {createAlbum} from '../../../_actions/albumAction'

import {BiImageAdd} from 'react-icons/bi'

import styles from './modal.module.css'

const AlbumUploadModal = ({isOpen, onRequestClose, onSubmit, onCancel}) => {
    const [state, setState] = useState({
        title: '',
    })

    const dispatch = useDispatch()

    const {title} = state

    const handleSubmit = e => {
        e.preventDefault()
        const Atoken = localStorage.getItem('access-token')
        const Rtoken = localStorage.getItem('refresh-token')

        dispatch(createAlbum(state, Atoken, Rtoken))
            .then(res => {
                setState({...state, title: ''})
                onSubmit()
                window.location.replace(`/user/album`)
            })
            .catch(e => console.log(e.message))
    }

    const handleInputChange = e => {
        let {name, value} = e.target
        setState({...state, [name]: value})
    }

    const handleCancel = () => {
        onCancel()
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
                    top: '20%',
                    left: '40%',
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
            <form className={styles.form} onSubmit={handleSubmit}>
                <legend>앨범 추가하기</legend>
                <label>
                    <div>
                        <BiImageAdd />
                    </div>
                    <span>앨범 이름</span>
                    <input type="text" name="title" value={title} onChange={handleInputChange} autoFocus />
                </label>
                <div>
                    <button type="submit">확인</button>
                    <button onClick={handleCancel}>취소</button>
                </div>
            </form>
        </Modal>
    )
}

export default AlbumUploadModal
