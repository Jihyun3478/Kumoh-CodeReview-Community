import React, { useEffect, useState } from 'react'
import { useHistory, useParams } from 'react-router-dom/cjs/react-router-dom.min'
import axiosInstance from '../utils/apis'
import styles from '../styles/QnaUpdatePage.module.css'


function QnaUpdatePage() {
  const id = useParams();
  const history = useHistory();

  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');

  const onChangeTitle = event => {
    setTitle(event.target.value);
  }

  const onChangeContent = event => {
    setContent(event.target.value);
  }

  const onClickConfirm = event => {
    axiosInstance.patch(`/api/question/${id.id}`, {
      title: title,
      content: content
    })
    .then(resp => {
      alert('수정이 완료되었습니다.')
      history.push('/question');
    })
    .catch(err => {
      alert('수정중 오류가 발생했습니다만..')
      console.error(err);
    })
  }

  useEffect(() => {
    const fetchData = async() => {
      await axiosInstance.get(`/question/${id.id}`)
        .then(resp => {
          setTitle(resp.data.title);
          setContent(resp.data.content);
        })
        .catch(err => {
          console.log(err);
        })
    }

    fetchData();
  }, [id])
  return (
    <>
      <div>
        <textarea className={styles.titleBox} value={title} onChange={onChangeTitle}></textarea>
      </div>
      <hr />
      <div>
        <textarea className={styles.contentBox} value={content} onChange={onChangeContent}></textarea>
      </div>

      <button className={styles.upBtn} onClick={onClickConfirm}>수정</button>
    </>
  )
}

export default QnaUpdatePage
