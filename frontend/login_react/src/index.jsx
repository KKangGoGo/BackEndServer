import React from 'react'
import ReactDOM from 'react-dom/client'
import './index.css'
import App from './App'
import {BrowserRouter} from 'react-router-dom'

import {createStore, applyMiddleware} from 'redux'
import logger from 'redux-logger'
import ReduxThunk from 'redux-thunk'
import promiseMiddleware from 'redux-promise'
import rootReducer from './_reducers/rootReducer'
import {Provider} from 'react-redux'

const middlewares = [ReduxThunk]
if (process.env.NODE_ENV === 'development') {
    middlewares.push(logger)
}

// const store = createStore(rootReducer, applyMiddleware(...middlewares))
const store = applyMiddleware(promiseMiddleware, ...middlewares)(createStore)

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
