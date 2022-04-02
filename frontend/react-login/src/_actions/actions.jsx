import * as types from './actionTypes'
import {auth} from '../firebase.js'

export const registerInitial = (email, password, displayName) => {
    return function (dispatch) {
        dispatch({
            type: types.REGISTER_START,
        })
        auth.createUserWithEmailAndPassword(email, password)
            .then(({user}) => {
                user.updateProfile({displayName})
                console.log(user)
                dispatch({
                    type: types.REGISTER_SUCCESS,
                    payload: user,
                })
            })
            .catch(error => {
                dispatch({
                    type: types.REGISTER_FAIL,
                    payload: error.message,
                })
            })
    }
}

export const loginInitial = (email, password) => {
    return function (dispatch) {
        dispatch({
            type: types.LOGIN_START,
        })
        auth.signInWithEmailAndPassword(email, password)
            .then(({user}) => {
                dispatch({
                    type: types.LOGIN_SUCCESS,
                    payload: user,
                })
            })
            .catch(error => {
                dispatch({
                    type: types.LOGIN_FAIL,
                    payload: error.message,
                })
            })
    }
}

export const logoutInitial = () => {
    return function (dispatch) {
        dispatch({
            type: types.LOGOUT_START,
        })
        auth.signOut()
            .then(res => {
                dispatch({
                    type: types.LOGOUT_SUCCESS,
                })
            })
            .catch(error => {
                dispatch({
                    type: types.LOGOUT_FAIL,
                    payload: error.message,
                })
            })
    }
}

export const setUser = user => ({
    type: types.SET_USER,
    payload: user,
})
