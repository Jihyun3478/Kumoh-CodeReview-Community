import React from 'react'
import styles from '../styles/Board.module.css'
import QuestionListBox from './QuestionListBox'
import SortList from './SortList'
import SearchBar from './SearchBar'
import Title from './Title'
import WriteButton from './WriteButton'

function CodeBoard() {
  return (
    <>
      <Title id={2}/> <SortList /> <WriteButton id={4}/>
      
      <div className={styles.mainBackGround}>
        <QuestionListBox />
      </div>

      <SearchBar />
    </>
  )
}

export default CodeBoard
