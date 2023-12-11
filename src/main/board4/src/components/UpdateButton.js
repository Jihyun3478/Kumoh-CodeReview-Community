import React from 'react'
import styles from '../styles/UpdateButton.module.css'
import { Link } from 'react-router-dom/cjs/react-router-dom.min'

function UpdateButton({id, pageId}) {
  
  return (
    <button className={styles.upBtn}>
      <Link to={pageId === 1 ? `/question/update/${id}` : `/codequestion/update/${id}`}>수정</Link>
    </button>
  )
}

export default UpdateButton
