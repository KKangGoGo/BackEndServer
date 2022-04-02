import './Login.css'
import React, {useState, useEffect} from 'react'
import {useDispatch, useSelector} from 'react-redux'
import {useNavigate, Link} from 'react-router-dom'
import {loginInitial} from '../_actions/actions'

const Login = () => {
    const [state, setState] = useState({
        email: '',
        password: '',
    })

    const navigate = useNavigate()
    const dispatch = useDispatch()
    const {currentUser} = useSelector(state => state.user)

    const {email, password} = state
    const handleGoogleSignIn = () => {}
    const handleFBSignIn = () => {}
    const handleSubmit = e => {
        e.preventDefault()
        if (!email || !password) return
        dispatch(loginInitial(email, password))
        setState({email: '', password: ''})
    }
    const handleChange = e => {
        let {name, value} = e.target
        setState({...state, [name]: value})
    }

    useEffect(() => {
        if (currentUser) navigate('/')
    }, [currentUser, navigate])

    return (
        <div>
            <div id="logreg-forms">
                <form action="" className="form-signin" onSubmit={handleSubmit}>
                    <h1 className="h3 mb-3 font-weight-normal" style={{textAlign: 'center'}}>
                        Sign in
                    </h1>
                    <div className="social-login">
                        <button className="btn google-btn social-btn" type="button" onClick={handleGoogleSignIn}>
                            <span>
                                <i className="fab fa-google-plus-g">Sign in with Google+</i>
                            </span>
                        </button>
                        <button className="btn facebook-btn social-btn" type="button" onClick={handleFBSignIn}>
                            <span>
                                <i className="fab fa-facebook-f">Sign in with Facebook</i>
                            </span>
                        </button>
                    </div>
                    <p style={{textAlign: 'center'}}>OR</p>
                    <input
                        type="email"
                        id="inputEmail"
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
                    <button className="btn btn-secondary btn-block" type="submit">
                        <i className="fas fa-sign-in-alt">Sign In</i>
                    </button>
                    <hr />
                    <p>Don't have an account</p>
                    <Link to="/register">
                        <button className="btn btn-primary btn-block" id="btn-signup" type="button">
                            <i className="fas fa-user-plus">Sign Up Account</i>
                        </button>
                    </Link>
                </form>
            </div>
        </div>
    )
}

export default Login
