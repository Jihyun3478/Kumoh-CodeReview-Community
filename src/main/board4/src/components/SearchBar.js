import React, { useState } from 'react'
import styles from '../styles/SearchBar.module.css'

const placeHolderText = '검색어를 입력해주세요.';

function SearchBar({onSearch}) {

  const [searchTerm, setSearchTerm] = useState('');

  const handleInputChange = event => {
    setSearchTerm(event.target.value);
  }

  const handleInputClick = () => {
    onSearch(searchTerm);

    alert(searchTerm + "의 검색 결과입니다.");

    setSearchTerm('');

  }

  const handleResetClick = () => {
    onSearch('');
  }

  return (
    <div className={styles.searchBar}>
      <input type='text' placeholder={placeHolderText} className={styles.textField}
        value={searchTerm}
        onChange={handleInputChange}
      />
      <button type='submit' className={styles.searchBtn} onClick={handleInputClick}>검색</button>
      <button type='submit' className={styles.backBtn} onClick={handleResetClick}>검색 초기화</button>
    </div>
  )
}

export default SearchBar
