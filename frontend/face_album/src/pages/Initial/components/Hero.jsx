import React from 'react'
import styles from './Hero.module.css'

const Hero = ({imageSrc}) => {
    return (
        <div className={styles.hero}>
            <img src={imageSrc} alt="" className={styles.hero__image} />
            {/* <h1 className={styles.hero__title}>Share Face Image.</h1> */}
        </div>
    )
}

export default Hero
