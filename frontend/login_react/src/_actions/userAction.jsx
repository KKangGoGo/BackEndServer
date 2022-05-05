import axios from 'axios'
import * as types from './types'

export const loginUser = async dataToSubmit => {
    const request = await axios.post('/api/login', dataToSubmit).then(res => {
        console.log(res)

        localStorage.setItem('login-token', res.headers.access_token)

        return res.data
    })

    return {
        type: types.LOGIN_USER,
        payload: request,
    }
}

export const logoutUser = async () => {
    await axios.get('/api/users/logout').then(res => {
        localStorage.removeItem('login-token')
        return res.data
    })
    return {
        type: types.LOGOUT_USER,
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

export const authUser = async token => {
    const request = await axios({
        method: 'get',
        url: '/api/user/auth',
        headers: {'Content-Type': 'application/json', access_token: token},
    }).then(res => {
        return res.data
    })

    return {
        type: types.AUTH_USER,
        payload: request,
    }
}
