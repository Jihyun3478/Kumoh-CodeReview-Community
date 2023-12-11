import React from "react";
import styles from '../styles/WriteButton.module.css'
import { useHistory } from "react-router-dom";

function WriteButton({id, sendDataToParent}) {
  const history = useHistory();
  let body; //임시



  const locate = () => {
    if (id === 1) {
      sendDataToParent();
      alert('댓글 등록 완료')
    }
    else if (id === 2) {
      history.push(`/question/add`)
    }
    else if (id === 3) {
      sendDataToParent()
      alert('글 작성이 완료되었습니다.')
      history.push(`/question`)
    }
    else if (id === 4) {
      history.push(`/codequestion/add`)
    }
    else if (id === 5) {
      sendDataToParent()
      alert('글 작성이 완료되었습니다.')
      history.push('/codequestion')
    }
  }

  return (
    <div className={styles.WriteButtonDiv}>
      <button type='submit' className={id !== 1 ? styles.WriteButton : styles.EnrollButton} onClick={() => locate()}>{id !== 1 ? '글쓰기' : '댓글 등록'}</button>
    </div>
  )
}

export default WriteButton