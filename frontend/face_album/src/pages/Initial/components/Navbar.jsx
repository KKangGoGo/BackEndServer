import React, {useState} from 'react'
import styles from './Navbar.module.css'
import {FiMenu, FiX} from 'react-icons/fi'

const Navbar = ({navbarLinks}) => {
    const [menuClicked, setMenuClicked] = useState(false)

    const toggleMenuClick = () => {
        setMenuClicked(!menuClicked)
    }

    return (
        <nav className={styles.navbar}>
            <span className={styles.navbar__logo}>
                <a href="/"> FaceAlbum</a>
            </span>
            {menuClicked ? (
                <FiX size={25} className={styles.navbar__menu} onClick={toggleMenuClick} />
            ) : (
                <FiMenu size={25} className={styles.navbar__menu} onClick={toggleMenuClick} />
            )}
            <ul className={menuClicked ? `${styles.navbar__list} ${styles.navbar__list_active}` : `${styles.navbar__list}`}>
                {navbarLinks.map((item, index) => {
                    return (
                        <li className={styles.navbar__item} key={index}>
                            <a className={styles.navbar__link} href={item.url}>
                                {item.title}
                            </a>
                        </li>
                    )
                })}
            </ul>
        </nav>
    )
}

export default Navbar
