import React, {useEffect} from 'react'

import {useDispatch} from 'react-redux'
import {useNavigate} from 'react-router-dom'
import {authUser} from '../../_actions/userAction.jsx'

function auth(SpecificComponent, option, adminRoute = null) {
    // null 아무나 출입 가능 페이지
    // true 로그인한 유저만 출입 가능
    // false 로그인한 유저는 출입 불강
    function AuthenticationCheck(props) {
        const Atoken = localStorage.getItem('access-token')
        const Rtoken = localStorage.getItem('refresh-token')
        const navigate = useNavigate()
        const dispatch = useDispatch()

        useEffect(() => {
            if (Atoken) {
                dispatch(authUser(Atoken, Rtoken)).then(res => {
                    if (res) {
                        if (option === false) {
                            navigate('/')
                        }
                    }
                })
            } else {
                if (option) {
                    navigate('/loginRegister')
                }
            }
        }, [Atoken, Rtoken, navigate, dispatch])
        return <SpecificComponent />
    }
    return AuthenticationCheck
}

export default auth
