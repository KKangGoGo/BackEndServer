import * as types from '../_actions/types'

function albumReducer(state = {}, action) {
    switch (action.type) {
        case types.CREATE_ALBUM:
            return {
                ...state,
                albumData: action.payload,
            }

        case types.GET_ALBUMS:
            return {
                ...state,
                albums: action.payload,
            }

        default:
            return state
    }
}

export default albumReducer
