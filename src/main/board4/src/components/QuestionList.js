import React, { useEffect, useState } from 'react';
import Question from './Question';
import styles from '../styles/QuestionList.module.css';
import axiosInstance from '../utils/apis';

function QuestionList({pageId, selectedValue, searchValue, currentPage, setTotalItem}) {
  const [contents, setContents] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        let tmpUrl = "";
  
        if (searchValue === '') {
          tmpUrl = pageId === 1 ? "/question" : "/codequestion";
          tmpUrl += selectedValue === 1 ? "" : "ByLikes";
          tmpUrl += currentPage === 1 ? '' : `?page=${currentPage-1}`;
        } else {
          tmpUrl = pageId === 1 ? "/question/search" : "/codequestion/search";
          tmpUrl += `?title=${searchValue}`;
        }
  
        const response = await axiosInstance.get(tmpUrl);
        setTotalItem(response.data.totalElements);
        setContents(response.data.content);
        
      } catch (error) {
        console.error('데이터를 불러오는 중 오류 발생 : ', error);
      }
    };
  
    fetchData();
  
  }, [pageId, selectedValue, searchValue, currentPage]);

  return (
    <ul className={styles.ul}>
      {contents.map(content => (
        <Question key={content.id} pageId={pageId} {...content}/>
      ))}
    </ul>
  );
}

export default QuestionList;
