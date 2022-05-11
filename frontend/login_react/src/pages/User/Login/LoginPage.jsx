import React, {useState} from 'react'
import {useDispatch, useSelector} from 'react-redux'
import {loginUser} from '../../../_actions/userAction'
import {useNavigate} from 'react-router-dom'

import {Formik} from 'formik'
import * as Yup from 'yup'

import styles from './LoginPage.module.css'

function LoginPage(props) {
    const [formErrorMessage, setFormErrorMessage] = useState('')
    const navigate = useNavigate()
    const dispatch = useDispatch()

    return (
        <Formik
            initialValues={{
                username: '',
                password: '',
            }}
            validationSchema={Yup.object().shape({
                username: Yup.string().min(4, 'Password must be at least 4 characters').required('username is required'),
                password: Yup.string().min(4, 'Password must be at least 4 characters').required('Password is required'),
            })}
            onSubmit={(values, {setSubmitting}) => {
                setTimeout(() => {
                    let dataToSubmit = {
                        username: values.username,
                        password: values.password,
                    }

                    console.log(dataToSubmit)
                    dispatch(loginUser(dataToSubmit))
                        .then(res => {
                            navigate('/user/main')
                        })
                        .catch(e => {
                            setFormErrorMessage('Check out your Account or Password again')
                            setTimeout(() => {
                                setFormErrorMessage('')
                            }, 3000)
                        })
                    setSubmitting(false)
                }, 500)
            }}
        >
            {props => {
                const {values, touched, errors, isSubmitting, handleChange, handleSubmit} = props
                return (
                    <div className={styles.login}>
                        <div>SIGN IN</div>
                        <form
                            onSubmit={handleSubmit}
                            style={{display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center'}}
                        >
                            <input
                                className={styles.input}
                                placeholder="Enter your username"
                                id="username"
                                name="username"
                                value={values.username}
                                onChange={handleChange}
                            />
                            {errors.username && touched.username && <div className={styles.feedback}>{errors.username}</div>}
                            <input
                                className={styles.input}
                                id="password"
                                placeholder="Enter your password"
                                type="password"
                                value={values.password}
                                onChange={handleChange}
                            />
                            {errors.password && touched.password && <div className={styles.feedback}>{errors.password}</div>}
                            {formErrorMessage && (
                                <label>
                                    <p className={styles.errMessage}>{formErrorMessage}</p>
                                </label>
                            )}
                            <button type="submit" className="login_button" disabled={isSubmitting} onSubmit={handleSubmit}>
                                LOGIN
                            </button>
                        </form>
                    </div>
                )
            }}
        </Formik>
    )
}

export default LoginPage
