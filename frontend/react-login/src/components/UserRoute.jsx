import React from 'react'
import {Route} from 'react-router-dom'
import {useSelector} from 'react-redux'
import LoadingToRedirect from './LoadingToRedirect'
import Home from '../pages/Home'

const UserRoute = ({children, ...rest}) => {
    const {currentUser} = useSelector(state => state.user)

    return currentUser ? <div>LOGIN</div> : <LoadingToRedirect />
}

export default UserRoute
