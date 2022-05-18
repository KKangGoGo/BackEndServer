import React, {useState} from 'react'
import {useDispatch} from 'react-redux'
import {registerUser} from '../../../_actions/userAction'

import {Formik} from 'formik'
import * as Yup from 'yup'

import styles from './RegisterPage.module.css'

function RegisterPage(props) {
    const [formErrorMessage, setFormErrorMessage] = useState('')
    const [state, setState] = useState({
        username: '',
        password: '',
        email: '',
    })

    const {username, email, password} = state

    const [ConfirmPassword, setConfirmPassword] = useState('')

    const dispatch = useDispatch()

    const handleInputChange = e => {
        let {name, value} = e.target
        setState({...state, [name]: value})
    }

    const onConfirmPasswordHandler = e => {
        setConfirmPassword(e.currentTarget.value)
    }

    const onSubmitHandler = async e => {
        e.preventDefault()
        let formData = new FormData()

        let files = e.target.image.files
        formData.append('photo', files[0])
        formData.append('signupInfo', new Blob([JSON.stringify(state)], {type: 'application/json'}))

        if (password !== ConfirmPassword) {
            return alert('비밀번호가 일치하지 않습니다.')
        }

        try {
            dispatch(registerUser(formData)).then(res => {
                console.log(res)
                window.location.replace('/loginRegister')
            })
        } catch (error) {
            console.log(error)
        }
    }

    return (
        <>
            <div className={styles.text}>SIGN UP</div>
            <form
                className={styles.form}
                onSubmit={onSubmitHandler}
                style={{display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center', color: '#848484'}}
            >
                <label htmlFor="">Username</label>
                <input
                    className={styles.input}
                    type="text"
                    placeholder="username"
                    name="username"
                    value={username}
                    onChange={handleInputChange}
                />

                <label htmlFor="">Password</label>
                <input
                    className={styles.input}
                    type="password"
                    placeholder="password"
                    name="password"
                    value={password}
                    onChange={handleInputChange}
                />

                <label htmlFor="">ConfirmPassword</label>
                <input
                    className={styles.input}
                    type="password"
                    placeholder="confirm-password"
                    value={ConfirmPassword}
                    onChange={onConfirmPasswordHandler}
                />

                <label htmlFor="">Email</label>
                <input className={styles.input} type="text" placeholder="email" name="email" value={email} onChange={handleInputChange} />

                <label htmlFor="file">Image</label>
                <input className={`${styles.input} ${styles.file}`} type="file" name="image" multiple="multiple" />

                <button>SIGN UP</button>
            </form>
        </>
    )
}

export default RegisterPage
