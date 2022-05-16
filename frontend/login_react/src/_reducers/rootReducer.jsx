import {combineReducers} from 'redux'
import userReducer from './userReducer'
import albumReducer from './albumReducer'

const rootReducer = combineReducers({
    user: userReducer,
    album: albumReducer,
})

export default rootReducer
