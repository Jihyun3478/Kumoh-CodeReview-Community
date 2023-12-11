import React from 'react'
import styles from '../styles/QnAContentBox.module.css'

function QnAContentBox({content, liked, ...hastags}) {
  return (
    <div className={styles.container}>
      <p className={styles.contentBox}>{content}</p>
    
    </div>
  )
}

export default QnAContentBox
