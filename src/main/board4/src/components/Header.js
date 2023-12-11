import React, { useContext, useMemo } from 'react'
import styles from '../styles/Header.module.css'
import { Link } from "react-router-dom";
import { BsFillBellFill } from "react-icons/bs";
import { TbMinusVertical } from "react-icons/tb";
import kitLogo from "../image/kit_LOGO.png";
import axiosInstance from '../utils/apis'
import { useHistory } from 'react-router-dom/cjs/react-router-dom.min';
import { AuthContext } from './AuthProvider';

function Header() {
    const {isLogin, setIsLogin} = useContext(AuthContext);
    
    const history = useHistory();

  const handleLogOut = () => {
    axiosInstance.post('/api/signout')
    .then(resp => { 
        setIsLogin(false);
    })
    .catch(err => {
        alert('error')
    })
  }

  const beforeBtn = <><button className={styles.login} ><Link to='/signin'>로그인</Link></button>
  <button className={styles.register}><Link to='/signup'>회원가입</Link></button></>;

  const afterBtn = <button onClick={handleLogOut} className={styles.logout}>로그아웃</button>

  

  return (
    <div className={styles.Header}>
            <div className={styles.header_wapper}>
                <div className={styles.logo_wapper}>
                    <Link to="/">
                            <img className={styles.kitLogo} src= {kitLogo} alt="kit_logo_img"></img>
                    </Link>
                </div>
 
                
                <div className={styles.QnA_wapper}>
                    <Link to="/question" className={styles.goToQnA}>
                        <span className={styles.QnA}>Q&A</span>    
                    </Link>
                </div>

                <TbMinusVertical />              
    
                <div className={styles.Code_wapper}>
                    <Link to="/codequestion" className={styles.goToCode}>
                        <span className={styles.Code}>Code Review</span>    
                    </Link>
                </div>
                 
                <div className={styles.user_wapper}>
                    {isLogin  ? afterBtn : beforeBtn}
                </div>
                
            </div>
        </div>
  )
}

export default Header
