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
import CodeWriteBoard from './CodeWriteBoard'
import UpdateButton from './UpdateButton'
import DeleteButton from './DeleteButton'
import CodeComments from './CodeComment'
import { PiSirenFill } from "react-icons/pi";
import Popup from './Popup'


function CodeDetail({comment}) {

  const {Id} = useParams();
  
  const [data, setData] = useState('');
    const [contents, setContents] = useState({});
    const [comms, setComments] = useState({});
    const [codeVal, setCodeVal] = useState('');
    const [code, setCode] = useState('');
    
    

    const [currentPage, setCurrentPage] = useState(1);
    const commentsPerPage = 5;

    const totalPages = useMemo(() => {
      return comms.totalPages;
    }, [comms])

    const onClickAddi = event => {
      setCurrentPage += 1;
    }

    const handleCodeChange = newCode => {
      setCode(newCode);
    }

    const handlePostRequest = () => {
    
      axiosInstance.post(`/codequestion/${Id}/codecomment`, {
        content: data,
        codeContent: code,
      }).then(resp => {
        console.log(resp)
        alert('댓글 등록 완료')
      })
      .catch(err => {
        console.error(err)
        alert('댓글 등록 실패')
      });
    }

  useEffect(() => {
    axiosInstance.get(`/codequestion/${Id}`)
      .then(resp => {
        
        setContents(resp.data);
        setComments(resp.data.codeQuestionComment);
        setCodeVal(resp.data.codeContent);
        setCode(resp.data.codeContent);
      })
      
      .catch(error => {
        console.error('데이터를 불러오는 중 오류 발생 : ', error);
      });
  }, [Id]);

  const handleContent = newVal => {
    setData(newVal);
  }

  const goToPreviousPage = () => {
    if (currentPage > 1) {
      setCurrentPage(currentPage - 1);
    }
  };

  // Go to the next page
  const goToNextPage = () => {
    if (currentPage < totalPages) {
      setCurrentPage(currentPage + 1);
    }
  };

  useEffect(() => {
    axiosInstance.get(currentPage !== 1 ? `/codequestion/${Id}?page=${currentPage-1}` : `/codequestion/${Id}`)
    .then(resp => {
      setComments(resp.data.codeQuestionComment);
    })
    .catch(error => console.error(error))
  }, [currentPage])

  return (
    <div className={styles.contentBox}>
      <div className={styles.hrDiv}>
      <QnAContentHeader title={contents.title} author={contents.writer} dated={contents.createDate} hit={contents.views}/>
      
      </div>

        <QnAContentBox content={contents.content} liked={contents.likes} hashtags={[]}/>
        <div className={styles.popDiv}>
        <Popup isCodeCon={true} postId={Id}/>

        </div>
        <code className={styles.codeBox}>{codeVal}</code>
        <div className={styles.btnDiv}>
          <UpdateButton id={Id} pageId={2}/> <DeleteButton id={Id} pageId={2}/>
        </div>

        <div className={styles.hrDiv}></div>

        <WriteContent id={2} onTextChange={handleContent}/>
        <CodeWriteBoard handleCodeChange={handleCodeChange} primaryCode={codeVal}/>
        <WriteButton id={1} sendDataToParent={handlePostRequest}/>

        <div className={styles.hrDiv}></div>
        
        <div>
      {/* 댓글 렌더링 */}
      <CodeComments comments={comms} primaryCode={codeVal}/>

      {/* 페이지 이동 버튼 */}
      <div className={styles.pagingDiv}>
        <button className={styles.btn} onClick={goToPreviousPage} disabled={currentPage === 1}>&lt;</button>
        <button className={styles.btn} onClick={goToNextPage} disabled={currentPage === totalPages}>&gt;</button>
      </div>
    </div>
    </div>
  )
}

export default CodeDetail
