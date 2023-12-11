import React, { useState } from 'react'
import styles from '../styles/ReComment.module.css'
import { MdOutlineSubdirectoryArrowRight } from "react-icons/md";
import WriteReComment from './WriteReComment';
import { useParams } from 'react-router-dom/cjs/react-router-dom.min';
import axiosInstance from '../utils/apis';
import CodeWriteBoard from './CodeWriteBoard';


function ReComment({question_comment_id}) {
  const [content, setContent] = useState('');
  const {Id} = useParams();


  const clickBtn = () => {
    axiosInstance.post(`/question/${Id}/comment/${question_comment_id}/child`, {
      content: content,
    })
    .then(resp => {
      console.log(resp)
      alert('댓글 등록 완료')
    })
    .catch(err => {
      console.error(err);
      alert('댓글 등록 실패')
    })
  }

  const handleContentChange = newText => {
    setContent(newText);
  }

  return (
    <div className={styles.flexBox}>
    <MdOutlineSubdirectoryArrowRight />
      <WriteReComment onTextChange={handleContentChange} className={styles.inputBox}/>

      <button className={styles.btn} onClick={clickBtn}>대댓글 등록</button>
    </div>
    
  )
}

export default ReComment
