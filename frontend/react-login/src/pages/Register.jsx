import './Register.css'
import React, {useState, useEffect} from 'react'
import {useDispatch, useSelector} from 'react-redux'
import {useNavigate, Link} from 'react-router-dom'
import {registerInitial} from '../_actions/actions'

const Register = () => {
    const [state, setState] = useState({
        displayName: '',
        email: '',
        password: '',
        passwordConfirm: '',
    })

    const {currentUser} = useSelector(state => state.user)

    const navigate = useNavigate()
    const dispatch = useDispatch()

    const {email, password, displayName, passwordConfirm} = state

    const handleSubmit = e => {
        e.preventDefault()
        if (password !== passwordConfirm) return
        dispatch(registerInitial(email, password, displayName))
        setState({email: '', displayName: '', password: '', passwordConfirm: ''})
        navigate('/login')
    }
    const handleChange = e => {
        let {name, value} = e.target
        setState({...state, [name]: value})
    }

    useEffect(() => {
        // if (currentUser) navigate('/login')
    }, [currentUser, navigate])

    return (
        <div>
            <div id="register-form">
                <form action="" className="form-signup" onSubmit={handleSubmit}>
                    <h1 className="h3 mb-3 font-weight-normal" style={{textAlign: 'center'}}>
                        Sign up
                    </h1>
                    <input
                        type="text"
                        id="displayName"
                        className="form-control"
                        placeholder="Full Name"
                        name="displayName"
                        onChange={handleChange}
                        value={displayName}
                        required
                    />
                    <input
                        type="email"
                        id="user-email"
                        className="form-control"
                        placeholder="Email Address"
                        name="email"
                        onChange={handleChange}
                        value={email}
                        required
                    />
                    <input
                        type="password"
                        id="inputPassword"
                        className="form-control"
                        placeholder="Password"
                        name="password"
                        onChange={handleChange}
                        value={password}
                        required
                    />
                    <input
                        type="password"
                        id="inputRePassword"
                        className="form-control"
                        placeholder="Repeat Password"
                        name="passwordConfirm"
                        onChange={handleChange}
                        value={passwordConfirm}
                        required
                    />
                    <button className="btn btn-primary btn-block" type="submit">
                        <i className="fas fa-user-plus">Sign Up</i>
                    </button>
                    <hr />
                    <Link to="/login">
                        <i className="fas fa-angle-left"></i> Back
                    </Link>
                </form>
            </div>
        </div>
    )
}

export default Register
