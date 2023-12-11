import React, { useEffect, useState } from 'react'
import Modal from 'react-modal'
import { PiSirenFill } from "react-icons/pi";
import styles from "../styles/Popup.module.css"
import axiosInstance from '../utils/apis'

const modalStyles = {
  overlay: {
    background: "rgba(0, 0, 0, 0.4)",
    width: "100vw",
    height: "100vh",
    zIndex: "10",
    position: "fixed",
    top: "0",
    left: "0",
  },
  content: {
    width: "600px",
    height: "400px",
    zIndex: "150",
    position: "absolute",
    top: "calc(40vh-400px)",
    left: "calc(40vw-600px)",
    transform: "translate(-50%, -50%)",
    borderRadius: "10px",
    boxShadow: "2px 2px 2px rgba(0, 0, 0, 0.25)",
    backgroundColor: "white",
    justifyContent: "center",
    overflow: "auto",
  }
};


function Popup({ isWriterDec = false, isQnaCon = false, isQnaComm = false, isCodeCon = false, isCodeComm = false, writer = '', postId = 0}) {
  const [modalOpen, setModalOpen] = useState(false);
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");

  const handleWriterDec = () => {
    axiosInstance.post('/reportByWriter', {
      writer: writer,
      title: title,
      content: content
    }).then(resp => console.log(resp))
    .then(result => alert('신고가 완료되었습니다.'))
    .catch(err => console.error(err))

    closeModal();
  }
  
  const handleQnaConDec = () => {
    axiosInstance.post('/reportByQuestion', {
      title: title,
      content: content,
      postId: postId
    }).then(resp => console.log(resp))
    .then(result => alert('신고가 완료되었습니다.'))
    .catch(err => console.error(err))
    
    closeModal();
  }
  
  const handleQnaCommDec = () => {
    axiosInstance.post('/reportByQuestionComment', {
      title: title,
      content: content,
      writer: writer
    }).then(resp => console.log(resp))
    .then(result => alert('신고가 완료되었습니다.'))
    .catch(err => console.error(err))

    closeModal();
  }
  
  const handleCodeConDec = () => {
    axiosInstance.post('/reportByCodequestion', {
      title: title,
      content: content,
      postId: postId
    })
    .then(resp => console.log(resp))
    .then(result => alert('신고가 완료되었습니다.'))
    .catch(err => console.error(err))
    closeModal();
  }
  
  const handleCodeCommDec = () => {
    axiosInstance.post('/reportByCodequestionComment', {
      title: title,
      content: content,
      writer: writer
    }).then(resp => console.log(resp))
    .then(result => alert('신고가 완료되었습니다.'))
    .catch(err => console.error(err))

    closeModal();
  }

  const handleTitle = event => {
    setTitle(event.target.value);
  }

  const handleChange = event => {
    setContent(event.target.value);
  }


  function whatIsDec() {
    if (isWriterDec)
      return (
      <>
        <p className={styles.textBox}>작성자 신고</p>
        <textarea className={styles.title} value={title} onChange={handleTitle}></textarea>
        <textarea className={styles.content} value={content} onChange={handleChange}></textarea>

        <button className={styles.btn} onClick={handleWriterDec}>신고</button>
      </>
      )

    else if (isQnaCon)
        return (
      <>
        <p className={styles.textBox}>QnA 게시물 신고</p>
        <textarea className={styles.title} value={title} onChange={handleTitle}></textarea>
    <textarea className={styles.content} value={content} onChange={handleChange}></textarea>
        <button className={styles.btn} onClick={handleQnaConDec}>신고</button>
      </>
          )
    else if (isQnaComm)
          return (
      <>
        <p className={styles.textBox}>QnA 댓글 신고</p>
        <textarea className={styles.title} value={title} onChange={handleTitle}></textarea>
    <textarea className={styles.content} value={content} onChange={handleChange}></textarea>
        <button className={styles.btn} onClick={handleQnaCommDec}>신고</button>
      </>
            )

    else if (isCodeCon)
            return (
          <>
          <p className={styles.textBox}>Code Review 게시물 신고</p>
          <textarea className={styles.title} value={title} onChange={handleTitle}></textarea>
    <textarea className={styles.content} value={content} onChange={handleChange}></textarea>
          <button className={styles.btn} onClick={handleCodeConDec}>신고</button>
          </>
          )

    else if (isCodeComm)
              return(
            <>
              <p className={styles.textBox}>Code Review 댓글 신고</p>
              <textarea className={styles.title} value={title} onChange={handleTitle}></textarea>
              <textarea className={styles.content} value={content} onChange={handleChange}></textarea>
              <button className={styles.btn} onClick={handleCodeCommDec}>신고</button>
            </>
            )
    else
                return <></>
  }

  const openModal = () => {
    setModalOpen(true);
  };

  const closeModal = () => {
    setModalOpen(false);
    setTitle("")
    setContent("")
  };


  return (
    <div>
      <PiSirenFill onClick={openModal} className={styles.siren}/> {/* PiSirenFill 아이콘을 클릭하면 모달을 엽니다. */}
      <Modal
        isOpen={modalOpen}
        onRequestClose={closeModal}
        style={modalStyles}
        ariaHideApp={false}
        shouldCloseOnOverlayClick={true}
      >
        {whatIsDec()}
        
        <button onClick={closeModal}>취소</button>
      </Modal>
    </div>
  );
}

export default Popup;