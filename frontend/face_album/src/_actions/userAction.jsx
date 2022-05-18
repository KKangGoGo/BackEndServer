import axios from 'axios'
import * as types from './types'

export const loginUser = async dataToSubmit => {
    const request = await axios.post('/api/login', dataToSubmit).then(res => {
        localStorage.setItem('access-token', res.headers.access_token)
        localStorage.setItem('refresh-token', res.headers.refresh_token)

        return res.data
    })

    return {
        type: types.LOGIN_USER,
        payload: request,
    }
}

export const logoutUser = async (Atoken, Rtoken) => {
    const request = await axios({
        method: 'get',
        url: '/api/logout',
        headers: {'Content-Type': 'application/json', access_token: Atoken, refresh_token: Rtoken},
    }).then(res => {
        return res.data
    })
    return {
        type: types.LOGOUT_USER,
        payload: request,
    }
}

export const registerUser = async dataToSubmit => {
    const request = await axios({
        method: 'post',
        url: '/api/signup',
        data: dataToSubmit,
        headers: {'Content-Type': 'multipart/form-data'},
    }).then(res => {
        return res.data
    })

    return {
        type: types.REGISTER_USER,
        payload: request,
    }
}

export const authUser = async (Atoken, Rtoken) => {
    const request = await axios({
        method: 'get',
        url: '/api/user/auth',
        headers: {'Content-Type': 'application/json', access_token: Atoken, refresh_token: Rtoken},
    }).then(res => {
        return res.data
    })

    return {
        type: types.AUTH_USER,
        payload: request,
    }
}
