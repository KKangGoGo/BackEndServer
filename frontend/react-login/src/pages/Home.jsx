import React, {useEffect} from 'react'
import {useDispatch, useSelector} from 'react-redux'
import UserRoute from '../components/UserRoute'
import {logoutInitial} from '../_actions/actions'

const Home = () => {
    const {currentUser, loading} = useSelector(state => state.user)
    const dispatch = useDispatch()

    const handleAuth = () => {
        if (currentUser) {
            dispatch(logoutInitial())
        }
    }

    return (
        <div style={{display: 'flex', justifyContent: 'center', alignItems: 'center', flexDirection: 'column'}}>
            <h2 style={{marginTop: '50px'}}>HOME</h2>
            <br />
            <button className="btn btn-danger" onClick={handleAuth}>
                Logout
            </button>
            <UserRoute />
        </div>
    )
}

export default Home
