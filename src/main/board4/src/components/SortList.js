import React, { useState } from 'react'
import styles from '../styles/SortList.module.css'

const options = [
    {
        "value" : 1,
        "name" : "최신순"
    },
    {
        "value" : 2,
        "name" : "공감순"
    }
]

function SortList({onSortChange}) {

  const [selectedSort, setSelectedSort] = useState(options[0].value);

  const handleSortChange = event => {
    const selectedValue = parseInt(event.target.value);
    setSelectedSort(selectedValue);
    onSortChange(selectedValue);
  }
  
  return (
    <select className={styles.selectBox}
      value={selectedSort}
      onChange={handleSortChange}
    >
      {options.map(option => (
        <option value={option.value} key={option.value} className={styles.optionStyle}>{option.name}</option>
      ))}
    </select>
  )
}

export default SortList
