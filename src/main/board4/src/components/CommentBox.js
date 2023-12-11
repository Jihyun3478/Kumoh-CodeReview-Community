import React from "react";
import styles from '../styles/CommentBox.module.css'

const placeHolderText = '댓글을 입력해주세요.';

function CommentBox() {
  return(
    <div>
      <textarea placeholder={placeHolderText} className={styles.CommentBox} rows="3"/>
    </div>
  )
}

export default CommentBox