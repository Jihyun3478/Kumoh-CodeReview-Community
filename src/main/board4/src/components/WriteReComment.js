import React from 'react'
import styles from '../styles/WriteReComment.module.css'

const placeHolderText = '내용을 입력해주세요.';

function  WriteReComment({onTextChange}) {
  
  const handleChange = event => {
    const newValue = event.target.value;
    onTextChange(newValue);
  }

  return(
    <div className={styles.divBox}>
      <textarea placeholder={placeHolderText} className={styles.recomment} rows="10" onChange={handleChange}/>
    </div>
  )
}

export default WriteReComment
