import React, {useState} from 'react'
import LoginPage from './Login/LoginPage'
import RegisterPage from './Register/RegisterPage'
import Auth from '../Hoc/auth'

import styles from './LoginRegisterPage.module.css'

const LoginRegisterPage = () => {
    const [isOn, setIsOn] = useState(true)

    const handlerActive = () => {
        setIsOn(!isOn)
    }

    return (
        <div className={styles.container}>
            <div className={isOn ? `${styles.banner}` : `${styles.banner} ${styles.active}`}>
                <div className={styles.box}>
                    <h2>Already Have an Account ?</h2>
                    <button onClick={handlerActive}>Sign in</button>
                </div>
                <div className={styles.box}>
                    <h2>Don't Have an Account ?</h2>
                    <button onClick={handlerActive}>Sign up</button>
                </div>
            </div>

            <div className={isOn ? `${styles.form}` : `${styles.form} ${styles.active}`}>
                <div className={isOn ? `${styles.signin}` : `${styles.signin} ${styles.active}`}>
                    <LoginPage />
                </div>
                <div className={isOn ? `${styles.signup}` : `${styles.signup} ${styles.active}`}>
                    <RegisterPage />
                </div>
            </div>
        </div>
    )
}

export default Auth(LoginRegisterPage, false)
