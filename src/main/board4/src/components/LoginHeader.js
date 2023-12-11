import React from 'react'
import styles from '../styles/Header.module.css'
import { Link } from "react-router-dom";
import { BsFillBellFill } from "react-icons/bs";
import { TbMinusVertical } from "react-icons/tb";
import kitLogo from "../image/kit_LOGO.png";
import axiosInstance from '../utils/apis'

function LoginHeader() {

const handleLogOut = () => {
    axiosInstance.post('/api/signout')
    .then(resp => {
        console.log(resp)
    })
    .catch(err => {
        console.log(err)
    })
  }


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
                
                <div className={styles.alarm_wapper}>
                    <div className={styles.alarm}>
                        <BsFillBellFill />
                    </div>
                </div>

                <div className={styles.serch_wapper}>
                    <div className={styles.login_wapper}>
                        <input className={styles.search_input} placeholder="내용을 입력해주세요 :)"></input>
                        <button className={styles.search_button}>검색</button>
                    </div>
                </div>
                    
                <div className={styles.user_wapper}>
                    <button className={styles.login} onClick={handleLogOut}>로그아웃</button>
                </div>
                
            </div>
        </div>
  )
}

export default LoginHeader
