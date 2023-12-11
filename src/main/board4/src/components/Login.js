import React, { useContext, useState } from "react";
import styles from "../styles/Login.module.css";
import "../styles/MainCSS.css";
import axios from "axios";
import instance from '../utils/apis'
import { useHistory } from "react-router-dom";
import { AuthContext } from "./AuthProvider";

function Login() {
    const {isLogin, setIsLogin} = useContext(AuthContext);
    const [loginId, setLoginId] = useState("");
    const [loginPw, setLoginPw] = useState("");
    const [ msg, setMsg ] = useState("");
    const history = useHistory(); 

   
        function handleClick() {
            const data = {
                loginId : loginId,
                loginPw : loginPw
        };
    
        
        instance.post('/api/signin', data)
        .then(resp => {
            setIsLogin(true)
            history.push('/')
        })
        .catch(error => {
            console.error('Error:', error);
            console.log("로그인 실패"); // 실패 메시지 설정
        });
        
      };
     

    return (
      <>
        <div className={styles.Login_title}>
        <h2 className={styles.loginText}>로그인</h2>
        </div>
        <div className={styles.login_contents}>
        <div className={styles.mailText}>인증메일을 확인해주세요!</div>
          <div className={styles.id_wapper}>
            <p className={styles.idText}>&nbsp;&nbsp;&nbsp;&nbsp;아이디</p>
            <input className={styles.inputBox} type="id_email" placeholder="       @kumoh.ac.kr" id="id_email" value={loginId} onChange={(e) => setLoginId(e.target.value)} />
          </div>
          <div className={styles.id_wapper}>
            <div className={styles.pwText}>비밀번호</div>
            <input className={styles.pwinputBox} type="password" id="pwd" value={loginPw} onChange={(e) => setLoginPw(e.target.value)} />
          </div>
  
          <div className={styles.login_button_wapper}>
              <button className={styles.loginBtn} type="submit" onClick={handleClick}>로그인</button>
          </div>
        </div>
        </>
    );
}


export default Login;