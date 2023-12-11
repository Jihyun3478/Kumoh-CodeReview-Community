import React, { useState, useCallback } from 'react';
import axiosInstance from '../utils/apis'
import { useHistory } from 'react-router-dom/cjs/react-router-dom.min';
import styles from '../styles/Register.module.css'

function Register() {
  const [stuNum, setStuNum] = useState('');
  const [loginId, setLoginId] = useState('');
  const [loginPw, setLoginPw] = useState('');
  const [confirmPwd, setConfirmPwd] = useState('');
  const [nickname, setNickname] = useState('');
  const history = useHistory();


  const handleStuNumChange = (e) => {
    setStuNum(e.target.value);
  };
  const handleLoginIdChange = (e) => {
    setLoginId(e.target.value);
  };
  const handleLoginPwChange = (e) => {
    setLoginPw(e.target.value);
  };
  const handleConfirmPwdChange = (e) => {
    setConfirmPwd(e.target.value);
  };
  const handleNicknameChange = (e) => {
    setNickname(e.target.value);
  };

  const authRegister = () => {
        const data = {
          stuNum : stuNum,
          loginId : loginId,
          loginPw : loginPw,
          nickname : nickname
        }

      axiosInstance.post('/api/signup', data)
        .then(resp => {
          alert('회원가입이 완료되었습니다');
          history.push('/signin')
      })
      .catch(err => {
          alert('로그인에 실패했습니다.')
      })
  };



  return (
    <div>
      <h2 className={styles.title}>회원가입</h2>
      <div className={styles.container}>
        <input type="text" placeholder="학번" value={stuNum} onChange={handleStuNumChange} className={styles.textBox}/>
        <input type="email" placeholder="웹메일" value={loginId} onChange={handleLoginIdChange} className={styles.emailBox}/>
        <input type="password" placeholder="비밀번호" value={loginPw} onChange={handleLoginPwChange} className={styles.passwordBox}/>
        <input type="password" placeholder="비밀번호를 다시 입력하세요" value={confirmPwd} onChange={handleConfirmPwdChange} className={styles.passwordBox}/>
        <input type="text" placeholder="닉네임" value={nickname} onChange={handleNicknameChange} className={styles.nickNameBox}/>
        <button type="submit" onClick={authRegister} className={styles.btn}>회원 가입</button>


      </div>
      <div className="register_btn">
      </div>
    </div>
  );
}

export default Register
