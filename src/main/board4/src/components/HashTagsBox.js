import React from 'react'
import styles from '../styles/HashTagsBox.module.css'
import { IoIosHeartEmpty } from "react-icons/io";

function HashTagsBox({likes, ...hashtags}) {
    const hashtag = Object.values(hashtags)[0].hashtags;
    
  return (
    <div className={styles.container}>
        <div className={styles.hashTagBox}>
            {hashtag.map(tag => (
                <p key={tag.tagId} className={styles.hashTag}>#{tag.tagName}</p>
            ))}
      </div>
      <div className={styles.likeDiv}>
      <p className={styles.likeIcon}><IoIosHeartEmpty /></p>
      <p className={styles.likes}>{likes}</p>
      </div>
    </div>
  )
}

export default HashTagsBox
