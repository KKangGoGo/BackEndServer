import axios from 'axios'
import * as types from './types'

export const loginUser = async dataToSubmit => {
    const request = await axios.post('/api/login', dataToSubmit).then(res => {
        console.log(res)
        return res.data
    })

    return {
        type: types.LOGIN_USER,
        payload: request,
    }
}

export const logoutUser = async () => {
    await axios.get('/api/users/logout').then(res => res.data)
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

    // const request = await axios.post('/api/signup', dataToSubmit).then(res => {
    //     return res.data
    // })

    return {
        type: types.REGISTER_USER,
        payload: request,
    }
}
