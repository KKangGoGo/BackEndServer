import * as types from '../_actions/actionTypes'

const initial = {
    loading: false,
    currentUser: null,
    error: null,
}

const userReducer = (state = initial, action) => {
    switch (action.type) {
        case types.LOGIN_START:
        case types.REGISTER_START:
        case types.LOGOUT_START:
            return {
                ...state,
                loading: true,
            }

        case types.LOGOUT_SUCCESS:
            return {
                ...state,
                currentUser: null,
            }

        case types.LOGIN_SUCCESS:
        case types.REGISTER_SUCCESS:
            return {
                ...state,
                currentUser: action.payload,
                loading: false,
            }
        case types.LOGIN_FAIL:
        case types.REGISTER_FAIL:
        case types.LOGOUT_FAIL:
            return {
                ...state,
                loading: false,
                error: action.payload,
            }

        case types.SET_USER:
            return {
                ...state,
                loading: false,
                currentUser: action.payload,
            }

        default:
            return state
    }
}

export default userReducer
