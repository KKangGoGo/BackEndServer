import React from 'react'
import Hero from './components/Hero'
import Navbar from './components/Navbar'
import Slider from './components/Slider'

import share from './assets/share.jpg'
import face from './assets/face.jpg'
import photo3 from './assets/photo3.jpg'
import share3 from './assets/share3.jpg'

const InitialPage = () => {
    const navbarLinks = [
        {url: 'user/home', title: 'Home'},
        {url: '/loginRegister', title: 'Sign'},
    ]

    return (
        <div className="App">
            <Navbar navbarLinks={navbarLinks} />
            <Hero imageSrc={share} />
            <Slider imageSrc={face} title={'사진의 얼굴을 인식해요'} subtitle={'저장된 사진의 얼굴을 인식하여 자동으로 분류해요'} />
            <Slider imageSrc={photo3} title={'사진을 저장해요'} subtitle={'당신의 추억이 담긴 사진을 저장해요'} flipped={true} />
            <Slider imageSrc={share3} title={'친구와 추억을 공유해요'} subtitle={'당신의 사진을 친구들과 함께 공유해요'} />
        </div>
    )
}

export default InitialPage
