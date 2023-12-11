import React from 'react'
import styles from '../styles/QnAContentHeader.module.css'
import Popup from './Popup'

function QnAContentHeader({title, author, dated, hit}) {
  return (
    <div className={styles.container}>
        <p className={styles.qna}>{title}</p>
        <div className={styles.popupDiv}>
        <div className={styles.remnantQna}>
            <div className={styles.pTagsContainer}>
                <p className={styles.pTag}>{author}</p> <p className={styles.pTag}>{dated}</p> <p className={styles.pTag}>{hit}</p>
            </div>
            </div>
            <Popup isWriterDec={true} writer={author}/>
            </div>
    </div> 
  )
}

export default QnAContentHeader
