import React from 'react'
import {useDispatch, useSelector} from 'react-redux'
import Auth from '../Hoc/auth'

const MainPage = () => {
    const user = useSelector(state => state.user)

    return (
        <div>
            <h2>home</h2>
        </div>
    )
}

export default Auth(MainPage, null)
