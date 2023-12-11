import React, {useState} from 'react'
import styles from '../styles/DeleteButton.module.css'
import axiosInstance from '../utils/apis';
import { Link } from 'react-router-dom/cjs/react-router-dom.min';
import { useHistory } from 'react-router-dom/cjs/react-router-dom.min';

function DeleteButton({id, pageId}) {
  const history = useHistory();

  const [showModal, setShowModal] = useState(false);

  const toggleModal = () => {
    setShowModal(!showModal);
  };

  const clickConfirm = () => {
    axiosInstance.delete(pageId === 1 ? `/api/question/${id}` : `/api/codequestion/${id}`)
    .then(resp => {
      console.log(resp)
      history.push(pageId === 1 ?'/question':'/codequestion')
    })
    .catch(err => {
      console.log(err);
    })
  }

  return (
    <div>
      <button className={styles.delBtn} onClick={toggleModal}>삭제</button>
      {showModal && (
        <div className={styles.delText}>
            정말 삭제하시겠습니까?
            <div className={styles.btnDiv}>
            <button onClick={clickConfirm}>삭제</button>
            <button onClick={toggleModal}>취소</button>
          </div>
        </div>
      )}
    </div>
  );
}

export default DeleteButton
