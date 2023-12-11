import React from "react";
import styles from '../styles/WriteContent.module.css'

const placeHolderText = '내용을 입력해주세요.';

function WriteContent({id, onTextChange}) {
  
  const className = id === 1 ? styles.content : styles.WriteContent;

  const handleChange = event => {
    const newValue = event.target.value;
    onTextChange(newValue);
  }

  return(
    <div>
      <textarea placeholder={placeHolderText} className={className} rows="10" onChange={handleChange}/>
    </div>
  )
}

export default WriteContent