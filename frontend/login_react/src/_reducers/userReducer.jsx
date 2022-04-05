import React from 'react'
import * as types from '../_actions/types'

function userReducer(state = {}, action) {
    switch (action.type) {
        case types.LOGIN_USER:
            return {
                ...state,
                Data: action.payload,
            }

        case types.LOGOUT_USER:
            return {
                ...state,
            }

        case types.REGISTER_USER:
            return {
                ...state,
                Data: action.payload,
            }
        default:
            return state
    }
}

export default userReducer
