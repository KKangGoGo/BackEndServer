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
        type: types.CREATE_ALBUM,
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
        type: types.GET_ALBUMS,
        payload: request,
    }
}
