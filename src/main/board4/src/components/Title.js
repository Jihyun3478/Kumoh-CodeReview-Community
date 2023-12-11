import React from 'react'
import styles from '../styles/Title.module.css'

function Title({pageId}) {
  return (
    <div className={styles.title}>
      {pageId %2 === 1 ? 'Q&A' : 'Code Review'}
    </div>
  )
}

export default Title
