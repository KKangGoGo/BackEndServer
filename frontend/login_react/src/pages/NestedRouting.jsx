import React from 'react'
import {Route, Routes} from 'react-router-dom'
import AlbumPage from './Album/AlbumPage'
import MainPage from './Main/MainPage'
import Navbar from './Nav/Navbar'

import styles from './NestedRouting.module.css'

const NestedRouting = () => {
    return (
        <div className={styles.container}>
            <div className={styles.letSide}>
                <Navbar />
            </div>
            <div className={styles.rightSide}>
                <Routes>
                    <Route path="main" element={<MainPage />} />
                    <Route path="album" element={<AlbumPage />} />
                </Routes>
            </div>
        </div>
    )
}

export default NestedRouting
