import React, {useEffect} from 'react'
import Button from '@mui/material/Button'
import ButtonGroup from '@mui/material/ButtonGroup'
import Card from '@mui/material/Card'
import CardContent from '@mui/material/CardContent'
import CardMedia from '@mui/material/CardMedia'
import Typography from '@mui/material/Typography'
import {CardActionArea} from '@mui/material'

import {useNavigate} from 'react-router-dom'
import {useDispatch, useSelector} from 'react-redux'
import {deleteUser, loadUsers} from '../redux/actions'

function createData(name, calories, fat, carbs, protein) {
    return {name, calories, fat, carbs, protein}
}

const rows = [
    createData('Frozen yoghurt', 159, 6.0, 24, 4.0),
    createData('Ice cream sandwich', 237, 9.0, 37, 4.3),
    createData('Eclair', 262, 16.0, 24, 6.0),
    createData('Cupcake', 305, 3.7, 67, 4.3),
    createData('Gingerbread', 356, 16.0, 49, 3.9),
]

const Home = () => {
    let dispatch = useDispatch()
    let navigate = useNavigate()

    const handelDelete = id => {
        if (window.confirm('Are you sure wanted to delete the user ?')) {
            dispatch(deleteUser(id))
        }
    }

    const {users} = useSelector(state => state.users)

    useEffect(() => {
        dispatch(loadUsers())
    }, [])

    return (
        <>
            <div style={{marginBottom: '30px'}}>
                <Button variant="contained" onClick={() => navigate('/addUser')}>
                    Add User Photo
                </Button>
            </div>
            <div style={{display: 'flex', flexWrap: 'wrap'}}>
                {users &&
                    users.map(user => (
                        <Card key={user.id} sx={{maxWidth: 345}}>
                            <CardActionArea>
                                <CardMedia component="img" height="140" image={user.image} alt="green iguana" />
                                <CardContent>
                                    <Typography gutterBottom variant="h5" component="div">
                                        {user.name}
                                    </Typography>
                                    <Typography variant="body2" color="text.secondary">
                                        {user.email}
                                    </Typography>
                                    {user.address.street ? (
                                        <Typography variant="body2" color="text.secondary">
                                            {user.address.street}
                                        </Typography>
                                    ) : (
                                        <Typography variant="body2" color="text.secondary">
                                            {user.address}
                                        </Typography>
                                    )}

                                    <Typography variant="body2" color="text.secondary">
                                        {user.phone}
                                    </Typography>
                                    <Typography style={{marginTop: '10px'}}>
                                        <ButtonGroup disableElevation variant="contained">
                                            <Button
                                                style={{backgroundColor: 'red', height: '20px'}}
                                                color="secondary"
                                                onClick={() => handelDelete(user.id)}
                                            >
                                                Delete
                                            </Button>
                                            <Button style={{height: '20px'}} onClick={() => navigate(`/editUser/${user.id}`)}>
                                                Edit
                                            </Button>
                                        </ButtonGroup>
                                    </Typography>
                                </CardContent>
                            </CardActionArea>
                        </Card>
                    ))}
            </div>
        </>
    )
}

export default Home
