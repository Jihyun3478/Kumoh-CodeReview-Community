import React from "react";
import styles from '../styles/WriteTitle.module.css'

const placeHolderText = '제목을 입력해주세요.';

function WriteTitle({onTextChange}) {

  const handleChange = event => {
    const newValue = event.target.value;
    onTextChange(newValue);
  }
  return(
    <div>
      <input type='text' placeholder={placeHolderText} className={styles.title} onChange={handleChange}/>
    </div>
  )
}

export default WriteTitle