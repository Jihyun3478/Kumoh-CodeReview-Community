import React, { useMemo } from 'react'
import styles from '../styles/Content.module.css'
import QnAContentHeader from './QnAContentHeader'
import QnAContentBox from './QnAContentBox'
import WriteContent from './WriteContent'
import Comments from './Comments'
import WriteButton from './WriteButton'
import { useEffect, useState } from 'react'
import axiosInstance from '../utils/apis'
import { useParams } from 'react-router-dom'
import GptComment from './GptComment'
import UpdateButton from './UpdateButton'
import DeleteButton from './DeleteButton'
import Popup from './Popup'

function Content() {

  const {Id} = useParams();
  const [data, setData] = useState('');
    const [contents, setContents] = useState({});
    const [comms, setComments] = useState({});
    const [gpt, setGpt] = useState('');

    const totalPages = useMemo(() => {
      return comms.totalPages;
    }, [comms])

    const [currentPage, setCurrentPage] = useState(1);
    const commentsPerPage = 5;


    const handlePostRequest = () => {
    
      axiosInstance.post(`/question/${Id}/comment`, {
        content: data
      }).then(resp => console.log(resp))
      .catch(err => console.log(err));
    }

  useEffect(() => {
    axiosInstance.get(`/question/${Id}`)
      .then(resp => {
        setContents(resp.data);
        setComments(resp.data.questionComments);
        setGpt(resp.data.chatGPT.content);
      })
      
      .catch(error => {
        console.error('데이터를 불러오는 중 오류 발생 : ', error);
      });
  }, [Id]);

  useEffect(() => {
    axiosInstance.get(currentPage !== 1 ? `/question/${Id}?page=${currentPage-1}` : `/question/${Id}`)
    .then(resp => {
      setComments(resp.data.questionComments);
    })
    .catch(error => console.error(error))
  }, [currentPage])

  const handleContent = newVal => {
    setData(newVal);
  }

  const goToPreviousPage = () => {
    if (currentPage > 1) {
      setCurrentPage(currentPage - 1);
    }
  };

  
  const goToNextPage = () => {
    if (currentPage < totalPages) {
      setCurrentPage(currentPage + 1);
    }
  };

  
  const startIndex = (currentPage - 1) * commentsPerPage;
  const endIndex = startIndex + commentsPerPage;

  return (
    <div className={styles.contentBox}>
      <div className={styles.hrDiv}>
      <QnAContentHeader title={contents.title} author={contents.writer} dated={contents.createDate} hit={contents.views}/>
      </div>
        <div className={styles.popDiv}>
        <Popup isQnaCon={true} postId={Id}/>
        </div>
        <QnAContentBox content={contents.content} liked={contents.likes} hashtags={[]}/>
        <div className={styles.btnDiv}>
        <UpdateButton id={Id} pageId={1}/> <DeleteButton id={Id} pageId={1}/>
        </div>

        <div className={styles.hrDiv2}></div>

        <WriteContent id={2} onTextChange={handleContent}/> <WriteButton id={1} sendDataToParent={handlePostRequest}/>

        <div className={styles.hrDiv} />
        {gpt ? <GptComment gpt={gpt}/> : <></>}
        <div>
      
      <Comments comments={comms} />

      
      <div className={styles.pagingDiv}>
      <button className={styles.btn} onClick={goToPreviousPage} disabled={currentPage === 1}>&lt;</button>
      <button className={styles.btn} onClick={goToNextPage} disabled={currentPage === totalPages}>&gt;</button>
      </div>
    </div>
    </div>
  )
}

export default Content