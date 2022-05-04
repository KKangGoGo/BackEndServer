import React from 'react'
import * as types from '../_actions/types'

function userReducer(state = {}, action) {
    switch (action.type) {
        case types.LOGIN_USER:
            return {
                ...state,
                loginSuccess: action.payload,
            }

        case types.LOGOUT_USER:
            return {
                ...state,
                userData: action.payload,
            }

        case types.REGISTER_USER:
            return {
                ...state,
                registerSuccess: action.payload,
            }

        case types.AUTH_USER:
            return {
                ...state,
                userData: action.payload,
            }

        default:
            return state
    }
}

export default userReducer
