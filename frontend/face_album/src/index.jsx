import React from 'react'
import ReactDOM from 'react-dom/client'
import './reset.css'
import App from './App'
import {BrowserRouter} from 'react-router-dom'

import {createStore, applyMiddleware} from 'redux'
import logger from 'redux-logger'
import ReduxThunk from 'redux-thunk'
import promiseMiddleware from 'redux-promise'
import rootReducer from './_reducers/rootReducer'
import {Provider} from 'react-redux'
import axios from 'axios'
import ReactModal from 'react-modal'

const middlewares = [ReduxThunk]
if (process.env.NODE_ENV === 'development') {
    middlewares.push(logger)
}

ReactModal.setAppElement('#root')

// const store = createStore(rootReducer, applyMiddleware(...middlewares))
const store = applyMiddleware(promiseMiddleware, ...middlewares)(createStore)
axios.defaults.withCredentials = true

const root = ReactDOM.createRoot(document.getElementById('root'))

root.render(
    <React.StrictMode>
        <BrowserRouter>
            <Provider store={store(rootReducer, window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__())}>
                <App />
            </Provider>
        </BrowserRouter>
    </React.StrictMode>,
)
