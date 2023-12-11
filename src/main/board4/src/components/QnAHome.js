import React, { useEffect, useState } from "react";
import { useParams } from 'react-router-dom';
import styles from "../styles/main.module.css";
import axiosInstance from '../utils/apis.js';
import QnAContent from "./QnAContent.js";


function QnAHome() {
    const [boardList, setBoardList] = useState([]);
  
    useEffect(() => {
        const fetchData = async () => {
            const response = await axiosInstance.get('/question/top/five')
           .then(resp => setBoardList(resp.data.content))
           .catch(err => console.log(err))
     }
     fetchData();
      }, []);
  
    return(
        <div className={styles.Main_wapper}>
            <a className={styles.list}>QnA</a>
  
            <div className={styles.list_wapper} style={{display: 'block'}}>
            {
           boardList && 
           boardList.map((board) => { 
            return (
               <QnAContent key={board.Id} contents={board} />
           )})}
              </div>
        </div>
    );
  }
  
  export default QnAHome;
