import React from 'react'
import styles from '../styles/GptComment.module.css';
import { GoDependabot } from "react-icons/go";


function GptComment({gpt}) {

  return (
    
    <div className={styles.container}>
      {gpt}
    </div>
  )
}

export default GptComment
