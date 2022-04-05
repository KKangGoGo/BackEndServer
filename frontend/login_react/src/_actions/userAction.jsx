import axios from 'axios'
import * as types from './types'

export const loginUser = async dataToSubmit => {
    const request = await axios.post('/api/users/login', dataToSubmit).then(res => res.data)

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
    const request = await axios.post('/api/users/register', dataToSubmit).then(res => res.data)

    return {
        type: types.REGISTER_USER,
        payload: request,
    }
}
