import React, { useEffect, useState } from 'react'
import { useHistory, useParams } from 'react-router-dom/cjs/react-router-dom.min'
import axiosInstance from '../utils/apis'
import CodeWriteBoard from '../components/CodeWriteBoard';
import styles from '../styles/CodeUpdatePage.module.css'


function CodeUpdatePage() {
  const id = useParams();
  const history = useHistory();

  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [codeVal, setCodeVal] = useState('');

  const onChangeTitle = event => {
    setTitle(event.target.value);
  }

  const onChangeContent = event => {
    setContent(event.target.value);
  }

  const onClickConfirm = event => {
    axiosInstance.patch(`/api/codequestion/${id.id}`, {
      title: title,
      content: content,
      codeContent: codeVal
    })
    .then(resp => {
      alert('수정이 완료되었습니다.')
      history.push('/codequestion');
    })
    .catch(err => {
      alert('수정중 오류가 발생했습니다만..')
      console.error(err);
    })
  }

  useEffect(() => {
    const fetchData = async() => {
      await axiosInstance.get(`/codequestion/${id.id}`)
        .then(resp => {
          setTitle(resp.data.title);
          setContent(resp.data.content);
          setCodeVal(resp.data.codeContent);
        })
        .catch(err => {
          console.log(err);
        })
    }

    fetchData();
  }, [id.id])

  const handleCodeChange = newCode => {
    setCodeVal(newCode);
  }
  return (
    <>
      <div>
        <textarea className={styles.titleBox} value={title} onChange={onChangeTitle}></textarea>
      </div>
      <div>
        <textarea className={styles.contentBox} value={content} onChange={onChangeContent}></textarea>
      </div>

      <CodeWriteBoard handleCodeChange={handleCodeChange}/>

      <button className={styles.upBtn} onClick={onClickConfirm}>수정</button>
    </>
  )
}

export default CodeUpdatePage
