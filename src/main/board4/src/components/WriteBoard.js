import React, { useEffect, useState } from "react";
import styles from '../styles/WriteBoard.module.css'
import WriteTitle from "./WriteTitle";
import WriteContent from "./WriteContent";
import WriteButton from "./WriteButton";
import axiosInstance from '../utils/apis';
import CodeWriteBoard from "./CodeWriteBoard";
import { useLocation } from "react-router-dom";
import { useHistory } from "react-router-dom/cjs/react-router-dom.min";

function WriteBoard() {
  const location = useLocation();
  const history = useHistory();
  const path = location.pathname;

  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [codeVal, setCodeVal] = useState('');
  
  const handlePostRequest = () => {
    
    axiosInstance.post(path !== '/codequestion/add' ? '/api/question' : '/api/codequestion', {
      title: title,
      content: content,
      codeContent: codeVal,
      
    }).then(resp => {
      alert('게시물 등록 성공')
      history.push(path !== '/codequestion/add' ? '/question' : '/codequestion')
    })
    .catch(err => alert('게시물 등록 실패'));
  }

  const handleTitleChange = newText => {
    setTitle(newText);
  }

  const handleContentChange = newText => {
    setContent(newText);
  }

  const handleCodeChange = newCode => {
    setCodeVal(newCode);
  }

  


  return(
    <div className={styles.container}>
    <div className={styles.WriteBoard}>
      <WriteTitle onTextChange={handleTitleChange}/>
      <div className={styles.Line}></div>
      <div className={styles.Line2}></div>
      <WriteContent id={1} onTextChange={handleContentChange}/>
    </div>
    {path === '/codequestion/add' ? <CodeWriteBoard handleCodeChange={handleCodeChange}/> : < ></>}
    <div>
      <div className={styles.didiv}></div>
      <WriteButton id={path === '/codequestion/add' ? 5 : 3} sendDataToParent={handlePostRequest}/>
    </div>
    </div>
  )
}

export default WriteBoard