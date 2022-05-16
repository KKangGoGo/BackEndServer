import * as types from '../_actions/types'

function albumReducer(state = {}, action) {
    switch (action.type) {
        case types.ALBUM_CREATE:
            return {
                ...state,
                albumData: action.payload,
            }

        case types.ALBUMS_GET:
            return {
                ...state,
                albums: action.payload,
            }

        case types.IMAGES_GET:
            return {
                ...state,
                images: action.payload,
            }

        case types.IMAGE_CREATE:
            return {
                ...state,
                image: action.payload,
            }

        default:
            return state
    }
}

export default albumReducer
