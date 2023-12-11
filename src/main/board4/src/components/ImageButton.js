import React from 'react'
import styles from '../styles/ImageButton.module.css'

function ImageButton() {
  return (
    <div className={styles.ImageButtonDiv}>
        <button type='submit' className={styles.ImageButton}>이미지 첨부</button>
      
    </div>
  )
}

export default ImageButton
