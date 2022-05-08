import React from 'react'
import {useParams} from 'react-router-dom'

const AlbumDetailPage = () => {
    const {albumId} = useParams()
    console.log(albumId)

    return (
        <div>
            <h1>디테일 페이지</h1>
        </div>
    )
}

export default AlbumDetailPage
