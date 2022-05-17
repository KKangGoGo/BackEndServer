import axios from 'axios'
import * as types from './types'

export const createAlbum = async (dataToSubmit, token) => {
    const request = await axios({
        method: 'post',
        url: '/api/user/album/create',
        data: dataToSubmit,
        headers: {'Content-Type': 'application/json', access_token: token},
    }).then(res => {
        return res.data
    })

    return {
        type: types.ALBUM_CREATE,
        payload: request,
    }
}

export const getAlbums = async token => {
    const request = await axios({
        method: 'get',
        url: '/api/user/album-list',
        headers: {'Content-Type': 'application/json', access_token: token},
    }).then(res => {
        return res.data
    })

    return {
        type: types.ALBUMS_GET,
        payload: request,
    }
}

export const getImages = async (id, token) => {
    const request = await axios({
        method: 'get',
        url: `/api/user/album/images?albumId=${id}`,
        headers: {'Content-Type': 'application/json', access_token: token},
    }).then(res => {
        return res.data
    })

    return {
        type: types.IMAGES_GET,
        payload: request,
    }
}

export const createImage = async (dataToSubmit, albumId, token) => {
    const request = await axios({
        method: 'post',
        url: `/api/user/album/images/${albumId}`,
        data: dataToSubmit,
        headers: {'Content-Type': 'multipart/form-data', access_token: token},
    }).then(res => {
        return res.data
    })

    return {
        type: types.IMAGE_CREATE,
        payload: request,
    }
}
