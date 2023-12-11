import React, { useEffect, useState } from "react";
import styles from "../styles/main.module.css";
import { CgGirl } from "react-icons/cg";

function QnAContent({contents}) {
    const [content, setContent] = useState(contents);

    useEffect(() => {
        setContent(contents);
    }, [contents]);
    
    return(
        <div className={styles.list_i}>
            <div className={styles.list_contents} style={{display: 'flex'}}>
                <div className="QnA_list_writer"><CgGirl />{contents?.writer}</div>                           
                <div className="QnA_small" style={{display: 'flex'}}>
                    <div className="QnA_list_time">{contents?.createDate}</div>
                    <div className="QnA_list_likes">{contents?.views}</div>
                </div>
            </div>
        <div className="QnA_list_title">{contents?.title}</div>
        </div>
    );
}

export default QnAContent;