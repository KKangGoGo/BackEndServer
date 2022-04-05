import axios from 'axios'
import * as types from './types'

export const loginUser = dataToSubmit => {
    return function (dispatch) {
        axios.post('/api/users/login', dataToSubmit).then(res => {
            dispatch({
                type: types.LOGIN_USER,
                payload: res.data,
            })
        })
    }
}

export const logoutUser = () => {
    return function (dispatch) {
        axios.get('/api/users/logout').then(res => {
            dispatch({
                type: types.LOGOUT_USER,
            })
        })
    }
}

export const registerUser = dataToSubmit => {
    return function (dispatch) {
        axios.post('/api/users/register', dataToSubmit).then(res => {
            dispatch({
                type: types.REGISTER_USER,
                payload: res.data,
            })
        })
    }
}
