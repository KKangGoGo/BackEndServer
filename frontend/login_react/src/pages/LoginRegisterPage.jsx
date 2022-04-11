import React, {useState} from 'react'
import styled from 'styled-components'
import LoginPage from './LoginPage'
import RegisterPage from './RegisterPage'

const Container = styled.div`
    position: relative;
    width: 800px;
    height: 500px;
    margin: 100px auto;
`

const Banner = styled.div`
    position: absolute;
    display: flex;
    justify-content: center;
    align-items: center;
    top: 40px;
    width: 100%;
    height: 420px;
    background: rgba(255, 255, 255, 0.2);
    box-shadow: 0 5px 80px 20px rgba(0, 0, 0, 0.15);
    border-radius: 20px;
    background: #dddcee;
    &.active {
        background: #6969b4;
    }
`

const Box = styled.div`
    position: relative;
    width: 50%;
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
`

const BoxH2 = styled.div`
    color: #fff;
    font-size: 1.7rem;
    font-weight: 500;
    margin-bottom: 10px;
`

const BoxBtn = styled.div`
    border: none;
    background: #fff;
    border-radius: 10px;
    padding: 5px 10px;
    cursor: pointer;
`

const FormBox = styled.div`
    position: absolute;
    top: 0;
    left: 0;
    width: 50%;
    height: 100%;
    background: #fff;
    z-index: 1;
    display: flex;
    justify-content: center;
    align-items: center;
    box-shadow: 0 5px 45px rgba(0, 0, 0, 0.25);
    border-radius: 20px;
    transition: 0.5s ease-in-out;
    overflow: hidden;
    &.active {
        left: 50%;
    }
`

const SigninForm = styled.div`
    position: absolute;
    left: 0;
    width: 100%;
    transition: 0.5s;
    transition-delay: 0.25s;
    &.active {
        left: -100%;
    }
`

const SignupForm = styled.div`
    position: absolute;
    left: 0;
    width: 100%;
    transition: 0.5s;
    left: 100%;
    transition-delay: 0.25s;
    &.active {
        left: 0;
        transition-delay: 0.25s;
    }
`

const LoginRegisterPage = () => {
    const [isOn, setIsOn] = useState(true)

    const handlerActive = () => {
        setIsOn(!isOn)
    }

    return (
        <Container>
            <Banner className={isOn ? ' ' : 'active'}>
                <Box>
                    <BoxH2>Already Have an Account ?</BoxH2>
                    <BoxBtn onClick={handlerActive}>Sign in</BoxBtn>
                </Box>
                <Box>
                    <BoxH2>Don't Have an Account ?</BoxH2>
                    <BoxBtn onClick={handlerActive}>Sign up</BoxBtn>
                </Box>
            </Banner>

            <FormBox className={isOn ? ' ' : 'active'}>
                <SigninForm className={isOn ? ' ' : 'active'}>
                    <LoginPage />
                </SigninForm>
                <SignupForm className={isOn ? ' ' : 'active'}>
                    <RegisterPage />
                </SignupForm>
            </FormBox>
        </Container>
    )
}

export default LoginRegisterPage
