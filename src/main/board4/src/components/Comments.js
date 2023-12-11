import React, { useState } from 'react'
import styles from '../styles/Comments.module.css'
import { IoIosHeartEmpty } from "react-icons/io";
import { TfiComment } from "react-icons/tfi";
import { IoHeart } from "react-icons/io5";
import ReComment from './ReComment';
import { MdOutlineSubdirectoryArrowRight } from "react-icons/md";
import Popup from './Popup';

function Comments({ comments }) {
  const [clickedComments, setClickedComments] = useState({});
  const [clickedReComms, setClickedReComms] = useState({});

  const toggleLike = (commentId) => {
    setClickedComments((prevState) => ({
      ...prevState,
      [commentId]: !prevState[commentId],

    }));
    
  };

  const toggleReComment = commentId => {
    setClickedReComms(prevState => ({
      ...prevState,
      [commentId]: !prevState[commentId],
    }));
  };

  const renderComments = (commentList) => {
    if (!Array.isArray(commentList)) {
      
      return [];
    }
    return commentList.map(comment => (
      <div key={comment.question_comment_id} className={comment.parent_id === null ? styles.commentBox : styles.commentBox2}>
        <div className={styles.commentBoxContent}>
          <div className={styles.container}>
          <p className={styles.pTag1}>{comment.writer}</p>
          <p className={styles.pTag2}>{comment.createdate}</p>
          <p className={styles.commentIcon} onClick={() => toggleReComment(comment.question_comment_id)}>
            <TfiComment />
          </p>
          <p className={styles.likeIcon} onClick={() => toggleLike(comment.question_comment_id)}>
            {clickedComments[comment.question_comment_id] ? <IoHeart /> : <IoIosHeartEmpty />}
          </p><Popup isQnaComm={true} writer={comment.writer}/>
          </div>
          <p className={styles.liked}>{comment.likes}</p>
        </div>
        <p className={styles.pTag3}>{comment.content}</p>
        
        {clickedReComms[comment.question_comment_id] ? <ReComment question_comment_id={comment.question_comment_id}/> : <></>}
        {comment.childComments && comment.childComments.length > 0 && (
          <div className={styles.childComments}>
            <MdOutlineSubdirectoryArrowRight />
            {renderComments(comment.childComments)}
          </div>
        )}
      </div>
    ));
        }

  return (
    <div>
      <p className={styles.lenTag}>댓글 [{comments.totalElements}]</p>
      {renderComments(comments.content)}
    </div>
  );
}

export default Comments;