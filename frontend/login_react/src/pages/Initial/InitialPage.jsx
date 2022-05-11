import React from 'react'
import {useNavigate} from 'react-router-dom'

const InitialPage = () => {
    const navigate = useNavigate()
    return (
        <div>
            <h1>초기화면 예정</h1>
            <button
                onClick={() => {
                    navigate('/loginRegister')
                }}
            >
                sign in/up
            </button>
        </div>
    )
}

export default InitialPage
